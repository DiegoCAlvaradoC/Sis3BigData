import 'package:flutter/material.dart';
import 'package:big_data/Components/export_components.dart';
import 'package:big_data/theme/my_theme.dart';
import 'package:http/http.dart' as http;

class ShowResult extends StatelessWidget {
  const ShowResult({super.key});

  Future<String> fetchImageUrl() async {
    // Replace with your endpoint URL
    final response = await http.get(Uri.parse('https://your-endpoint.com/image'));

    if (response.statusCode == 200) {
      // Assuming the endpoint returns a JSON with an 'imageUrl' field
      // You can modify this according to your API's response structure
      final imageUrl = response.body; // Assuming the response body is the image URL directly
      return imageUrl;
    } else {
      throw Exception('Failed to load image');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const MyAppBar(
        color: Colors.white,
        fontSize: 25,
        title: "resultado",
      ),
      body: Column(
        children: <Widget>[
          FutureBuilder<String>(
            future: fetchImageUrl(),
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const Center(child: CircularProgressIndicator());
              } else if (snapshot.hasError) {
                return Center(child: Text('Error: ${snapshot.error}'));
              } else if (snapshot.hasData) {
                return Image.network(snapshot.data!);
              } else {
                return const Center(child: Text('No image available'));
              }
            },
          ),
        ],
      ),
    );
  }
}
