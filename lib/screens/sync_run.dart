import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:x_scan/services/app_prefs.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class SyncRunScreen extends StatefulWidget {
  const SyncRunScreen({super.key});

  @override
  State<SyncRunScreen> createState() => _SyncRunScreenState();
}

class _SyncRunScreenState extends State<SyncRunScreen> {
  bool _running = true;
  int _total = 0;
  int _processed = 0;
  int _successCount = 0;
  int _failureCount = 0;
  String _statusText = 'Preparando sincronizacao...';
  final List<_SyncFailure> _failures = <_SyncFailure>[];

  @override
  void initState() {
    super.initState();
    _startSync();
  }

  Future<void> _startSync() async {
    final webhook = await AppPrefs.loadWebhookUrl();
    if (!_isValidWebhook(webhook)) {
      _finishWithError(
        'Webhook invalido.',
        'Configure uma URL http/https valida em Configuracoes do app.',
      );
      return;
    }

    final queue = await _loadQueueEvents();
    if (queue.isEmpty) {
      _finishWithError(
        'Fila vazia.',
        'Nao ha eventos pendentes para enviar.',
      );
      return;
    }

    setState(() {
      _total = queue.length;
      _statusText = 'Enviando eventos para o webhook...';
    });

    final client = HttpClient();
    client.connectionTimeout = const Duration(seconds: 12);
    final failedEvents = <Map<String, dynamic>>[];

    try {
      for (var index = 0; index < queue.length; index += 1) {
        final event = queue[index];
        final failureReason = await _sendEvent(client, webhook, event);

        if (!mounted) {
          return;
        }

        setState(() {
          _processed = index + 1;
          if (failureReason == null) {
            _successCount += 1;
          } else {
            _failureCount += 1;
            _failures.add(
              _SyncFailure(
                eventType: event['event_type']?.toString() ?? 'evento',
                reason: failureReason,
              ),
            );
            failedEvents.add(event);
          }
        });
      }

      await _saveQueueEvents(failedEvents);

      if (!mounted) {
        return;
      }

      setState(() {
        _running = false;
        _statusText = 'Sincronizacao concluida.';
      });
    } finally {
      client.close(force: true);
    }
  }

  Future<String?> _sendEvent(
    HttpClient client,
    String webhook,
    Map<String, dynamic> event,
  ) async {
    try {
      final request = await client.postUrl(Uri.parse(webhook));
      request.headers.contentType = ContentType.json;
      request.write(jsonEncode(event));

      final response = await request.close();
      final body = await utf8.decoder.bind(response).join();
      if (response.statusCode >= 200 && response.statusCode < 300) {
        return null;
      }

      final compactBody = body.trim().replaceAll(RegExp(r'\s+'), ' ');
      if (compactBody.isEmpty) {
        return 'HTTP ${response.statusCode}';
      }

      final preview = compactBody.length > 220
          ? '${compactBody.substring(0, 220)}...'
          : compactBody;
      return 'HTTP ${response.statusCode}: $preview';
    } on SocketException catch (e) {
      return 'Falha de rede: ${e.message}';
    } on HandshakeException {
      return 'Falha SSL/TLS ao conectar no webhook';
    } on HttpException catch (e) {
      return 'Erro HTTP: ${e.message}';
    } catch (e) {
      return 'Erro inesperado: $e';
    }
  }

  void _finishWithError(String title, String reason) {
    if (!mounted) return;
    setState(() {
      _running = false;
      _statusText = title;
      _failureCount = 1;
      _failures
        ..clear()
        ..add(_SyncFailure(eventType: 'sincronizacao', reason: reason));
    });
  }

  bool _isValidWebhook(String raw) {
    final uri = Uri.tryParse(raw);
    if (uri == null) {
      return false;
    }
    return (uri.scheme == 'http' || uri.scheme == 'https') && uri.host.isNotEmpty;
  }

  Future<List<Map<String, dynamic>>> _loadQueueEvents() async {
    final docsDir = await getApplicationDocumentsDirectory();
    final file = File('${docsDir.path}/inventory_events.json');
    if (!await file.exists()) {
      return <Map<String, dynamic>>[];
    }

    final content = await file.readAsString();
    if (content.trim().isEmpty) {
      return <Map<String, dynamic>>[];
    }

    final parsed = jsonDecode(content);
    if (parsed is! List) {
      return <Map<String, dynamic>>[];
    }

    return parsed
        .whereType<Map>()
        .map((e) => e.map((k, v) => MapEntry(k.toString(), v)))
        .toList();
  }

  Future<void> _saveQueueEvents(List<Map<String, dynamic>> events) async {
    final docsDir = await getApplicationDocumentsDirectory();
    final file = File('${docsDir.path}/inventory_events.json');
    await file.writeAsString(jsonEncode(events));
  }

  Widget _buildMetricCard(
    BuildContext context, {
    required String label,
    required String value,
    required Color color,
    required IconData icon,
  }) {
    return Expanded(
      child: Card(
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Icon(icon, color: color),
              const SizedBox(height: 10),
              Text(
                value,
                style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                  fontWeight: FontWeight.w700,
                  color: color,
                ),
              ),
              const SizedBox(height: 4),
              Text(label),
            ],
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return AppPageScaffold(
      body: ListView(
        padding: const EdgeInsets.fromLTRB(16, 16, 16, 24),
        children: [
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Execucao da sincronizacao',
                    style: Theme.of(context).textTheme.titleMedium,
                  ),
                  const SizedBox(height: 8),
                  Text(_statusText),
                  const SizedBox(height: 14),
                  LinearProgressIndicator(
                    value: _total == 0 ? null : _processed / _total,
                  ),
                  const SizedBox(height: 10),
                  Text('Processados: $_processed de $_total'),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Row(
            children: [
              _buildMetricCard(
                context,
                label: 'Sucessos',
                value: '$_successCount',
                color: Colors.green,
                icon: Icons.check_circle,
              ),
              _buildMetricCard(
                context,
                label: 'Falhas',
                value: '$_failureCount',
                color: Colors.red,
                icon: Icons.error,
              ),
            ],
          ),
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Motivos de falha',
                    style: Theme.of(context).textTheme.titleSmall,
                  ),
                  const SizedBox(height: 8),
                  if (_failures.isEmpty)
                    const Text('Nenhuma falha registrada.')
                  else
                    ..._failures.map(
                      (failure) => Padding(
                        padding: const EdgeInsets.only(bottom: 10),
                        child: Container(
                          width: double.infinity,
                          padding: const EdgeInsets.all(12),
                          decoration: BoxDecoration(
                            color: Colors.red.withOpacity(0.06),
                            borderRadius: BorderRadius.circular(10),
                            border: Border.all(
                              color: Colors.red.withOpacity(0.15),
                            ),
                          ),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                failure.eventType,
                                style: const TextStyle(fontWeight: FontWeight.w700),
                              ),
                              const SizedBox(height: 4),
                              Text(failure.reason),
                            ],
                          ),
                        ),
                      ),
                    ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          FilledButton.icon(
            onPressed: _running ? null : () => Navigator.of(context).pop(),
            icon: const Icon(Icons.arrow_back),
            label: Text(_running ? 'Sincronizando...' : 'Voltar'),
          ),
        ],
      ),
    );
  }
}

class _SyncFailure {
  const _SyncFailure({
    required this.eventType,
    required this.reason,
  });

  final String eventType;
  final String reason;
}
