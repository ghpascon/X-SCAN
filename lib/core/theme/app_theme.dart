import 'package:flutter/material.dart';

class AppTheme {
  static ThemeData get light {
    return ThemeData(
      brightness: Brightness.light,
      scaffoldBackgroundColor: const Color(0xFFF7FAFC),
      colorScheme: ColorScheme.fromSeed(
        seedColor: const Color(0xFF1A4A6A),
        brightness: Brightness.light,
      ),
      useMaterial3: true,
    );
  }
}
