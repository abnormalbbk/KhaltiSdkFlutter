import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
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
      home: Container(
        child: Center(
          child: RaisedButton(
            child: Text('Pay 100'),
            onPressed: () {
              Flutterplugin.showFlutterCheckout(
                Config(
                    publicKey:
                        "test_public_key_3e980bcab8034736bd150f18f7789a87",
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
