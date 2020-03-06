import 'dart:async';
import 'package:meta/meta.dart';
import 'package:flutter/services.dart';

class KhaltiCheckOutException {
  final String action;
  final String message;

  KhaltiCheckOutException(this.action, this.message);

  @override
  String toString() => 'Action: $action\nMessage: $message';
}

class KhaltiCheckOut {
  static const MethodChannel _channel = const MethodChannel('khalti');

  static Future show(Config config) async {
    try {
      return await _channel.invokeMapMethod(
        'makePaymentViaKhalti',
        config.toMap(),
      );
    } on PlatformException catch (e) {
      final errorMap = Map<String, dynamic>.from(e.details);
      throw KhaltiCheckOutException(
        errorMap['action'],
        '${errorMap['message'] ?? errorMap['detail']}',
      );
    } catch (e) {
      rethrow;
    }
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
  }) : assert(publicKey != null &&
            productID != null &&
            productName != null &&
            amountInPaisa != null &&
            paymentPreferences != null);

  Map<String, dynamic> toMap() => {
        'public_key': publicKey,
        'product_id': productID,
        'product_name': productName,
        'amount': amountInPaisa,
        'payment_preferences': paymentPreferences
            .map((a) => a.toString().split('.').last)
            .toList(),
        'additional_data': additionalData,
        'product_url': productUrl,
        'mobile': mobileNo,
      };
}

enum PaymentPreference { khalti, sct, ebanking, mobile_banking, connect_ips }
