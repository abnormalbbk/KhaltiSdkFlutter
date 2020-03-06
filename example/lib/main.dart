import 'package:flutter/material.dart';
import 'package:flutterplugin/flutterplugin.dart';

void main() => runApp(Home());

class Home extends StatelessWidget {
  List<PaymentPreference> _paymentPreferences = [
    PaymentPreference.khalti,
    PaymentPreference.ebanking
  ];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Khalti Sdk Flutter Demo'),
        ),
        body: Center(
          child: MaterialButton(
            height: 50,
            color: Theme.of(context).accentColor,
            child: Text(
              'Pay Rs. 100',
              style: TextStyle(
                color: Colors.white,
              ),
            ),
            onPressed: () {
              Flutterplugin.showKhaltiCheckout(
                Config(
                    publicKey: null,
                    amountInPaisa: 1000,
                    paymentPreferences: _paymentPreferences,
                    productName: "This is name",
                    productID: "This is id",
                    mobileNo: "9841877861"),
              );
            },
          ),
        ),
      ),
    );
  }
}
