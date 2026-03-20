import 'package:flutter/services.dart';

class At907Plugin {
    static Future<int?> getPower() async {
      return await _channel.invokeMethod<int>('getPower');
    }

    static Future<bool> setPower(int power) async {
      final result = await _channel.invokeMethod<bool>('setPower', {'power': power});
      return result == true;
    }
  static const MethodChannel _channel = MethodChannel('at907_plugin');

  static Future<bool> connect() async {
    final result = await _channel.invokeMethod('connect');
    return result == true;
  }

  static Future<String?> getFirmwareVersion() async {
    return await _channel.invokeMethod('getFirmwareVersion');
  }

  static Future<bool> startInventory() async {
    final result = await _channel.invokeMethod('startInventory');
    return result == true;
  }

  static Future<bool> stopInventory() async {
    final result = await _channel.invokeMethod('stopInventory');
    return result == true;
  }
}
