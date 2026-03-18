import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:x_scan/core/rfid/rfid_platform_info.dart';
import 'package:x_scan/core/rfid/rfid_reader.dart';
import 'package:x_scan/core/rfid/rfid_tag.dart';

class RfidController extends ChangeNotifier {
  RfidController({required RfidReader reader}) : _reader = reader;

  static final RegExp _hex24Pattern = RegExp(r'^[0-9a-f]{24}$');

  final RfidReader _reader;

  final Map<String, RfidTag> _tagsByEpc = <String, RfidTag>{};
  final List<String> _logs = <String>[];

  StreamSubscription<RfidTag>? _tagSubscription;
  StreamSubscription<String>? _statusSubscription;
  StreamSubscription<bool>? _triggerSubscription;

  bool _isBusy = false;
  bool _isInventoryRunning = false;
  bool _isTriggerPressed = false;
  bool _isReaderConnected = false;
  String? _errorMessage;
  RfidPlatformInfo? _platformInfo;

  /// Limiar de RSSI configurado na tela de configuração do leitor.
  /// null = sem filtro. Aplicado a todos os controladores.
  static int? rssiThreshold;

  /// Controle global de beep por leitura de tag no leitor nativo.
  static bool beepEnabled = true;

  /// Prefixos de EPC aceitos. Vazio = aceita qualquer EPC.
  static List<String> epcPrefixes = <String>[];

  bool get isBusy => _isBusy;
  bool get isInventoryRunning => _isInventoryRunning;
  bool get isReaderConnected => _isReaderConnected;
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
      _isReaderConnected = true;
      _appendLog(
        'Reader conectado: ${_platformInfo?.readerName ?? 'desconhecido'}',
      );
    } catch (error) {
      _errorMessage = error.toString();
      _isReaderConnected = false;
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

  Future<void> clearTags() async {
    if (_isBusy) {
      return;
    }

    _setBusy(true);
    _errorMessage = null;
    _tagsByEpc.clear();
    notifyListeners();

    try {
      await _reader.clearTagSession();
      _appendLog('Lista de tags limpa');
    } catch (error) {
      _errorMessage = error.toString();
      _appendLog('Erro ao limpar tags: $error');
    } finally {
      _setBusy(false);
      notifyListeners();
    }
  }

  void clearLogs() {
    _logs.clear();
    notifyListeners();
  }

  void _bindStreamsIfNeeded() {
    _tagSubscription ??= _reader.tags.listen((tag) {
      // Filtro de RSSI configurado na tela de configuração do leitor
      final threshold = RfidController.rssiThreshold;
      if (threshold != null && (tag.rssi == null || tag.rssi! < threshold)) {
        return;
      }

      final normalizedEpc = tag.epc.trim().toLowerCase();
      final normalizedTid = tag.tid?.trim().toLowerCase();
      // Regra: EPC e TID devem ser hex com 24 caracteres, senao ignora.
      if (!_isHex24(normalizedEpc) || normalizedTid == null || !_isHex24(normalizedTid)) {
        return;
      }

      final normalizedTag = tag.copyWith(
        epc: normalizedEpc,
        tid: normalizedTid,
      );

      if (!_matchesAnyPrefix(normalizedTag.epc)) {
        return;
      }

      // TID sempre como chave primaria.
      final key = normalizedTid;
      final existing = _tagsByEpc[key];
      if (existing == null) {
        _tagsByEpc[key] = normalizedTag;
      } else {
        final updatedCount =
            normalizedTag.count > 1
                ? (normalizedTag.count > existing.count
                    ? normalizedTag.count
                    : existing.count)
                : (existing.count + 1);

        _tagsByEpc[key] = existing.copyWith(
          count: updatedCount,
          tid: normalizedTid,
          rssi: normalizedTag.rssi ?? existing.rssi,
          timestamp: normalizedTag.timestamp,
        );
      }
      notifyListeners();
    });

    _statusSubscription ??= _reader.status.listen((message) {
      final normalized = message.trim().toLowerCase();
      if (normalized.startsWith('rfid conectado')) {
        _isReaderConnected = true;
      } else if (normalized.startsWith('rfid desconectado')) {
        _isReaderConnected = false;
      }
      _appendLog(message);
      notifyListeners();
    });

    _triggerSubscription ??= _reader.trigger.listen((pressed) {
      unawaited(_handleTriggerState(pressed));
    });
  }

  bool _matchesAnyPrefix(String epc) {
    final filters = RfidController.epcPrefixes;
    if (filters.isEmpty) {
      return true;
    }

    final normalizedEpc = epc.trim().toLowerCase();
    for (final prefix in filters) {
      final normalizedPrefix = prefix.trim().toLowerCase();
      if (normalizedPrefix.isEmpty) {
        continue;
      }
      if (normalizedEpc.startsWith(normalizedPrefix)) {
        return true;
      }
    }
    return false;
  }

  bool _isHex24(String value) {
    return _hex24Pattern.hasMatch(value);
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
