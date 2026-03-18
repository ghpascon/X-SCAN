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
            onPressed: () => Navigator.of(context).pushNamed('/rfid'),
            icon: const Icon(Icons.nfc),
            label: const Text('RFID'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/sync'),
            icon: const Icon(Icons.sync),
            label: const Text('Sincronizar'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/utils'),
            icon: const Icon(Icons.extension),
            label: const Text('Utils'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/configuration'),
            icon: const Icon(Icons.settings),
            label: const Text('Configuracoes'),
          ),
        ],
      ),
    );
  }
}
