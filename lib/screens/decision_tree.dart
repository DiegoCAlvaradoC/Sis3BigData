import 'dart:io';

import 'package:flutter/material.dart';
import 'package:big_data/Components/export_components.dart';
import 'package:big_data/theme/my_theme.dart';
import 'package:file_picker/file_picker.dart';

class DecisionTree extends StatelessWidget {
  //get file path from arguments

  const DecisionTree({Key? key}) : super(key: key);
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
            MyText(
              bold: true,
              text: filePath,
              color: AppTheme.primary,
              fontSize: 25,
              textAlign: TextAlign.center,
            )
          ],
        ),
      ),
    );
  }
}
