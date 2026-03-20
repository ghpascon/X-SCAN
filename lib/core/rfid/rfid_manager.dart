import 'package:x_scan/core/rfid/rfid_reader.dart';
import 'package:x_scan/services/rfid/platform_rfid_reader.dart';
import 'package:x_scan/services/rfid/platform_at907_reader.dart';
import 'package:x_scan/core/rfid/rfid_reader_type.dart';
import 'package:x_scan/services/reader_prefs.dart';

class RfidManager {
  static RfidReaderType _type = RfidReaderType.c72;

  static Future<void> loadType() async {
    _type = await ReaderPrefs.loadReaderType();
  }

  static Future<void> setReaderType(RfidReaderType type) async {
    _type = type;
    await ReaderPrefs.saveReaderType(type);
  }

  static RfidReader get reader {
    switch (_type) {
      case RfidReaderType.c72:
        return PlatformRfidReader();
      case RfidReaderType.at907:
        return PlatformAt907Reader();
      default:
        return PlatformRfidReader();
    }
  }
}
