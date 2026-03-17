import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:device_info_plus/device_info_plus.dart';
import 'package:flutter/services.dart';
import 'package:x_scan/core/rfid/rfid_device_profile.dart';
import 'package:x_scan/core/rfid/rfid_device_profile_repository.dart';
import 'package:x_scan/core/rfid/rfid_failure.dart';
import 'package:x_scan/core/rfid/rfid_platform_info.dart';
import 'package:x_scan/core/rfid/rfid_reader.dart';
import 'package:x_scan/core/rfid/rfid_reader_type.dart';
import 'package:x_scan/core/rfid/rfid_tag.dart';

class PlatformRfidReader implements RfidReader {
    static const RfidDeviceProfileRepository _profileRepository =
        RfidDeviceProfileRepository();

  static const MethodChannel _channel = MethodChannel('x_scan/rfid');
  static const EventChannel _tagsEventChannel = EventChannel('x_scan/rfid/tags');
  static const EventChannel _connectedEventChannel =
    EventChannel('x_scan/rfid/connected');
  static const EventChannel _triggerEventChannel =
    EventChannel('x_scan/rfid/trigger');

  final StreamController<RfidTag> _tagsController =
      StreamController<RfidTag>.broadcast();
  final StreamController<String> _statusController =
      StreamController<String>.broadcast();
  final StreamController<bool> _triggerController =
    StreamController<bool>.broadcast();

  StreamSubscription<dynamic>? _tagEventSubscription;
  StreamSubscription<dynamic>? _connectedEventSubscription;
  StreamSubscription<dynamic>? _triggerEventSubscription;
  bool _initialized = false;
  bool _isConnected = false;
  SelectedRfidProfile? _selectedProfile;

  @override
  Stream<RfidTag> get tags => _tagsController.stream;

  @override
  Stream<String> get status => _statusController.stream;

  @override
  Stream<bool> get trigger => _triggerController.stream;

  @override
  Future<RfidPlatformInfo> getPlatformInfo() async {
    if (!Platform.isAndroid) {
      throw const RfidFailure('RFID suportado apenas em Android.');
    }

    final androidInfo = await _getAndroidInfo();
    final selected = await _resolveSelectedProfile(androidInfo);
    final platformVersion = await _channel.invokeMethod<String>(
      'getPlatformVersion',
    );

    return RfidPlatformInfo(
      manufacturer: androidInfo.manufacturer,
      brand: androidInfo.brand,
      model: androidInfo.model,
      device: androidInfo.device,
      product: androidInfo.product,
      hardware: androidInfo.hardware,
      board: androidInfo.board,
      androidSdkInt: androidInfo.version.sdkInt,
      sdkAvailable: platformVersion != null && platformVersion.isNotEmpty,
      readerName: selected.profile.displayName,
      readerType: selected.profile.readerType.value,
      profileId: selected.profile.id,
    );
  }

  @override
  Future<void> initialize() async {
    if (_initialized) {
      if (!_isConnected) {
        _statusController.add('Sessao inicializada sem conexao ativa, reconectando...');
        await _reconnect();
      }
      return;
    }

    final androidInfo = await _getAndroidInfo();
    final selected = await _resolveSelectedProfile(androidInfo);
    _statusController.add(
      'Perfil RFID selecionado: ${selected.profile.id} (${selected.profile.readerType.value})',
    );

    if (selected.profile.readerType != RfidReaderType.uhfUart) {
      throw RfidFailure(
        'Leitor ${selected.profile.readerType.value} ainda nao implementado. '
        'Perfil selecionado: ${selected.profile.id}',
      );
    }

    _tagEventSubscription = _tagsEventChannel
        .receiveBroadcastStream()
        .listen(
          _handleTagEvent,
          onError: (Object error) {
            _statusController.add('Erro no stream RFID: $error');
          },
        );

    _connectedEventSubscription = _connectedEventChannel
        .receiveBroadcastStream()
        .listen(
          _handleConnectedEvent,
          onError: (Object error) {
            _statusController.add('Erro no status de conexao RFID: $error');
          },
        );

    _triggerEventSubscription = _triggerEventChannel
        .receiveBroadcastStream()
        .listen(
          _handleTriggerEvent,
          onError: (Object error) {
            _statusController.add('Erro no stream do gatilho RFID: $error');
          },
        );

    final success = await _connectWithRetry(
      attempts: 4,
      reason: 'Inicializacao',
      cleanBeforeFirstAttempt: false,
    );

    _isConnected = success;
    if (!_isConnected) {
      throw RfidFailure(
        _connectionFailureMessage('Falha na inicializacao do RFID.'),
      );
    }

    // Give hardware a short settle window before first inventory start.
    await Future<void>.delayed(const Duration(milliseconds: 250));

    _initialized = true;
    _statusController.add('RFID inicializado via SDK nativo');
  }

