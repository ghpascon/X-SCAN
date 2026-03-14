import 'package:geolocator/geolocator.dart';

class GpsService {
  Future<Position> getCurrentPosition() async {
    final serviceEnabled = await Geolocator.isLocationServiceEnabled();

    if (!serviceEnabled) {
      throw const GpsServiceException(
        'Servico de localizacao desativado no dispositivo.',
      );
    }

    var permission = await Geolocator.checkPermission();

    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
    }

    if (permission == LocationPermission.denied) {
      throw const GpsServiceException(
        'Permissao de localizacao negada pelo usuario.',
      );
    }

    if (permission == LocationPermission.deniedForever) {
      throw const GpsServiceException(
        'Permissao negada permanentemente. Ative nas configuracoes do Android.',
      );
    }

    return Geolocator.getCurrentPosition(
      locationSettings: const LocationSettings(
        accuracy: LocationAccuracy.high,
      ),
    );
  }
}

class GpsServiceException implements Exception {
  const GpsServiceException(this.message);

  final String message;

  @override
  String toString() => message;
}
