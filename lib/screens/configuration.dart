import 'package:flutter/material.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class ConfigurationScreen extends StatelessWidget {
  const ConfigurationScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return AppPageScaffold(
      body: ListView(
        padding: const EdgeInsets.fromLTRB(16, 20, 16, 24),
        children: [
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/select-reader'),
            icon: const Icon(Icons.tune),
            label: const Text('Selecionar Leitor RFID'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/rfid-config'),
            icon: const Icon(Icons.settings_input_component),
            label: const Text('Configurar RFID'),
          ),
          const SizedBox(height: 14),
          FilledButton.icon(
            onPressed: () => Navigator.of(context).pushNamed('/app-settings'),
            icon: const Icon(Icons.settings),
            label: const Text('Configuracoes do app'),
          ),
        ],
      ),
    );
  }
}