  @override
  Future<void> startInventory() async {
    if (!_initialized) {
      await initialize();
    }

    // If stream says disconnected, attempt one reconnect before inventory.
    if (!_isConnected) {
      await _reconnect();
    }

    var success = await _invokeBool('startContinuous');
    if (success != true) {
      // Retry once with reconnect for transient UHFOpenAndConnect/start errors.
      _statusController.add('Falha no start inicial, tentando reconectar...');
      await _reconnect();
      success = await _invokeBool('startContinuous');
    }

    if (success != true) {
      throw RfidFailure(
        'Falha ao iniciar inventario RFID apos reconexao.\n'
        'Diagnostico: UHF pode estar sem handshake (ex.: Status=254 / UHFOpenAndConnect=-1).\n'
        'Acoes: feche app leitor nativo, reinicie servico RFID no PDA e tente novamente.',
      );
    }
    _statusController.add('Leitura iniciada');
  }

  @override
  Future<void> stopInventory() async {
    final success = await _invokeBool('stop');
    if (success != true) {
      throw const RfidFailure('Falha ao parar inventario RFID.');
    }
    _statusController.add('Leitura parada');
  }

  @override
  Future<void> dispose() async {
    await _invokeBool('stop');
    await _invokeBool('clearData');
    await _invokeBool('close');
    await _tagEventSubscription?.cancel();
    await _connectedEventSubscription?.cancel();
    await _triggerEventSubscription?.cancel();
    await _tagsController.close();
    await _statusController.close();
    await _triggerController.close();
  }

  void _handleConnectedEvent(dynamic event) {
    if (event is bool) {
      _isConnected = event;
      _statusController.add(event ? 'RFID conectado' : 'RFID desconectado');
    }
  }

  void _handleTriggerEvent(dynamic event) {
    if (event is bool) {
      _triggerController.add(event);
      return;
    }

    if (event is num) {
      _triggerController.add(event != 0);
    }
  }

  Future<void> _reconnect() async {
    final success = await _connectWithRetry(
      attempts: 3,
      reason: 'Reconexao',
      cleanBeforeFirstAttempt: true,
    );

    _isConnected = success;
    if (!_isConnected) {
      throw RfidFailure(
        _connectionFailureMessage('Falha ao reconectar com o modulo RFID.'),
      );
    }

    await Future<void>.delayed(const Duration(milliseconds: 220));
    _statusController.add('Reconexao RFID concluida');
  }

  Future<bool> _connectWithRetry({
    required int attempts,
    required String reason,
    required bool cleanBeforeFirstAttempt,
  }) async {
    for (var i = 1; i <= attempts; i++) {
      final shouldClean = cleanBeforeFirstAttempt || i > 1;
      if (shouldClean) {
        await _invokeBool('stop');
        await _invokeBool('close');
        await Future<void>.delayed(const Duration(milliseconds: 260));
      }

      _statusController.add('$reason: tentativa de conexao $i/$attempts');
      final connected = await _invokeBool('connect');
      if (connected == true) {
        _statusController.add('$reason: conectado com sucesso na tentativa $i');
        return true;
      }

      _statusController.add(
        '$reason: tentativa $i falhou (connect=false). Possivel UART ocupada/sem resposta.',
      );

      // Backoff gradual para hardware UHF que demora a liberar UART.
      final waitMs = 300 + (i * 180);
      await Future<void>.delayed(Duration(milliseconds: waitMs));
    }

    _statusController.add(
      '$reason: todas tentativas falharam. Indicativo de handshake UHF indisponivel (Status=254).',
    );
    return false;
  }

