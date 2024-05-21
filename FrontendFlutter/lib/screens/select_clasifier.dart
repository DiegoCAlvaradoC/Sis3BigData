import 'dart:io';
import 'package:big_data/screens/show_result.dart';
import 'package:flutter/material.dart';
import 'package:big_data/Components/export_components.dart';
import 'package:big_data/theme/my_theme.dart';
import 'package:file_picker/file_picker.dart';

import 'j48request.dart';

class SelectClasifier extends StatefulWidget {
  const SelectClasifier({Key? key}) : super(key: key);

  @override
  _SelectClasifierState createState() => _SelectClasifierState();
}

class _SelectClasifierState extends State<SelectClasifier> {
  Widget? _currentWidget;
  String? _filePath;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final arguments = ModalRoute.of(context)?.settings.arguments;
    _filePath = arguments as String?;
    _currentWidget = _buildInitialWidget();
  }

  Widget _buildInitialWidget() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        mainAxisSize: MainAxisSize.max,
        children: [
          Center(
              child: MyText(
                  text: "Seleccionar Clasificador para $_filePath",
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
            onPressed: () {
              setState(() {
                _currentWidget = ShowResult(imageContainer: ShowTree());
              });
            },
          ),
          SizedBox(height: 20),
          MyButton(
            text: "Create Random Forest",
            fontSize: 20,
            width: 3 / 5 * MediaQuery.of(context).size.width,
            height: MediaQuery.of(context).size.height / 11,
            color: AppTheme.secondary,
            textColor: Colors.white,
            onPressed: () {
              setState(() {
                _currentWidget = ShowResult(imageContainer: ShowTree());
              });
            },
          ),
          SizedBox(height: 20),
          MyButton(
            text: "Create Multilayer Perceptron",
            fontSize: 20,
            width: 3 / 5 * MediaQuery.of(context).size.width,
            height: MediaQuery.of(context).size.height / 11,
            color: AppTheme.secondary,
            textColor: Colors.white,
            onPressed: () {
              setState(() {
                _currentWidget = ShowResult(imageContainer: ShowTree());
              });
            },
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: MyAppBar(
        title: "Select Classifier",
        fontSize: 25,
        color: Colors.white,
      ),
      body: Center(
        child: _currentWidget!,
      ),
    );
  }
}
