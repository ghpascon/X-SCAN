import 'package:shared_preferences/shared_preferences.dart';

class AppPrefs {
  static const _keyWebhookUrl = 'app_webhook_url';

  static Future<String> loadWebhookUrl() async {
    final prefs = await SharedPreferences.getInstance();
    return (prefs.getString(_keyWebhookUrl) ?? '').trim();
  }

  static Future<void> saveWebhookUrl(String value) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_keyWebhookUrl, value.trim());
  }
}