  String _connectionFailureMessage(String prefix) {
    return '$prefix\n'
        'Diagnostico: o modulo UHF nao respondeu ao handshake (comum em logs com Status=254 / UHFOpenAndConnect=-1).\n'
        'Checklist rapido:\n'
        '1) Fechar app RFID nativo/servicos em background que usam o leitor.\n'
        '2) Evitar hot-reload repetido durante teste de hardware.\n'
        '3) Reiniciar o servico RFID ou o dispositivo e testar novamente.';
  }

  Future<AndroidDeviceInfo> _getAndroidInfo() async {
    final deviceInfo = DeviceInfoPlugin();
    return deviceInfo.androidInfo;
  }

  Future<SelectedRfidProfile> _resolveSelectedProfile(
    AndroidDeviceInfo androidInfo,
  ) async {
    final cached = _selectedProfile;
    if (cached != null) {
      return cached;
    }

    final identity = DeviceIdentity(
      manufacturer: androidInfo.manufacturer,
      brand: androidInfo.brand,
      model: androidInfo.model,
      device: androidInfo.device,
      product: androidInfo.product,
      hardware: androidInfo.hardware,
      board: androidInfo.board,
      sdkInt: androidInfo.version.sdkInt,
    );
    final selected = await _profileRepository.selectProfile(identity);
    _selectedProfile = selected;
    return selected;
  }

  void _handleTagEvent(dynamic event) {
    if (event == null) {
      return;
    }

    final raw = event.toString();
    if (raw.isEmpty) {
      return;
    }

    final parsed = _decodeTagPayload(raw);
    for (final tag in parsed) {
      final epc = (tag['KEY_EPC'] ?? '').toString();
      if (epc.isEmpty) {
        continue;
      }
      
      // Parse RSSI - handle both integer and decimal formats
      final rssiRaw = tag['KEY_RSSI'] ?? '';
      int? rssiInt;
      if (rssiRaw.toString().isNotEmpty) {
        // Try direct int parse first
        rssiInt = int.tryParse(rssiRaw.toString());
        
        // If that fails, try parsing as double then converting to int
        if (rssiInt == null) {
          final rssiDouble = double.tryParse(rssiRaw.toString());
          if (rssiDouble != null) {
            rssiInt = rssiDouble.toInt();
          }
        }
      }
      
      _tagsController.add(
        RfidTag(
          epc: epc,
          tid: _normalizeString(tag['KEY_TID']),
          rssi: rssiInt,
          count: int.tryParse((tag['KEY_COUNT'] ?? '').toString()) ?? 1,
          timestamp: DateTime.now(),
        ),
      );
    }
  }

  Future<bool?> _invokeBool(String method, [Map<String, Object?>? args]) {
    return _channel.invokeMethod<bool>(method, args);
  }

  List<Map<String, dynamic>> _decodeTagPayload(String raw) {
    try {
      final decoded = jsonDecode(raw);
      if (decoded is List) {
        return decoded
            .whereType<Map>()
            .map((entry) => entry.map(
                  (key, value) => MapEntry(key.toString(), value),
                ))
            .toList();
      }
    } catch (_) {
      _statusController.add('Falha ao decodificar payload de tags.');
    }
    return const <Map<String, dynamic>>[];
  }

  String? _normalizeString(Object? value) {
    final text = value?.toString().trim() ?? '';
    if (text.isEmpty) {
      return null;
    }
    return text;
  }
}
