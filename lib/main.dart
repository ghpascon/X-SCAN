import 'package:flutter/material.dart';
import 'package:x_scan/core/theme/app_theme.dart';
import 'package:x_scan/screens/gps.dart';
import 'package:x_scan/screens/home.dart';
import 'package:x_scan/screens/info.dart';

void main() {
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
			},
			themeMode: ThemeMode.light,
			theme: AppTheme.light,
		);
	}
}
