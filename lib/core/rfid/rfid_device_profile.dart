import 'package:x_scan/core/rfid/rfid_reader_type.dart';

class DeviceIdentity {
  const DeviceIdentity({
    required this.manufacturer,
    required this.brand,
    required this.model,
    required this.device,
    required this.product,
    required this.hardware,
    required this.board,
    required this.sdkInt,
  });

  final String manufacturer;
  final String brand;
  final String model;
  final String device;
  final String product;
  final String hardware;
  final String board;
  final int sdkInt;
}

class DeviceMatchRule {
  const DeviceMatchRule({
    this.manufacturerContains = const <String>[],
    this.brandContains = const <String>[],
    this.modelContains = const <String>[],
    this.deviceContains = const <String>[],
    this.productContains = const <String>[],
    this.hardwareContains = const <String>[],
    this.boardContains = const <String>[],
  });

  final List<String> manufacturerContains;
  final List<String> brandContains;
  final List<String> modelContains;
  final List<String> deviceContains;
  final List<String> productContains;
  final List<String> hardwareContains;
  final List<String> boardContains;

  factory DeviceMatchRule.fromJson(Map<String, dynamic> json) {
    List<String> toLowerStringList(Object? value) {
      if (value is! List) {
        return const <String>[];
      }
      return value
          .whereType<String>()
          .map((item) => item.trim().toLowerCase())
          .where((item) => item.isNotEmpty)
          .toList();
    }

    return DeviceMatchRule(
      manufacturerContains: toLowerStringList(json['manufacturerContains']),
      brandContains: toLowerStringList(json['brandContains']),
      modelContains: toLowerStringList(json['modelContains']),
      deviceContains: toLowerStringList(json['deviceContains']),
      productContains: toLowerStringList(json['productContains']),
      hardwareContains: toLowerStringList(json['hardwareContains']),
      boardContains: toLowerStringList(json['boardContains']),
    );
  }

  bool matches(DeviceIdentity identity) {
    bool matchesAny(List<String> patterns, String source) {
      if (patterns.isEmpty) {
        return true;
      }
      final lowered = source.toLowerCase();
      return patterns.any(lowered.contains);
    }

    return matchesAny(manufacturerContains, identity.manufacturer) &&
        matchesAny(brandContains, identity.brand) &&
        matchesAny(modelContains, identity.model) &&
        matchesAny(deviceContains, identity.device) &&
        matchesAny(productContains, identity.product) &&
        matchesAny(hardwareContains, identity.hardware) &&
        matchesAny(boardContains, identity.board);
  }
}

class RfidDeviceProfile {
  const RfidDeviceProfile({
    required this.id,
    required this.displayName,
    required this.readerType,
    required this.priority,
    required this.match,
  });

  final String id;
  final String displayName;
  final RfidReaderType readerType;
  final int priority;
  final DeviceMatchRule match;

  factory RfidDeviceProfile.fromJson(Map<String, dynamic> json) {
    return RfidDeviceProfile(
      id: (json['id'] as String? ?? '').trim(),
      displayName: (json['displayName'] as String? ?? '').trim(),
      readerType: RfidReaderType.fromValue(json['readerType'] as String?),
      priority: json['priority'] is int ? json['priority'] as int : 999,
      match: DeviceMatchRule.fromJson((json['match'] as Map?)?.cast<String, dynamic>() ?? const <String, dynamic>{}),
    );
  }
}

class SelectedRfidProfile {
  const SelectedRfidProfile({required this.profile, required this.identity});

  final RfidDeviceProfile profile;
  final DeviceIdentity identity;
}
