import 'package:flutter/material.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return AppPageScaffold(
      body: ListView(
        padding: const EdgeInsets.fromLTRB(16, 20, 16, 24),
        children: [

          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/info'),
            icon: const Icon(Icons.info_outline),
            label: const Text('Info'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/gps'),
            icon: const Icon(Icons.gps_fixed),
            label: const Text('GPS'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/rfid'),
            icon: const Icon(Icons.nfc),
            label: const Text('RFID'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/barcode-test'),
            icon: const Icon(Icons.qr_code_scanner),
            label: const Text('Teste Barcode'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/rfid-config'),
            icon: const Icon(Icons.tune),
            label: const Text('Configurar Leitor RFID'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/sync'),
            icon: const Icon(Icons.sync),
            label: const Text('Sincronizar'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/app-settings'),
            icon: const Icon(Icons.settings),
            label: const Text('Configuracoes do app'),
          ),
          const SizedBox(height: 14),
        ],
      ),
    );
  }
}
