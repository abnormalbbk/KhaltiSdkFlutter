import 'dart:async';
import 'package:meta/meta.dart';
import 'package:flutter/services.dart';

class Flutterplugin {
  static const MethodChannel _channel = const MethodChannel('flutterplugin');

  static Future showFlutterCheckout(Config config) async {
    return await _channel.invokeMapMethod(
        'makePaymentViaKhalti', config.toMap());
  }
}

class Config {
  final String publicKey;
  final String productID;
  final String productName;
  final int amountInPaisa;
  final List<PaymentPreference> paymentPreferences;
  final Map<String, dynamic> additionalData;
  final String productUrl;
  final String mobileNo;

  Config({
    @required this.publicKey,
    @required this.productID,
    @required this.productName,
    @required this.amountInPaisa,
    this.paymentPreferences,
    this.additionalData,
    this.productUrl,
    this.mobileNo,
  });

  Map<String, dynamic> toMap() {
    return {
      'public_key': publicKey,
      'product_id': productID,
      'product_name': productName,
      'amount': amountInPaisa,
      'payment_preferences':
          paymentPreferences.map((a) => a.toString().split('.').last).toList(),
      'additional_data': additionalData,
      'product_url': productUrl,
      'mobile': mobileNo,
    };
  }
}

enum PaymentPreference { khalti, sct, ebanking, mobile_banking, connect_ips }
