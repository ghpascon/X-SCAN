import 'dart:io';
import 'dart:convert';

import 'package:device_info_plus/device_info_plus.dart';
import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:x_scan/core/rfid/rfid_tag.dart';
import 'package:x_scan/services/gps_service.dart';
import 'package:x_scan/services/rfid/platform_rfid_reader.dart';
import 'package:x_scan/services/rfid/rfid_controller.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class RfidScreen extends StatefulWidget {
  const RfidScreen({super.key});

  @override
  State<RfidScreen> createState() => _RfidScreenState();
}

class _RfidScreenState extends State<RfidScreen> {
  late final RfidController _controller;
  final GpsService _gpsService = GpsService();
  bool _isSavingLocal = false;
  bool _isStatusExpanded = false;

  @override
  void initState() {
    super.initState();
    _controller = RfidController(reader: PlatformRfidReader())..setup();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AppPageScaffold(
      body: AnimatedBuilder(
        animation: _controller,
        builder: (context, _) {
          return ListView(
            padding: const EdgeInsets.fromLTRB(16, 16, 16, 24),
            children: [
              _buildStatusCard(),
              const SizedBox(height: 12),
              _buildActions(),
              const SizedBox(height: 12),
              _buildTagSummary(),
              const SizedBox(height: 10),
              ..._buildTagTiles(_controller.tags),
            ],
          );
        },
      ),
    );
  }

