import 'package:shared_preferences/shared_preferences.dart';
import 'package:x_scan/services/rfid/rfid_controller.dart';

/// Persiste as configurações do leitor RFID entre sessões.
class ReaderPrefs {
  static const _keyPower = 'reader_power';
  static const _keyRssi = 'reader_rssi_threshold';

  /// Carrega as configurações salvas e aplica imediatamente ao controller.
  /// Deve ser chamado antes de runApp.
  static Future<void> load() async {
    final prefs = await SharedPreferences.getInstance();
    if (prefs.containsKey(_keyRssi)) {
      RfidController.rssiThreshold = prefs.getInt(_keyRssi);
    }
  }

  /// Retorna a potência salva (padrão 30 dBm).
  static Future<int> loadPower() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getInt(_keyPower) ?? 30;
  }

  /// Persiste potência e limiar de RSSI.
  static Future<void> save({
    required int power,
    required int? rssiThreshold,
  }) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt(_keyPower, power);
    if (rssiThreshold == null) {
      await prefs.remove(_keyRssi);
    } else {
      await prefs.setInt(_keyRssi, rssiThreshold);
    }
  }
}
