import 'package:flutter/material.dart';
import 'package:x_scan/core/theme/app_theme.dart';
import 'package:x_scan/screens/app_settings.dart';
import 'package:x_scan/screens/barcode_test.dart';
import 'package:x_scan/screens/configuration.dart';
import 'package:x_scan/screens/gps.dart';
import 'package:x_scan/screens/home.dart';
import 'package:x_scan/screens/info.dart';
import 'package:x_scan/screens/rfid.dart';
import 'package:x_scan/screens/rfid_config.dart';
import 'package:x_scan/screens/sync.dart';
import 'package:x_scan/screens/sync_queue.dart';
import 'package:x_scan/screens/utils.dart';
import 'package:x_scan/screens/select_reader.dart';
import 'package:x_scan/services/reader_prefs.dart';
import 'package:x_scan/core/rfid/rfid_manager.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await ReaderPrefs.load();
  await RfidManager.loadType(); // Carrega o tipo salvo
  runApp(const XScanApp());
}

class XScanApp extends StatelessWidget {
  const XScanApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      initialRoute: '/',
      routes: {
        '/': (context) => const HomeScreen(),
        '/rfid': (context) => const RfidScreen(),
        '/sync': (context) => const SyncScreen(),
        '/sync-queue': (context) => const SyncQueueScreen(),
        '/utils': (context) => const UtilsScreen(),
        '/configuration': (context) => const ConfigurationScreen(),
        '/info': (context) => const InfoScreen(),
        '/gps': (context) => const GpsScreen(),
        '/barcode-test': (context) => const BarcodeTestScreen(),
        '/rfid-config': (context) => const RfidConfigScreen(),
        '/app-settings': (context) => const AppSettingsScreen(),
        '/select-reader': (context) => SelectReaderScreen(),
      },
      themeMode: ThemeMode.light,
      theme: AppTheme.light,
    );
  }
}
