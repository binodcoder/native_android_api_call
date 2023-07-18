import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  //Method channel send message to android and android send response to flutter

  static const batteryChannel = MethodChannel('com.example.native_android_api_call');
  String batteryLevel = 'Waiting ...';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Text(
          batteryLevel,
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: getBatteryLevel,
        tooltip: 'check battery',
        child: const Icon(Icons.battery_0_bar),
      ),
    );
  }

  Future getBatteryLevel() async {
    final arguments = {'name': 'Webbook'};
    final int newBatteryLevel = await batteryChannel.invokeMethod('getBatteryLevel', arguments);

    setState(() {
      batteryLevel = '$newBatteryLevel';
    });
  }
}