  Widget _buildStatusCard() {
    final platformInfo = _controller.platformInfo;
    final error = _controller.errorMessage;
    final inventoryText =
        _controller.isInventoryRunning ? 'Leitura em andamento' : 'Leitura parada';
    final connectionText =
      _controller.isReaderConnected ? 'Conectado' : 'Desconectado';
    final deviceLabel = platformInfo?.model ?? '...';

    final cardColor = error != null
        ? Colors.red.shade100
        : _controller.isInventoryRunning
            ? Colors.green.shade100
            : _controller.isReaderConnected
                ? Colors.amber.shade100
                : null;

    return Card(
      color: cardColor,
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            InkWell(
              onTap: () {
                setState(() {
                  _isStatusExpanded = !_isStatusExpanded;
                });
              },
              child: Row(
                children: [
                  const Icon(Icons.devices_other),
                  const SizedBox(width: 8),
                  Expanded(
                    child: Text(
                      'Device: $deviceLabel',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                  ),
                  Icon(_isStatusExpanded ? Icons.expand_less : Icons.expand_more),
                ],
              ),
            ),
            if (_isStatusExpanded) ...[
              const SizedBox(height: 8),
              const Divider(height: 1),
              const SizedBox(height: 8),
              Text('Modelo: ${platformInfo?.model ?? '...'}'),
              Text('Fabricante: ${platformInfo?.manufacturer ?? '...'}'),
              Text('Conexao do leitor: $connectionText'),
              Text('Tipo de leitor: ${platformInfo?.readerType ?? '...'}'),
              Text(
                'SDK detectado: ${platformInfo?.sdkAvailable == true ? 'Sim' : 'Nao'}',
              ),
              Text('Status: $inventoryText'),
              if (error != null) ...[
                const SizedBox(height: 8),
                Text(
                  error,
                  style: TextStyle(
                    color: Theme.of(context).colorScheme.error,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ],
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildActions() {
    return Wrap(
      spacing: 10,
      runSpacing: 10,
      children: [
        OutlinedButton.icon(
          onPressed: _controller.isBusy ? null : _controller.setup,
          icon: const Icon(Icons.refresh),
          label: const Text('Reconectar'),
        ),
        OutlinedButton(
          onPressed: _controller.isBusy ? null : _controller.clearTags,
          child: const Text('Limpar Tags'),
        ),
        OutlinedButton.icon(
          onPressed: (_controller.tags.isEmpty ||
                  _isSavingLocal ||
                  _controller.isInventoryRunning)
              ? null
              : _saveLocally,
          icon: _isSavingLocal
              ? const SizedBox(
                  width: 16,
                  height: 16,
                  child: CircularProgressIndicator(strokeWidth: 2),
                )
              : const Icon(Icons.save),
          label: const Text('Salvar tags localmente'),
        ),
      ],
    );
  }



  Future<void> _saveLocally() async {
    final tags = _controller.tags;
    if (tags.isEmpty) {
      return;
    }

    if (_controller.isInventoryRunning) {
      if (!mounted) {
        return;
      }
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Pare a leitura antes de salvar localmente.')),
      );
      return;
    }

    setState(() => _isSavingLocal = true);

    try {
      final position = await _gpsService.getCurrentPosition();
      final deviceInfo = await DeviceInfoPlugin().androidInfo;
      final docsDir = await getApplicationDocumentsDirectory();
      final file = File('${docsDir.path}/inventory_events.json');

      List<dynamic> events = <dynamic>[];
      if (await file.exists()) {
        final content = await file.readAsString();
        if (content.trim().isNotEmpty) {
          final parsed = jsonDecode(content);
          if (parsed is List<dynamic>) {
            events = parsed;
          }
        }
      }

      final event = <String, dynamic>{
        'device': deviceInfo.id,
        'event_type': 'inventory',
        'event_data': <String, dynamic>{
          'timestamp': DateTime.now().toIso8601String(),
          'gps': <String, dynamic>{
            'latitude': position.latitude,
            'longitude': position.longitude,
          },
          'tags': tags
              .map(
                (tag) => <String, dynamic>{
                  'epc': tag.epc,
                  'tid': tag.tid,
                  'rssi': tag.rssi,
                  'count': tag.count,
                  'timestamp': tag.timestamp.toIso8601String(),
                },
              )
              .toList(),
        },
      };

      events.add(event);
      await file.writeAsString(
        const JsonEncoder.withIndent('  ').convert(events),
      );

      if (!mounted) {
        return;
      }
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Evento salvo localmente (${events.length}).')),
      );
      Navigator.of(context).popUntil((route) => route.isFirst);
    } catch (e) {
      if (!mounted) {
        return;
      }
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao salvar localmente: $e')),
      );
    } finally {
      if (mounted) {
        setState(() => _isSavingLocal = false);
      }
    }
  }



  Widget _buildTagSummary() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text('Tags unicas: ${_controller.tags.length}'),
            Text('Leituras: ${_controller.totalReads}'),
          ],
        ),
      ),
    );
  }

  List<Widget> _buildTagTiles(List<RfidTag> tags) {
    if (tags.isEmpty) {
      return const [
        Card(
          child: Padding(
            padding: EdgeInsets.all(16),
            child: Text('Nenhuma tag lida ainda.'),
          ),
        ),
      ];
    }

    return tags.map((tag) {
      return Card(
        margin: const EdgeInsets.only(bottom: 8),
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // EPC
              SelectableText(
                tag.epc,
                style: Theme.of(context).textTheme.titleSmall?.copyWith(
                  fontFamily: 'monospace',
                  fontWeight: FontWeight.w600,
                ),
              ),
              
              // TID (se disponível)
              if (tag.tid != null && tag.tid!.isNotEmpty) ...[
                const SizedBox(height: 4),
                Text(
                  'TID: ${tag.tid}',
                  style: const TextStyle(
                    fontFamily: 'monospace',
                    fontSize: 12,
                    color: Colors.grey,
                  ),
                ),
              ],
              
              // Info line: Count | RSSI | Timestamp
              const SizedBox(height: 8),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    'Cont: ${tag.count}',
                    style: const TextStyle(fontSize: 12),
                  ),
                  Text(
                    'RSSI: ${tag.rssi ?? '-'}',
                    style: const TextStyle(fontSize: 12),
                  ),
                  Flexible(
                    child: Text(
                      tag.timestamp.toIso8601String().split('T')[1].split('.')[0],
                      style: const TextStyle(fontSize: 11, color: Colors.grey),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      );
    }).toList();
  }
}
