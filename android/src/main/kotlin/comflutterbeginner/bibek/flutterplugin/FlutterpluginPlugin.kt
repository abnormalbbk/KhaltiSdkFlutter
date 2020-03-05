package comflutterbeginner.bibek.flutterplugin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.KhaltiCheckOut
import com.khalti.checkout.helper.OnCheckOutListener
import com.khalti.checkout.helper.PaymentPreference
import com.khalti.utils.EmptyUtil
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlin.collections.HashMap


/** FlutterpluginPlugin */
public class FlutterpluginPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var appContext: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.i("OnAttachToEngine", "OnAttachToEngine")
        val channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutterplugin")
        channel.setMethodCallHandler(this);
        appContext = flutterPluginBinding.applicationContext
        Log.i("OnAttachToEngine", "OnAttachToEngine2")
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {

        Log.i("onMethodCall", "onMethodCall")
        when (call.method) {
            "makePaymentViaKhalti" -> makePaymentViaKhalti(call.arguments as HashMap<String, Any>, result);
        }
    }

    @SuppressLint("LongLogTag")
    private fun makePaymentViaKhalti(data: HashMap<String, Any>, result: Result) {
        Log.i("makePaymentViaKhalti", "makePaymentViaKhalti")
        val builder: Config.Builder = Config.Builder(
                castToString(data, "public_key"),
                castToString(data, "product_id"),
                castToString(data, "product_name"),
                (data["amount"] as Int).toLong(), object : OnCheckOutListener {
            override fun onSuccess(@NonNull data: Map<String, Any>) {
                Log.i("success", data.toString())
                result.success(data)
            }

            override fun onError(action: String, errorMap: MutableMap<String, String>) {
                Log.i(action, errorMap.toString())
                result.error("", errorMap.toString(), errorMap.toString())
            }
        })

        val selectedPaymentPreference: ArrayList<PaymentPreference> = ArrayList();

        if (data.containsKey("additional_data") && data["additional_data"] != null) {
            builder.additionalData(data["additional_data"] as MutableMap<String, Any>?)
        }

        if (data.containsKey("product_url") && data["product_url"] != null) {
            builder.productUrl(castToString(data, "product_url"))
        }

        if (data.containsKey("mobile") && data["mobile"] != null) {
            builder.mobile(castToString(data, "mobile"))
        }

        if (data.containsKey("payment_preferences") && data["payment_preferences"] != null) {
            val paymentPreferences: List<String> = data["payment_preferences"] as List<String>
            if (EmptyUtil.isNotNull(paymentPreferences) && paymentPreferences.isNotEmpty()) {
                for (p in paymentPreferences) {
                    Log.i("Payment Preference", p);
                    Log.i("Payment Preference Get from Value", from(p).toString());
                    selectedPaymentPreference.add(from(p))
                }
            }
        }
        builder.paymentPreferences(selectedPaymentPreference)
        val config = builder.build()

        if (::appContext.isInitialized) {
            KhaltiCheckOut(appContext, config).show();
        }
    }

    private fun castToString(data: HashMap<String, Any>, key: Any): String {
        return data[key] as String;
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        Log.i("onAttachedToActivity", "onAttachedToActivity")
        this.appContext = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    companion object {
        fun from(search: String): PaymentPreference = requireNotNull(PaymentPreference.values().find { it.value == search }) { "No Payment Preference with value $search" }
    }


}
