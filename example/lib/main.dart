import 'package:flutter/material.dart';
import 'package:khalti/khalti_checkout.dart';

void main() => runApp(Home());

class Home extends StatelessWidget {
  GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey();

  final List<String> _paymentPreferences = [
    PaymentPreference.khalti,
    PaymentPreference.e_banking,
    PaymentPreference.connect_ips,
    PaymentPreference.sct,
    PaymentPreference.mobile_banking,
  ];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        primarySwatch: Colors.deepPurple,
      ),
      home: Scaffold(
        key: _scaffoldKey,
        appBar: AppBar(
          title: Text('Khalti Checkour Demo'),
        ),
        body: Center(
          child: RaisedButton(
            color: Colors.deepPurple,
            textColor: Colors.white,
            child: Text('Pay 100'),
            onPressed: _checkout,
          ),
        ),
      ),
    );
  }

  void _checkout() async {
    try {
      dynamic result = await KhaltiCheckOut.show(
        Config(
          publicKey: 'test_public_key_3e980bcab8034736bd150f18f7789a87',
          amountInPaisa: 1000,
          paymentPreferences: _paymentPreferences,
          productName: 'This is name',
          productID: 'This is id',
          mobileNo: '9841877861',
        ),
      );
      _showSnackBar('$result}');
    } on KhaltiCheckOutException catch (e) {
      _showSnackBar(e.message);
    } catch (e) {
      _showSnackBar(e.toString());
    }
  }

  void _showSnackBar(String message) => _scaffoldKey.currentState.showSnackBar(
        SnackBar(
          backgroundColor: Colors.deepPurple,
          duration: Duration(minutes: 10),
          content: Text(message),
        ),
      );
}
