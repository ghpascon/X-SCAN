import 'package:device_info_plus/device_info_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class InfoScreen extends StatefulWidget {
  const InfoScreen({super.key});

  @override
  State<InfoScreen> createState() => _InfoScreenState();
}

class _InfoScreenState extends State<InfoScreen> {
  static const MethodChannel _hardwareChannel = MethodChannel(
    'x_scan/hardware_features',
  );

  late Future<List<_DeviceField>> _deviceInfoFuture;

  @override
  void initState() {
    super.initState();
    _deviceInfoFuture = _loadDeviceInfo();
  }

  Future<List<_DeviceField>> _loadDeviceInfo() async {
    try {
      final info = await DeviceInfoPlugin().androidInfo;
      final hardware = await _getHardwareFeatures();

      return [
        _DeviceField(label: 'Plataforma', value: 'Android'),
        _DeviceField(label: 'Marca', value: info.brand),
        _DeviceField(label: 'Modelo', value: info.model),
        _DeviceField(label: 'Dispositivo', value: info.device),
        _DeviceField(label: 'Fabricante', value: info.manufacturer),
        _DeviceField(label: 'Versao Android', value: info.version.release),
        _DeviceField(label: 'SDK', value: info.version.sdkInt.toString()),
        _DeviceField(label: 'Hardware', value: info.hardware),
        _DeviceField(label: 'isPhysicalDevice', value: info.isPhysicalDevice.toString()),
        _DeviceField(
          label: 'RFID / NFC',
          value: _availability(hardware['rfidNfc']),
        ),
        _DeviceField(
          label: 'Bluetooth',
          value: _availability(hardware['bluetooth']),
        ),
        _DeviceField(
          label: 'Bluetooth LE',
          value: _availability(hardware['bluetoothLe']),
        ),
        _DeviceField(
          label: 'Scanner de codigo de barras',
          value: _availability(hardware['barcodeScanner']),
        ),
        _DeviceField(
          label: 'Camera',
          value: _availability(hardware['camera']),
        ),
        _DeviceField(
          label: 'Camera com autofocus',
          value: _availability(hardware['cameraAutofocus']),
        ),
        _DeviceField(
          label: 'Biometria (digital)',
          value: _availability(hardware['fingerprint']),
        ),
        _DeviceField(
          label: 'Wi-Fi',
          value: _availability(hardware['wifi']),
        ),
        _DeviceField(
          label: 'USB Host',
          value: _availability(hardware['usbHost']),
        ),
        _DeviceField(
          label: 'Microfone',
          value: _availability(hardware['microphone']),
        ),
        _DeviceField(
          label: 'GPS',
          value: _availability(hardware['gps']),
        ),
      ];
    } on MissingPluginException {
      return [
        _DeviceField(label: 'Status', value: 'Plugin nao registrado na execucao atual'),
        _DeviceField(label: 'Acao recomendada', value: 'Pare o app e execute flutter run novamente'),
      ];
    } catch (error) {
      return [
        _DeviceField(label: 'Status', value: 'Falha ao coletar dados do dispositivo'),
        _DeviceField(label: 'Erro tecnico', value: error.toString()),
      ];
    }
  }

  Future<Map<String, bool>> _getHardwareFeatures() async {
    final result = await _hardwareChannel
        .invokeMapMethod<String, dynamic>('getHardwareFeatures');

    if (result == null) {
      return const <String, bool>{};
    }

    return result.map(
      (key, value) => MapEntry(key, value == true),
    );
  }

  String _availability(bool? available) {
    return available == true ? 'Disponivel' : 'Nao disponivel';
  }

  Future<void> _refresh() async {
    setState(() {
      _deviceInfoFuture = _loadDeviceInfo();
    });

    await _deviceInfoFuture;
  }

  @override
  Widget build(BuildContext context) {
    final colorScheme = Theme.of(context).colorScheme;

    return AppPageScaffold(
      body: RefreshIndicator(
        onRefresh: _refresh,
        child: FutureBuilder<List<_DeviceField>>(
          future: _deviceInfoFuture,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            }

            if (snapshot.hasError) {
              return ListView(
                padding: const EdgeInsets.all(20),
                children: [
                  _ErrorState(
                    message: 'Nao foi possivel carregar as informacoes do dispositivo.',
                    onRetry: _refresh,
                  ),
                ],
              );
            }

            final fields = snapshot.data ?? const <_DeviceField>[];

            return ListView(
              physics: const AlwaysScrollableScrollPhysics(),
              padding: const EdgeInsets.fromLTRB(16, 18, 16, 24),
              children: [
                Container(
                  padding: const EdgeInsets.all(18),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(16),
                    gradient: LinearGradient(
                      colors: [
                        colorScheme.primary.withValues(alpha: 0.92),
                        colorScheme.primary.withValues(alpha: 0.76),
                      ],
                      begin: Alignment.topLeft,
                      end: Alignment.bottomRight,
                    ),
                    boxShadow: [
                      BoxShadow(
                        color: colorScheme.primary.withValues(alpha: 0.18),
                        blurRadius: 20,
                        offset: const Offset(0, 12),
                      ),
                    ],
                  ),
                  child: const Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Device Info',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 22,
                          fontWeight: FontWeight.w700,
                        ),
                      ),
                      SizedBox(height: 6),
                      Text(
                        'Dados tecnicos do dispositivo para suporte e diagnostico.',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 13,
                          height: 1.4,
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 14),
                ...fields.map(_InfoTile.new),
              ],
            );
          },
        ),
      ),
    );
  }
}

class _InfoTile extends StatelessWidget {
  const _InfoTile(this.field);

  final _DeviceField field;

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;

    return Container(
      margin: const EdgeInsets.only(bottom: 10),
      padding: const EdgeInsets.fromLTRB(14, 12, 14, 12),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: const Color(0xFFE8EEF3)),
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Expanded(
            flex: 3,
            child: Text(
              field.label,
              style: textTheme.bodyMedium?.copyWith(
                color: const Color(0xFF45617A),
                fontWeight: FontWeight.w600,
              ),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            flex: 4,
            child: Text(
              field.value,
              textAlign: TextAlign.right,
              style: textTheme.bodyMedium?.copyWith(
                color: const Color(0xFF1C2A36),
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _ErrorState extends StatelessWidget {
  const _ErrorState({
    required this.message,
    required this.onRetry,
  });

  final String message;
  final Future<void> Function() onRetry;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(18),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: const Color(0xFFE8EEF3)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            message,
            style: Theme.of(context).textTheme.bodyMedium,
          ),
          const SizedBox(height: 12),
          FilledButton.icon(
            onPressed: onRetry,
            icon: const Icon(Icons.refresh),
            label: const Text('Tentar novamente'),
          ),
        ],
      ),
    );
  }
}

class _DeviceField {
  const _DeviceField({
    required this.label,
    required this.value,
  });

  final String label;
  final String value;
}
