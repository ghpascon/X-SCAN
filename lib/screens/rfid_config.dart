import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:x_scan/services/reader_prefs.dart';
import 'package:x_scan/services/rfid/rfid_controller.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class RfidConfigScreen extends StatefulWidget {
  const RfidConfigScreen({super.key});

  @override
  State<RfidConfigScreen> createState() => _RfidConfigScreenState();
}

class _RfidConfigScreenState extends State<RfidConfigScreen> {
  static const MethodChannel _channel = MethodChannel('x_scan/rfid');

  bool _loading = true;
  bool _saving = false;
  String? _error;

  int _power = 30;
  int? _rssiThreshold = RfidController.rssiThreshold;

  @override
  void initState() {
    super.initState();
    _loadConfig();
  }

  Future<void> _loadConfig() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      // Carrega potência salva como padrão inicial
      final savedPower = await ReaderPrefs.loadPower();
      setState(() => _power = savedPower);

      final connected = await _channel.invokeMethod<bool>('connect') ?? false;
      if (!connected) {
        throw Exception('Falha ao conectar com o leitor RFID.');
      }

      final raw = await _channel.invokeMethod<Map<dynamic, dynamic>>(
        'getReaderConfig',
      );

      final config =
          raw?.map((key, value) => MapEntry(key.toString(), value)) ??
          <String, dynamic>{};

      // Hardware tem prioridade sobre o valor salvo
      final hwPower = (config['power'] as num?)?.toInt();
      if (hwPower != null) {
        setState(() => _power = hwPower);
      }
    } catch (e) {
      setState(() {
        _error = e.toString();
      });
    } finally {
      setState(() {
        _loading = false;
      });
    }
  }

  Future<void> _saveConfig() async {
    setState(() {
      _saving = true;
      _error = null;
    });

    try {
      final ok = await _channel.invokeMethod<bool>('applyReaderConfig', {
        'power': _power,
      });

      if (ok != true) {
        throw Exception('Leitor recusou os parametros enviados.');
      }

      RfidController.rssiThreshold = _rssiThreshold;

      await ReaderPrefs.save(power: _power, rssiThreshold: _rssiThreshold);

      await _loadConfig();

      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Configuracao aplicada com sucesso.')),
      );
    } catch (e) {
      setState(() {
        _error = e.toString();
      });
    } finally {
      setState(() {
        _saving = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return AppPageScaffold(
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : ListView(
              padding: const EdgeInsets.fromLTRB(16, 16, 16, 24),
              children: [
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(12),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'Configuracao do Leitor',
                          style: Theme.of(context).textTheme.titleMedium,
                        ),
                        const SizedBox(height: 6),
                        const Text('Baseado na API C72: setPower/getPower.'),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 12),
                _buildPowerCard(),
                const SizedBox(height: 10),
                _buildRssiFilterCard(),
                if (_error != null) ...[
                  const SizedBox(height: 12),
                  Text(
                    _error!,
                    style: TextStyle(
                      color: Theme.of(context).colorScheme.error,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
                const SizedBox(height: 16),
                Row(
                  children: [
                    Expanded(
                      child: OutlinedButton.icon(
                        onPressed: _saving ? null : _loadConfig,
                        icon: const Icon(Icons.refresh),
                        label: const Text('Recarregar'),
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: FilledButton.icon(
                        onPressed: _saving ? null : _saveConfig,
                        icon: _saving
                            ? const SizedBox(
                                width: 16,
                                height: 16,
                                child: CircularProgressIndicator(strokeWidth: 2),
                              )
                            : const Icon(Icons.save),
                        label: const Text('Aplicar'),
                      ),
                    ),
                  ],
                ),
              ],
            ),
    );
  }

  Widget _buildPowerCard() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Potencia: $_power dBm'),
            Slider(
              value: _power.toDouble(),
              min: 5,
              max: 30,
              divisions: 25,
              label: _power.toString(),
              onChanged: (value) {
                setState(() {
                  _power = value.round();
                });
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildRssiFilterCard() {
    final enabled = _rssiThreshold != null;
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text('Filtro RSSI minimo', style: Theme.of(context).textTheme.titleSmall),
                Switch(
                  value: enabled,
                  onChanged: (on) => setState(() => _rssiThreshold = on ? -70 : null),
                ),
              ],
            ),
            if (enabled) ...[
              Text('$_rssiThreshold dBm'),
              Slider(
                value: _rssiThreshold!.toDouble(),
                min: -100,
                max: -30,
                divisions: 10,
                label: '$_rssiThreshold dBm',
                onChanged: (value) {
                  setState(() {
                    _rssiThreshold = (value / 5).round() * 5;
                  });
                },
              ),
            ] else
              const Padding(
                padding: EdgeInsets.only(top: 4),
                child: Text('Desativado — todas as tags sao aceitas.'),
              ),
          ],
        ),
      ),
    );
  }
}
