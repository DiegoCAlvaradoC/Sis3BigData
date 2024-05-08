import 'dart:io';

import 'package:flutter/material.dart';
import 'package:big_data/Components/export_components.dart';
import 'package:big_data/theme/my_theme.dart';
import 'package:file_picker/file_picker.dart';

class SelectClasifier extends StatelessWidget {
  //get file path from arguments

  const SelectClasifier({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    final arguments = ModalRoute.of(context)?.settings.arguments;
    final String? filePath = arguments as String?;
    print("decision tree");
    print(filePath);

    return Scaffold(
      appBar: MyAppBar(
        title: "Decision Tree",
        fontSize: 25,
        color: Colors.white,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            MyButton(
              text: "Create Decision Tree",
              fontSize: 20,
              width: 3 / 5 * MediaQuery.of(context).size.width,
              height: MediaQuery.of(context).size.height / 11,
              color: AppTheme.secondary,
              textColor: Colors.white,
            ),
            MyButton(
              text: "Create Random Forest",
              fontSize: 20,
              width: 3 / 5 * MediaQuery.of(context).size.width,
              height: MediaQuery.of(context).size.height / 11,
              color: AppTheme.secondary,
              textColor: Colors.white,
            ),
            MyButton(
              text: "Multilayer Perceptron",
              fontSize: 20,
              width: 3 / 5 * MediaQuery.of(context).size.width,
              height: MediaQuery.of(context).size.height / 11,
              color: AppTheme.secondary,
              textColor: Colors.white,
            ),
          ],
        ),
      ),
    );
  }
}
