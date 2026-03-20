import 'package:x_scan/core/rfid/rfid_platform_info.dart';
import 'package:x_scan/core/rfid/rfid_tag.dart';

abstract class RfidReader {
  Future<RfidPlatformInfo> getPlatformInfo();

  Future<void> initialize();

  Future<void> startInventory();

  Future<void> stopInventory();

  Future<void> clearTagSession();

  Future<void> dispose();

  Stream<RfidTag> get tags;

  Stream<String> get status;

  /// Obtém configuração atual do leitor (potência, beep, etc)
  Future<Map<String, dynamic>> getConfig();

  /// Aplica configuração ao leitor (potência, beep, etc)
  Future<bool> applyConfig(Map<String, dynamic> config);

  Stream<bool> get trigger;
}
