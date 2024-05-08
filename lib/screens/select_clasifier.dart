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
        title: "Select Classifier",
        fontSize: 25,
        color: Colors.white,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          mainAxisSize: MainAxisSize.max,
          children: [
            Center(
                child: MyText(
                    text: "Seleccionar Clasificador para $filePath",
                    fontSize: 15,
                    color: AppTheme.primary,
                    bold: true)),
            SizedBox(height: 20),
            MyDropdown(),
            MyButton(
              text: "Create Decision Tree",
              fontSize: 20,
              width: 3 / 5 * MediaQuery.of(context).size.width,
              height: MediaQuery.of(context).size.height / 11,
              color: AppTheme.secondary,
              textColor: Colors.white,
              onPressed: () {},
            ),
            SizedBox(height: 20),
            MyButton(
              text: "Create Random Forest",
              fontSize: 20,
              width: 3 / 5 * MediaQuery.of(context).size.width,
              height: MediaQuery.of(context).size.height / 11,
              color: AppTheme.secondary,
              textColor: Colors.white,
              onPressed: () {},
            ),
            SizedBox(height: 20),
            MyButton(
              text: "Create Multilayer Perceptron",
              fontSize: 20,
              width: 3 / 5 * MediaQuery.of(context).size.width,
              height: MediaQuery.of(context).size.height / 11,
              color: AppTheme.secondary,
              textColor: Colors.white,
              onPressed: () {},
            ),
          ],
        ),
      ),
    );
  }
}
