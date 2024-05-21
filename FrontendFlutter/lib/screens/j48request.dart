import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class ShowTree extends StatefulWidget {
  @override
  _ShowTreeState createState() => _ShowTreeState();
}

class _ShowTreeState extends State<ShowTree> {
  Uint8List? _imageBytes;

  @override
  void initState() {
    super.initState();
    _fetchImage();
  }

  Future<void> _fetchImage() async {
    final url = 'http://10.0.2.2:8080/generate-tree';

    try {
      final response = await http.get(Uri.parse(url));

      if (response.statusCode == 200) {
        setState(() {
          _imageBytes = response.bodyBytes;
        });
      } else {
        print('Request failed with status: ${response.statusCode}');
      }
    } catch (e) {
      print('Request failed with error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Generated Tree'),
      ),
      body: Center(
        child: _imageBytes == null
            ? CircularProgressIndicator()
            : Image.memory(_imageBytes!),
      ),
    );
  }
}

