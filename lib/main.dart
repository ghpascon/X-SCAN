import 'package:flutter/material.dart';
import 'package:x_scan/core/theme/app_theme.dart';
import 'package:x_scan/screens/app_settings.dart';
import 'package:x_scan/screens/barcode_test.dart';
import 'package:x_scan/screens/gps.dart';
import 'package:x_scan/screens/home.dart';
import 'package:x_scan/screens/info.dart';
import 'package:x_scan/screens/rfid.dart';
import 'package:x_scan/screens/rfid_config.dart';
import 'package:x_scan/screens/sync.dart';
import 'package:x_scan/screens/sync_queue.dart';
import 'package:x_scan/services/reader_prefs.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await ReaderPrefs.load();
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
        '/gps': (context) => const GpsScreen(),
        '/info': (context) => const InfoScreen(),
        '/barcode-test': (context) => const BarcodeTestScreen(),
        '/app-settings': (context) => const AppSettingsScreen(),
        '/rfid': (context) => const RfidScreen(),
        '/rfid-config': (context) => const RfidConfigScreen(),
        '/sync': (context) => const SyncScreen(),
        '/sync-queue': (context) => const SyncQueueScreen(),
      },
      themeMode: ThemeMode.light,
      theme: AppTheme.light,
    );
  }
}
