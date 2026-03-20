import 'package:shared_preferences/shared_preferences.dart';
import 'package:x_scan/services/rfid/rfid_controller.dart';
import 'package:x_scan/core/rfid/rfid_reader_type.dart';

/// Persiste as configurações do leitor RFID entre sessões.
class ReaderPrefs {
    static const _keyReaderType = 'reader_type';
    /// Salva o tipo de leitor selecionado
    static Future<void> saveReaderType(RfidReaderType type) async {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(_keyReaderType, type.value);
    }

    /// Carrega o tipo de leitor salvo (padrão: c72)
    static Future<RfidReaderType> loadReaderType() async {
      final prefs = await SharedPreferences.getInstance();
      final value = prefs.getString(_keyReaderType);
      return RfidReaderType.fromValue(value);
    }
  static const _keyPower = 'reader_power';
  static const _keyRssi = 'reader_rssi_threshold';
  static const _keyBeepEnabled = 'reader_beep_enabled';
  static const _keyEpcPrefixes = 'reader_epc_prefixes';

  /// Carrega as configurações salvas e aplica imediatamente ao controller.
  /// Deve ser chamado antes de runApp.
  static Future<void> load() async {
    final prefs = await SharedPreferences.getInstance();
    if (prefs.containsKey(_keyRssi)) {
      RfidController.rssiThreshold = prefs.getInt(_keyRssi);
    }
    RfidController.beepEnabled = prefs.getBool(_keyBeepEnabled) ?? true;
    final savedPrefixes = prefs.getStringList(_keyEpcPrefixes) ?? <String>[];
    RfidController.epcPrefixes = _normalizePrefixes(savedPrefixes);
  }

  /// Retorna a potência salva (padrão 30 dBm).
  static Future<int> loadPower() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getInt(_keyPower) ?? 30;
  }

  /// Retorna se o beep por leitura esta habilitado (padrao: true).
  static Future<bool> loadBeepEnabled() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getBool(_keyBeepEnabled) ?? true;
  }

  /// Retorna os prefixos de EPC salvos para filtro.
  static Future<List<String>> loadEpcPrefixes() async {
    final prefs = await SharedPreferences.getInstance();
    final raw = prefs.getStringList(_keyEpcPrefixes) ?? <String>[];
    return _normalizePrefixes(raw);
  }

  /// Persiste potência e limiar de RSSI.
  static Future<void> save({
    required int power,
    required int? rssiThreshold,
    required bool beepEnabled,
    required List<String> epcPrefixes,
  }) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt(_keyPower, power);
    await prefs.setBool(_keyBeepEnabled, beepEnabled);
    await prefs.setStringList(_keyEpcPrefixes, _normalizePrefixes(epcPrefixes));
    if (rssiThreshold == null) {
      await prefs.remove(_keyRssi);
    } else {
      await prefs.setInt(_keyRssi, rssiThreshold);
    }
  }

  static List<String> _normalizePrefixes(List<String> values) {
    final normalized = values
        .map((value) => value.trim().toLowerCase())
        .where((value) => value.isNotEmpty)
        .toSet()
        .toList();
    normalized.sort();
    return normalized;
  }
}
