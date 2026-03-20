import 'package:shared_preferences/shared_preferences.dart';
import 'package:x_scan/core/rfid/rfid_reader_type.dart';

class AppPrefs {
  static const _keyWebhookUrl = 'app_webhook_url';
  static const _keyReaderType = 'app_reader_type';

  static Future<String> loadWebhookUrl() async {
    final prefs = await SharedPreferences.getInstance();
    return (prefs.getString(_keyWebhookUrl) ?? '').trim();
  }

  static Future<void> saveWebhookUrl(String value) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_keyWebhookUrl, value.trim());
  }

  /// Carrega o tipo de leitor salvo (padrão: c72)
  static Future<RfidReaderType> loadReaderType() async {
    final prefs = await SharedPreferences.getInstance();
    final value = prefs.getString(_keyReaderType);
    return RfidReaderType.fromValue(value);
  }

  /// Salva o tipo de leitor selecionado
  static Future<void> saveReaderType(RfidReaderType type) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_keyReaderType, type.value);
  }
}