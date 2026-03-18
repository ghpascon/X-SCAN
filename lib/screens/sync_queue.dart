import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class SyncQueueScreen extends StatefulWidget {
  const SyncQueueScreen({super.key});

  @override
  State<SyncQueueScreen> createState() => _SyncQueueScreenState();
}

class _SyncQueueScreenState extends State<SyncQueueScreen> {
  late Future<List<Map<String, dynamic>>> _queueFuture;

  void _showEventJson(Map<String, dynamic> event, int index) {
    final prettyJson = const JsonEncoder.withIndent('  ').convert(event);
    showDialog<void>(
      context: context,
      builder: (dialogContext) {
        return AlertDialog(
          title: const Text('JSON do evento'),
          content: SizedBox(
            width: double.maxFinite,
            child: SingleChildScrollView(
              child: SelectableText(
                prettyJson,
                style: const TextStyle(fontFamily: 'monospace', fontSize: 12),
              ),
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(dialogContext).pop(),
              child: const Text('Fechar'),
            ),
            TextButton(
              onPressed: () {
                Navigator.of(dialogContext).pop();
                _deleteEvent(index);
              },
              child: const Text('Excluir',
                  style: TextStyle(color: Colors.red)),
            ),
          ],
        );
      },
    );
  }

  @override
  void initState() {
    super.initState();
    _queueFuture = _loadQueue();
  }

  Future<List<Map<String, dynamic>>> _loadQueue() async {
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

  Future<void> _refresh() async {
    setState(() {
      _queueFuture = _loadQueue();
    });
    await _queueFuture;
  }

  Future<void> _deleteEvent(int index) async {
    try {
      final docsDir = await getApplicationDocumentsDirectory();
      final file = File('${docsDir.path}/inventory_events.json');

      if (!await file.exists()) {
        return;
      }

      final content = await file.readAsString();
      if (content.trim().isEmpty) {
        return;
      }

      final parsed = jsonDecode(content);
      if (parsed is! List) {
        return;
      }

      // Remove event at index
      (parsed as List).removeAt(index);

      // Save updated list back to file
      await file.writeAsString(jsonEncode(parsed));

      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Evento excluído da fila')),
      );

      // Refresh the queue
      await _refresh();
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao excluir evento: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return AppPageScaffold(
      body: RefreshIndicator(
        onRefresh: _refresh,
        child: FutureBuilder<List<Map<String, dynamic>>>(
          future: _queueFuture,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            }

            if (snapshot.hasError) {
              return ListView(
                padding: const EdgeInsets.all(16),
                children: [
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(12),
                      child: Text('Erro ao carregar fila: ${snapshot.error}'),
                    ),
                  ),
                ],
              );
            }

            final queue = snapshot.data ?? const <Map<String, dynamic>>[];
            if (queue.isEmpty) {
              return ListView(
                physics: const AlwaysScrollableScrollPhysics(),
                padding: const EdgeInsets.all(16),
                children: const [
                  Card(
                    child: Padding(
                      padding: EdgeInsets.all(16),
                      child: Text('Fila vazia. Nenhum evento salvo localmente.'),
                    ),
                  ),
                ],
              );
            }

            return ListView.builder(
              physics: const AlwaysScrollableScrollPhysics(),
              padding: const EdgeInsets.fromLTRB(16, 16, 16, 24),
              itemCount: queue.length + 1,
              itemBuilder: (context, index) {
                if (index == 0) {
                  return Card(
                    margin: const EdgeInsets.only(bottom: 10),
                    child: Padding(
                      padding: const EdgeInsets.all(12),
                      child: Text('Eventos na fila: ${queue.length}'),
                    ),
                  );
                }

                final event = queue[index - 1];
                final eventType = event['event_type']?.toString() ?? '-';

                return Card(
                  margin: const EdgeInsets.only(bottom: 8),
                  child: ListTile(
                    title: Text(eventType),
                    subtitle: const Text('Toque para ver o JSON completo'),
                    trailing: const Icon(Icons.chevron_right),
                    onTap: () => _showEventJson(event, index - 1),
                  ),
                );
              },
            );
          },
        ),
      ),
    );
  }
}
