package comflutterbeginner.bibek.flutterplugin

import android.content.Context
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


/** FlutterpluginPlugin */
public class FlutterpluginPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var appContext: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val channel = MethodChannel(flutterPluginBinding.binaryMessenger, "khalti")
        channel.setMethodCallHandler(this);
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "makePaymentViaKhalti" -> makePaymentViaKhalti(call.arguments as HashMap<String, Any>, result);
        }
    }

    private fun makePaymentViaKhalti(data: HashMap<String, Any>, result: Result) {
        lateinit var builder: Config.Builder;
        with(data) {
            builder = Config.Builder(of("public_key"), of("product_id"), of("product_name"), of("amount"), object : OnCheckOutListener {
                override fun onSuccess(data: MutableMap<String, Any>) {
                    result.success(data)
                }

                override fun onError(action: String, errorMap: MutableMap<String, String>) {
                    result.error("", errorMap.toString(), errorMap)
                }

            })
        }

        val selectedPaymentPreference: ArrayList<PaymentPreference> = ArrayList();

        if (data.containsKey("additional_data") && data["additional_data"] != null) {
            builder.additionalData(data["additional_data"] as MutableMap<String, Any>?)
        }

        if (data.containsKey("product_url") && data["product_url"] != null) {
            builder.productUrl(data.of("product_url"))
        }

        if (data.containsKey("mobile") && data["mobile"] != null) {
            builder.mobile(data.of( "mobile"))
        }

        if (data.containsKey("payment_preferences") && data["payment_preferences"] != null) {
            val paymentPreferences = data["payment_preferences"] as List<String>
            if (EmptyUtil.isNotNull(paymentPreferences) && paymentPreferences.isNotEmpty()) {
                for (p in paymentPreferences) {
                    print("THere")
                    selectedPaymentPreference.add(from(p))
                }
            }
        }
        builder.paymentPreferences(selectedPaymentPreference)
        val config = builder.build()

        if (::appContext.isInitialized) {
            KhaltiCheckOut(appContext, config).show()
        }
    }

    private fun <T> HashMap<String, Any>.of(key: String): T {
        return when(val value = this[key]){
            is Int ->  value.toLong() as T
            else -> value as T
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }

    companion object {
        fun from(search: String): PaymentPreference = requireNotNull(PaymentPreference.values().find { it.value == search }) { "No Payment Preference with value $search" }
    }

    override fun onDetachedFromActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        appContext = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
