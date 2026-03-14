import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:x_scan/services/gps_service.dart';
import 'package:x_scan/widgets/app_page_scaffold.dart';

class GpsScreen extends StatefulWidget {
  const GpsScreen({super.key});

  @override
  State<GpsScreen> createState() => _GpsScreenState();
}

class _GpsScreenState extends State<GpsScreen> {
  final GpsService _gpsService = GpsService();

  bool _loading = false;
  Position? _position;
  String? _error;

  Future<void> _testGps() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      final position = await _gpsService.getCurrentPosition();

      if (!mounted) {
        return;
      }

      setState(() {
        _position = position;
      });
    } catch (error) {
      if (!mounted) {
        return;
      }

      setState(() {
        _error = error.toString();
      });
    } finally {
      if (!mounted) {
        return;
      }

      setState(() {
        _loading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;

    return AppPageScaffold(
      body: ListView(
        padding: const EdgeInsets.fromLTRB(16, 20, 16, 24),
        children: [
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(14),
              border: Border.all(color: const Color(0xFFE8EEF3)),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Teste de GPS',
                  style: textTheme.titleMedium?.copyWith(
                    fontWeight: FontWeight.w700,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  'Toque no botao para ler a localizacao atual do dispositivo.',
                  style: textTheme.bodyMedium,
                ),
                const SizedBox(height: 16),
                SizedBox(
                  width: double.infinity,
                  child: FilledButton.icon(
                    onPressed: _loading ? null : _testGps,
                    icon: _loading
                        ? const SizedBox(
                            height: 18,
                            width: 18,
                            child: CircularProgressIndicator(strokeWidth: 2),
                          )
                        : const Icon(Icons.gps_fixed),
                    label: Text(_loading ? 'Lendo GPS...' : 'Testar GPS'),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 14),
          if (_position != null)
            _GpsResultCard(position: _position!)
          else
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(14),
                border: Border.all(color: const Color(0xFFE8EEF3)),
              ),
              child: const Text(
                'Nenhuma localizacao lida ainda.',
              ),
            ),
          if (_error != null) ...[
            const SizedBox(height: 12),
            Container(
              padding: const EdgeInsets.all(14),
              decoration: BoxDecoration(
                color: const Color(0xFFFFF4F4),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: const Color(0xFFFFD3D3)),
              ),
              child: Text(
                _error!,
                style: textTheme.bodyMedium?.copyWith(
                  color: const Color(0xFF8B1A1A),
                ),
              ),
            ),
          ],
        ],
      ),
    );
  }
}

class _GpsResultCard extends StatelessWidget {
  const _GpsResultCard({required this.position});

  final Position position;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: const Color(0xFFE8EEF3)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Localizacao atual',
            style: TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w700,
            ),
          ),
          const SizedBox(height: 12),
          _GpsLine(label: 'Latitude', value: position.latitude.toStringAsFixed(6)),
          _GpsLine(label: 'Longitude', value: position.longitude.toStringAsFixed(6)),
          _GpsLine(label: 'Precisao (m)', value: position.accuracy.toStringAsFixed(1)),
          _GpsLine(label: 'Altitude (m)', value: position.altitude.toStringAsFixed(1)),
          _GpsLine(label: 'Velocidade (m/s)', value: position.speed.toStringAsFixed(2)),
        ],
      ),
    );
  }
}

class _GpsLine extends StatelessWidget {
  const _GpsLine({
    required this.label,
    required this.value,
  });

  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Row(
        children: [
          Expanded(
            child: Text(
              label,
              style: const TextStyle(
                color: Color(0xFF466177),
                fontWeight: FontWeight.w600,
              ),
            ),
          ),
          Text(
            value,
            style: const TextStyle(
              color: Color(0xFF1C2A36),
              fontWeight: FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }
}
