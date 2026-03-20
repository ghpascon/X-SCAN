import 'package:flutter/material.dart';
import 'package:x_scan/core/rfid/rfid_reader_type.dart';
import 'package:x_scan/core/rfid/rfid_manager.dart';

class SelectReaderScreen extends StatelessWidget {
  const SelectReaderScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return _SelectReaderBody();
  }
}

class _SelectReaderBody extends StatefulWidget {
  @override
  State<_SelectReaderBody> createState() => _SelectReaderBodyState();
}

class _SelectReaderBodyState extends State<_SelectReaderBody> {
  Future<void> _showCommunicationDialog(RfidReaderType type, String label) async {
    bool loading = true;
    bool? success;
    String message = '';
    Color? feedbackColor;

    await showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) {
        // StatefulBuilder para atualizar o dialog
        return StatefulBuilder(
          builder: (dialogCtx, setDialogState) {
            if (loading) {
              // Inicia comunicação assim que o dialog abre
              Future.microtask(() async {
                await RfidManager.setReaderType(type);
                final reader = RfidManager.reader;
                try {
                  await reader.initialize();
                  final info = await reader.getPlatformInfo();
                  success = true;
                  message = 'Conexão com $label OK!\nModelo: \\${info.model}';
                  feedbackColor = Colors.green;
                } catch (e) {
                  success = false;
                  message = 'Falha ao comunicar com $label:\n$e';
                  feedbackColor = Colors.red;
                }
                loading = false;
                setDialogState(() {});
              });
            }
            return AlertDialog(
              title: Text('Teste de Comunicação'),
              content: SizedBox(
                width: 280,
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    if (loading)
                      Column(
                        children: const [
                          CircularProgressIndicator(),
                          SizedBox(height: 16),
                          Text('Testando comunicação...'),
                        ],
                      )
                    else ...[
                      Icon(
                        success == true ? Icons.check_circle : Icons.cancel,
                        color: feedbackColor,
                        size: 48,
                      ),
                      const SizedBox(height: 16),
                      Text(
                        message,
                        style: TextStyle(
                          color: feedbackColor,
                          fontWeight: FontWeight.bold,
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ],
                ),
              ),
              actions: [
                TextButton(
                  onPressed: loading ? null : () => Navigator.of(dialogCtx).pop(),
                  child: const Text('Fechar'),
                ),
              ],
            );
          },
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Selecionar Leitor RFID')),
      body: ListView(
        padding: const EdgeInsets.all(24),
        children: [
          ListTile(
              leading: const Icon(Icons.nfc),
              title: const Text('C72'),
              subtitle: const Text('Leitor C72 via SDK'),
              onTap: () => _showCommunicationDialog(RfidReaderType.c72, 'C72'),
              ),
              ListTile(
              leading: const Icon(Icons.device_hub),
              title: const Text('AT907'),
              subtitle: const Text('Leitor ATID AT907 via SDK'),
              onTap: () => _showCommunicationDialog(RfidReaderType.at907, 'AT907'),
          ),
        ],
      ),
    );
  }
}
