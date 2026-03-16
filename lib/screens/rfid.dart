import 'package:flutter/material.dart';
import 'package:x_scan/core/rfid/rfid_tag.dart';
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
              const SizedBox(height: 16),
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

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('RFID C72', style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 8),
            Text('Modelo: ${platformInfo?.model ?? '...'}'),
            Text('Fabricante: ${platformInfo?.manufacturer ?? '...'}'),
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
          onPressed: _controller.clearTags,
          child: const Text('Limpar Tags'),
        ),
        const Chip(
          avatar: Icon(Icons.settings_input_antenna, size: 18),
          label: Text('Segure o gatilho para ler'),
        ),
      ],
    );
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
        child: ListTile(
          dense: true,
          title: Text(tag.epc, style: const TextStyle(fontFamily: 'monospace')),
          subtitle: Text(
            'Count: ${tag.count} | RSSI: ${tag.rssi?.toString() ?? '-'} | ${tag.timestamp.toIso8601String()}',
          ),
        ),
      );
    }).toList();
  }
}
