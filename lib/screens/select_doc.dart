import 'package:flutter/material.dart';
import 'package:big_data/Components/export_components.dart';
import 'package:big_data/theme/my_theme.dart';
import 'package:file_picker/file_picker.dart';

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
              onPressed: () async {
                //Select Document from device
                // ...

                FilePickerResult? result =
                    await FilePicker.platform.pickFiles();

                if (result != null) {
                  // Get the file path
                  String? filePath = result.files.single.path;

                  // Do something with the selected file
                  if (filePath != null) {
                    // Your code here to handle the selected file
                    // For example, you can display the file name or upload it to a server
                    // alert to create a decision treee
                    // Navigator.pushNamed(context, '/decision_tree', arguments: filePath);
                    // ignore: use_build_context_synchronously
                    showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return AlertDialog(
                            title: const Text('File Selected'),
                            content: Text('Selected file: $filePath'),
                            actions: <Widget>[
                              MyButton(
                                text: "OK",
                                fontSize: 20,
                                width:
                                    3 / 5 * MediaQuery.of(context).size.width,
                                height: MediaQuery.of(context).size.height / 11,
                                color: AppTheme.secondary,
                                textColor: Colors.white,
                                onPressed: () {
                                  Navigator.pop(context);
                                  Navigator.pushNamed(context, '/decision_tree',
                                      arguments: filePath);
                                },
                              )
                            ],
                          );
                        });
                    print('Selected file: $filePath');
                  }
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
