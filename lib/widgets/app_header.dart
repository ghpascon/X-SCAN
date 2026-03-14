import 'package:flutter/material.dart';

class AppHeader extends StatelessWidget implements PreferredSizeWidget {
  const AppHeader({super.key});

  static const String homeRoute = '/';

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight + 8);

  @override
  Widget build(BuildContext context) {
    return AppBar(
      backgroundColor: Colors.white,
      elevation: 0,
      scrolledUnderElevation: 0,
      toolbarHeight: kToolbarHeight + 8,
      titleSpacing: 20,
      title: Row(
        children: [
          Image.asset(
            'assets/images/smtx.png',
            height: 36,
            fit: BoxFit.contain,
          ),
        ],
      ),
      actions: [
        IconButton(
          tooltip: 'Voltar para inicio',
          icon: const Icon(Icons.home_outlined),
          onPressed: () {
            final navigator = Navigator.maybeOf(context);

            if (navigator == null) {
              return;
            }

            navigator.pushNamedAndRemoveUntil(
              homeRoute,
              (route) => false,
            );
          },
        ),
      ],
      bottom: PreferredSize(
        preferredSize: const Size.fromHeight(1),
        child: Container(
          height: 1,
          color: const Color(0xFFE9EEF2),
        ),
      ),
    );
  }
}
