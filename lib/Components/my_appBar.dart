import 'package:flutter/material.dart';

class MyAppBar extends StatelessWidget implements PreferredSizeWidget {
  final String? title;
  final double? fontSize;
  final Color? color;

  const MyAppBar({Key? key, this.title, this.fontSize, this.color})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AppBar(
      title: Center(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          mainAxisSize: MainAxisSize.max,
          children: [
            Text(
              title!,
              style: TextStyle(
                fontSize: fontSize,
                color: color,
              ),
            ),
          ],
        ),
      ),
      flexibleSpace: const Image(
        image: AssetImage("assets/background.jpeg"),
        fit: BoxFit.cover,
      ),
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}
