import 'package:flutter/material.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class BarcodeTestScreen extends StatefulWidget {
  const BarcodeTestScreen({super.key});

  @override
  State<BarcodeTestScreen> createState() => _BarcodeTestScreenState();
}

class _BarcodeTestScreenState extends State<BarcodeTestScreen> {
  final TextEditingController _controller = TextEditingController();
  final FocusNode _focusNode = FocusNode();
  final List<String> _history = <String>[];

  String _lastCode = '-';

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (!mounted) return;
      _focusNode.requestFocus();
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    _focusNode.dispose();
    super.dispose();
  }

  void _consumeInput() {
    final raw = _controller.text.trim();
    if (raw.isEmpty) {
      return;
    }

    setState(() {
      _lastCode = raw;
      _history.insert(0, raw);
      if (_history.length > 80) {
        _history.removeRange(80, _history.length);
      }
      _controller.clear();
    });

    _focusNode.requestFocus();
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
                    'Teste de Barcode',
                    style: Theme.of(context).textTheme.titleMedium,
                  ),
                  const SizedBox(height: 8),
                  const Text(
                    'Use o scanner lateral. O codigo sera digitado neste campo.',
                  ),
                  const SizedBox(height: 10),
                  TextField(
                    controller: _controller,
                    focusNode: _focusNode,
                    autofocus: true,
                    textInputAction: TextInputAction.done,
                    onSubmitted: (_) => _consumeInput(),
                    onChanged: (value) {
                      if (value.contains('\n') || value.contains('\r')) {
                        _controller.text = value.replaceAll('\n', '').replaceAll('\r', '');
                        _controller.selection = TextSelection.fromPosition(
                          TextPosition(offset: _controller.text.length),
                        );
                        _consumeInput();
                      }
                    },
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Codigo lido',
                      hintText: 'Aponte e pressione o gatilho',
                    ),
                  ),
                  const SizedBox(height: 10),
                  const Text('Ultima leitura:'),
                  const SizedBox(height: 4),
                  SelectableText(
                    _lastCode,
                    style: Theme.of(context).textTheme.titleMedium?.copyWith(
                      fontFamily: 'monospace',
                      fontWeight: FontWeight.w700,
                    ),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          OutlinedButton.icon(
            onPressed: () {
              setState(() {
                _history.clear();
                _lastCode = '-';
                _controller.clear();
              });
              _focusNode.requestFocus();
            },
            icon: const Icon(Icons.clear_all),
            label: const Text('Limpar historico'),
          ),
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Historico (${_history.length})',
                    style: Theme.of(context).textTheme.titleSmall,
                  ),
                  const SizedBox(height: 8),
                  if (_history.isEmpty)
                    const Text('Nenhum codigo lido ainda.')
                  else
                    ..._history.map(
                      (code) => Padding(
                        padding: const EdgeInsets.only(bottom: 8),
                        child: Container(
                          width: double.infinity,
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: Colors.black.withValues(alpha: 0.03),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: SelectableText(
                            code,
                            style: const TextStyle(fontFamily: 'monospace'),
                          ),
                        ),
                      ),
                    ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}