import 'package:flutter/material.dart';
import 'package:x_scan/widgets/app_header.dart';

class AppPageScaffold extends StatelessWidget {
  const AppPageScaffold({
    required this.body,
    super.key,
  });

  final Widget body;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const AppHeader(),
      body: body,
    );
  }
}
