import 'dart:convert';

import 'package:flutter/services.dart' show rootBundle;
import 'package:x_scan/core/rfid/rfid_device_profile.dart';
import 'package:x_scan/core/rfid/rfid_reader_type.dart';

class RfidDeviceProfileRepository {
  const RfidDeviceProfileRepository();

  static const String defaultConfigPath = 'assets/config/rfid_device_profiles.json';

  Future<List<RfidDeviceProfile>> loadProfiles({
    String assetPath = defaultConfigPath,
  }) async {
    final raw = await rootBundle.loadString(assetPath);
    final decoded = jsonDecode(raw);
    if (decoded is! List) {
      return const <RfidDeviceProfile>[];
    }

    return decoded
        .whereType<Map>()
        .map((item) => item.cast<String, dynamic>())
        .map(RfidDeviceProfile.fromJson)
        .where((profile) =>
            profile.id.isNotEmpty &&
            profile.displayName.isNotEmpty &&
            profile.readerType != RfidReaderType.unknown)
        .toList()
      ..sort((a, b) => a.priority.compareTo(b.priority));
  }

  Future<SelectedRfidProfile> selectProfile(DeviceIdentity identity) async {
    final profiles = await loadProfiles();

    final matched = profiles.where((profile) => profile.match.matches(identity));
    final selected = matched.isNotEmpty
        ? matched.first
        : const RfidDeviceProfile(
            id: 'fallback_unknown',
            displayName: 'Fallback UHF UART',
            readerType: RfidReaderType.uhfUart,
            priority: 9999,
            match: DeviceMatchRule(),
          );

    return SelectedRfidProfile(profile: selected, identity: identity);
  }
}
