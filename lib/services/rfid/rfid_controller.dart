import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:x_scan/core/rfid/rfid_platform_info.dart';
import 'package:x_scan/core/rfid/rfid_reader.dart';
import 'package:x_scan/core/rfid/rfid_tag.dart';

class RfidController extends ChangeNotifier {
  RfidController({required RfidReader reader}) : _reader = reader;

  final RfidReader _reader;

  final Map<String, RfidTag> _tagsByEpc = <String, RfidTag>{};
  final List<String> _logs = <String>[];

  StreamSubscription<RfidTag>? _tagSubscription;
  StreamSubscription<String>? _statusSubscription;
  StreamSubscription<bool>? _triggerSubscription;

  bool _isBusy = false;
  bool _isInventoryRunning = false;
  bool _isTriggerPressed = false;
  String? _errorMessage;
  RfidPlatformInfo? _platformInfo;

  bool get isBusy => _isBusy;
  bool get isInventoryRunning => _isInventoryRunning;
  String? get errorMessage => _errorMessage;
  RfidPlatformInfo? get platformInfo => _platformInfo;

  List<RfidTag> get tags {
    final list = _tagsByEpc.values.toList();
    list.sort((a, b) => b.timestamp.compareTo(a.timestamp));
    return list;
  }

  int get totalReads =>
      _tagsByEpc.values.fold<int>(0, (acc, tag) => acc + tag.count);

  List<String> get logs => List<String>.unmodifiable(_logs);

  Future<void> setup() async {
    _setBusy(true);
    _errorMessage = null;
    notifyListeners();

    try {
      _platformInfo = await _reader.getPlatformInfo();
      _bindStreamsIfNeeded();
      await _reader.initialize();
      _appendLog(
        'Reader conectado: ${_platformInfo?.readerName ?? 'desconhecido'}',
      );
    } catch (error) {
      _errorMessage = error.toString();
      _appendLog('Erro de setup: $error');
    } finally {
      _setBusy(false);
      notifyListeners();
    }
  }

  Future<void> start() async {
    if (_isBusy) {
      return;
    }

    _setBusy(true);
    _errorMessage = null;
    notifyListeners();

    try {
      await _reader.startInventory();
      _isInventoryRunning = true;
      _appendLog('Inventario iniciado');
    } catch (error) {
      _errorMessage = error.toString();
      _appendLog('Erro ao iniciar: $error');
    } finally {
      _setBusy(false);
    }

    notifyListeners();
  }

  Future<void> stop() async {
    if (_isBusy) {
      return;
    }

    _setBusy(true);
    _errorMessage = null;
    notifyListeners();

    try {
      await _reader.stopInventory();
      _isInventoryRunning = false;
      _appendLog('Inventario parado');
    } catch (error) {
      _errorMessage = error.toString();
      _appendLog('Erro ao parar: $error');
    } finally {
      _setBusy(false);
    }

    notifyListeners();
  }

  Future<void> toggleInventory() {
    if (_isInventoryRunning) {
      return stop();
    }
    return start();
  }

  void clearTags() {
    _tagsByEpc.clear();
    _appendLog('Lista de tags limpa');
    notifyListeners();
  }

  void clearLogs() {
    _logs.clear();
    notifyListeners();
  }

  void _bindStreamsIfNeeded() {
    _tagSubscription ??= _reader.tags.listen((tag) {
      final existing = _tagsByEpc[tag.epc];
      if (existing == null) {
        _tagsByEpc[tag.epc] = tag;
      } else {
        final updatedCount =
            tag.count > 1
                ? (tag.count > existing.count ? tag.count : existing.count)
                : (existing.count + 1);

        _tagsByEpc[tag.epc] = existing.copyWith(
          count: updatedCount,
          rssi: tag.rssi ?? existing.rssi,
          timestamp: tag.timestamp,
        );
      }
      notifyListeners();
    });

    _statusSubscription ??= _reader.status.listen((message) {
      _appendLog(message);
      notifyListeners();
    });

    _triggerSubscription ??= _reader.trigger.listen((pressed) {
      unawaited(_handleTriggerState(pressed));
    });
  }

  Future<void> _handleTriggerState(bool pressed) async {
    if (pressed) {
      if (_isTriggerPressed) {
        return;
      }
      _isTriggerPressed = true;
      if (!_isInventoryRunning) {
        await start();
      }
      return;
    }

    if (!_isTriggerPressed) {
      return;
    }
    _isTriggerPressed = false;
    if (_isInventoryRunning) {
      await stop();
    }
  }

  void _appendLog(String message) {
    final timestamp = DateTime.now().toIso8601String();
    _logs.insert(0, '[$timestamp] $message');
    if (_logs.length > 50) {
      _logs.removeRange(50, _logs.length);
    }
  }

  void _setBusy(bool value) {
    _isBusy = value;
  }

  @override
  void dispose() {
    unawaited(_tagSubscription?.cancel());
    unawaited(_statusSubscription?.cancel());
    unawaited(_triggerSubscription?.cancel());
    unawaited(_reader.dispose());
    super.dispose();
  }
}
