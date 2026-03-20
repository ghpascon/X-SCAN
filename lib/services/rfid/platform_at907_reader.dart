import 'package:at907_plugin/at907_plugin.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:x_scan/core/rfid/rfid_reader.dart';
import 'package:x_scan/core/rfid/rfid_platform_info.dart';
import 'package:x_scan/core/rfid/rfid_tag.dart';
import 'package:x_scan/core/rfid/rfid_failure.dart';
import 'package:flutter/services.dart';
import 'dart:async';

class PlatformAt907Reader implements RfidReader {
  @override
  Future<Map<String, dynamic>> getConfig() async {
    final rawPower = await At907Plugin.getPower();
    // O SDK retorna x10, então ajusta para mostrar ao usuário
    final power = rawPower != null ? (rawPower ~/ 10) : 30;
    return {
      'power': power,
      'beepEnabled': true,
    };
  }

  @override
  Future<bool> applyConfig(Map<String, dynamic> config) async {
    final power = config['power'] as int?;
    if (power != null) {
      // O SDK espera x10
      final ok = await At907Plugin.setPower(power * 10);
      if (!ok) return false;
    }
    // beepEnabled não implementado ainda
    return true;
  }
  static final EventChannel _triggerEventChannel = EventChannel('x_scan/rfid/trigger');
  StreamSubscription<dynamic>? _triggerEventSubscription;
  final StreamController<bool> _triggerController = StreamController<bool>.broadcast();

  @override
  Stream<RfidTag> get tags => Stream.empty(); // TODO: implementar stream de tags

  @override
  Stream<String> get status => Stream.empty(); // TODO: implementar status

  @override
  Stream<bool> get trigger => _triggerController.stream;

  PlatformAt907Reader() {
    _triggerEventSubscription = _triggerEventChannel.receiveBroadcastStream().listen(
      (event) {
        if (event is bool) {
          _triggerController.add(event);
        } else if (event is num) {
          _triggerController.add(event != 0);
        }
      },
      onError: (Object error) {
        _triggerController.add(false);
      },
    );
  }

  // ...existing code...
  // ...existing code...

  @override
  Future<void> clearTagSession() async {
    // TODO: implementar limpeza de sessão de tags
  }

  @override
  Future<RfidPlatformInfo> getPlatformInfo() async {
    // Puxa dados reais do Android
    final androidInfo = await DeviceInfoPlugin().androidInfo;
    final fw = await At907Plugin.getFirmwareVersion();
    return RfidPlatformInfo(
      manufacturer: androidInfo.manufacturer,
      brand: androidInfo.brand,
      model: androidInfo.model,
      device: androidInfo.device,
      product: androidInfo.product,
      hardware: androidInfo.hardware,
      board: androidInfo.board,
      androidSdkInt: androidInfo.version.sdkInt,
      sdkAvailable: fw != null && fw.isNotEmpty,
      readerName: 'AT907 (at907_plugin)',
      readerType: 'at907',
      profileId: '',
    );
  }

  @override
  Future<void> initialize() async {
    final ok = await At907Plugin.connect();
    if (!ok) {
      throw const RfidFailure('Falha ao conectar ao AT907');
    }
  }

  @override
  Future<void> startInventory() async {
    final ok = await At907Plugin.startInventory();
    if (!ok) {
      throw const RfidFailure('Falha ao iniciar inventário AT907');
    }
  }

  @override
  Future<void> stopInventory() async {
    final ok = await At907Plugin.stopInventory();
    if (!ok) {
      throw const RfidFailure('Falha ao parar inventário AT907');
    }
  }

  @override
  Future<void> dispose() async {
    await _triggerEventSubscription?.cancel();
    await _triggerController.close();
    try {
      await At907Plugin.stopInventory();
    } catch (e) {
      // Ignora erro de não inicializado
    }
  }
}
