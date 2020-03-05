import 'dart:async';

import 'package:flutter/services.dart';

class Flutterplugin {
  static const MethodChannel _channel = const MethodChannel('flutterplugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future showFlutterCheckout(Config config) async {
    return await _channel.invokeMapMethod(
        'makePaymentViaKhalti', config.toMap());
  }
}

class Config {
  final String publicKey;
  final String productID;
  final String productName;
  final String amount;
  final PaymentPreference paymentPreference;

  Config(
      {this.publicKey,
      this.productID,
      this.productName,
      this.amount,
      this.paymentPreference});

  Map<String, dynamic> toMap() {
    return {
      'public_key': publicKey,
      'product_id': productID,
      'product_name': productName,
      'amount': amount,
      'payment_preference': paymentPreference.toString(),
    };
  }
}

enum PaymentPreference { khalti, sct, ebanking, mobile_banking, connect_ips }
