import 'dart:io';
import 'package:flutter/material.dart';
import 'package:big_data/Components/export_components.dart';
import 'package:big_data/theme/my_theme.dart';
import 'package:file_picker/file_picker.dart';
import 'package:http/http.dart' as http;

class SelectDoc extends StatelessWidget {
  const SelectDoc({Key? key}) : super(key: key);

  Future<void> uploadFile(File file) async {
    var request = http.MultipartRequest(
      'POST',
      Uri.parse('http://10.0.2.2:8080/upload'), // Replace with your server endpoint
    );

    request.files.add(await http.MultipartFile.fromPath(
      'file', // The name should match the parameter name expected by your server
      file.path,
    ));

    try {
      var response = await request.send();

      if (response.statusCode == 200) {
        print('File uploaded successfully.');
      } else {
        print('File upload failed with status: ${response.statusCode}');
      }
    } catch (e) {
      print('File upload error: $e');
    }
  }

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
              onPressed: () async {
                // Select Document from device
                FilePickerResult? result = await FilePicker.platform.pickFiles();

                if (result != null) {
                  // Get the selected file
                  File file = File(result.files.single.path!);

                  // Upload the file


                  // Show alert dialog
                  showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return AlertDialog(
                        title: const Text('File Selected'),
                        content: Text('Selected file: ${result.files.single.name}'),
                        actions: <Widget>[
                          Row(
                            children: [
                              MyButton(
                                text: "OK",
                                fontSize: 20,
                                width: 2 / 5 * MediaQuery.of(context).size.width,
                                height: MediaQuery.of(context).size.height / 11,
                                color: AppTheme.secondary,
                                textColor: Colors.white,
                                onPressed: () async {


                                  await uploadFile(file);
                                  Navigator.pop(context);
                                  Navigator.pushNamed(context, '/decision_tree', arguments: file.path);
                                },
                              ),
                              MyButton(
                                text: "Cancel",
                                fontSize: 20,
                                width: 2 / 5 * MediaQuery.of(context).size.width,
                                height: MediaQuery.of(context).size.height / 11,
                                color: AppTheme.alert,
                                textColor: Colors.white,
                                onPressed: () {
                                  Navigator.pop(context);
                                },
                              ),
                            ],
                          )
                        ],
                      );
                    },
                  );
                  print('Selected file: ${file.path}');
                } else {
                  // User canceled the file picker
                  print('No file selected');
                }
              },
            ),
          ],
        ),
      ),
    );
  }
}
