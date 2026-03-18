import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:x_scan/screens/sync_run.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class SyncScreen extends StatefulWidget {
  const SyncScreen({super.key});

  @override
  State<SyncScreen> createState() => _SyncScreenState();
}

class _SyncScreenState extends State<SyncScreen> {
  late Future<int> _queueCountFuture;

  @override
  void initState() {
    super.initState();
    _queueCountFuture = _getQueueCount();
  }

  Future<int> _getQueueCount() async {
    try {
      final docsDir = await getApplicationDocumentsDirectory();
      final file = File('${docsDir.path}/inventory_events.json');

      if (!await file.exists()) {
        return 0;
      }

      final content = await file.readAsString();
      if (content.trim().isEmpty) {
        return 0;
      }

      final parsed = jsonDecode(content);
      if (parsed is! List) {
        return 0;
      }

      return parsed.length;
    } catch (e) {
      return 0;
    }
  }

  Future<void> _reloadQueueCount() async {
    setState(() {
      _queueCountFuture = _getQueueCount();
    });
    await _queueCountFuture;
  }

  Future<void> _openSyncRun() async {
    await Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (context) => const SyncRunScreen(),
      ),
    );
    if (!mounted) return;
    await _reloadQueueCount();
  }

  @override
  Widget build(BuildContext context) {
    return AppPageScaffold(
      body: ListView(
        padding: const EdgeInsets.fromLTRB(16, 16, 16, 24),
        children: [
          Card(
            child: Padding(
              padding: const EdgeInsets.all(12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Sincronizacao',
                    style: Theme.of(context).textTheme.titleMedium,
                  ),
                  const SizedBox(height: 6),
                  const Text('Area de fila local e envio de eventos.'),
                  const SizedBox(height: 12),
                  FutureBuilder<int>(
                    future: _queueCountFuture,
                    builder: (context, snapshot) {
                      final count = snapshot.data ?? 0;
                      return Text(
                        'Eventos na fila: $count',
                        style: const TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.w500,
                          color: Colors.blue,
                        ),
                      );
                    },
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          FilledButton.icon(
            onPressed: () async {
              await Navigator.of(context).pushNamed('/sync-queue');
              if (!mounted) return;
              await _reloadQueueCount();
            },
            icon: const Icon(Icons.list_alt),
            label: const Text('Ver fila'),
          ),
          const SizedBox(height: 12),
          FilledButton.icon(
            onPressed: _openSyncRun,
            icon: const Icon(Icons.cloud_upload),
            label: const Text('Enviar dados'),
          ),
        ],
      ),
    );
  }
}
