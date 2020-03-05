package comflutterbeginner.bibek.flutterplugin

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.KhaltiCheckOut
import com.khalti.checkout.helper.OnCheckOutListener
import com.khalti.checkout.helper.PaymentPreference
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*
import kotlin.collections.HashMap


/** FlutterpluginPlugin */
public class FlutterpluginPlugin : FlutterPlugin, MethodCallHandler,ActivityAware {
    lateinit var context: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.i("OnAttachToEngine", "")
        val channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutterplugin")
        channel.setMethodCallHandler(FlutterpluginPlugin());
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
//        if (call.method == "getPlatformVersion") {
//            result.success("Android ${android.os.Build.VERSION.RELEASE}")
//        } else {
//            result.notImplemented()
//        }

        Log.i("OnAttachToEngine", "")
        when (call.method) {
            "makePaymentViaKhalti" -> makePaymentViaKhalti(call.arguments as HashMap<String, Any>, result);
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }

    fun makePaymentViaKhalti(data: HashMap<String, Any>, result: Result) {
        val config: Config = Config.Builder(castToString(data, "public_key"), castToString(data, "product_id"), castToString(data, "product_name"), (castToString(data, "amount")).toLong(), object : OnCheckOutListener {
                    override fun onSuccess(@NonNull data: Map<String, Any>) {
                        Log.i("success", data.toString())
                        result.success(data)
                    }

                    override fun onError(action: String, errorMap: MutableMap<String, String>) {
                        Log.i(action, errorMap.toString())
                        result.error("", errorMap.toString(), errorMap.toString())
                    }
                })
                .paymentPreferences(object : ArrayList<PaymentPreference?>() {
                    init {
                        add(PaymentPreference.KHALTI)
                        add(PaymentPreference.EBANKING)
                        add(PaymentPreference.MOBILE_BANKING)
                        add(PaymentPreference.CONNECT_IPS)
                        add(PaymentPreference.SCT)
                    }
                })
                .build()
        KhaltiCheckOut(context, config).show();
    }

    private fun castToString(data: HashMap<String, Any>, key: Any): String {
        return data[key] as String;
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {

        Log.i("onAttachedToActivity", "")
        context = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }


}
