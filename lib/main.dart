import 'package:big_data/screens/decision_tree.dart';
import 'package:big_data/screens/select_doc.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      initialRoute: "/",
      routes: {
        "/": (context) => const SelectDoc(),
        "/decision_tree": (context) => const DecisionTree(),
      },
    );
  }
}
