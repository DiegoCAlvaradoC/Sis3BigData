import 'package:flutter/material.dart';
import 'package:big_data/Components/export_components.dart';
import 'package:big_data/theme/my_theme.dart';

class SelectDoc extends StatelessWidget {
  const SelectDoc({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: MyAppBar(
        title: "Select Document",
        fontSize: 25,
        color: Colors.white,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            MyButton(
              text: "Select Document",
              fontSize: 20,
              width: 3 / 5 * MediaQuery.of(context).size.width,
              height: MediaQuery.of(context).size.height / 11,
              color: AppTheme.secondary,
              textColor: Colors.white,
              onPressed: () {
                print("Select Document");
              },
            ),
          ],
        ),
      ),
    );
  }
}
