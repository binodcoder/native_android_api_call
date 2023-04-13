package com.example.native_android_api_call
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val BATTERY_CHANNEL="com.example.native_android_api_call"
    private lateinit var channel:MethodChannel

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        channel=MethodChannel(flutterEngine.dartExecutor.binaryMessenger, BATTERY_CHANNEL)

        /// Receive data from Flutter
        channel.setMethodCallHandler{call, result->
            if(call.method=="getBatteryLevel"){
                val arguments= call.arguments() as Map<String, String>?
                val name= arguments?.get("name")
                val batteryLevel=getBatteryLevel()
                result.success(batteryLevel)
            }
        }
    }

    private fun getBatteryLevel(): Int{
        val batteryLevel:Int
        if (VERSION.SDK_INT>=VERSION_CODES.LOLLIPOP){
            val batteryManager=getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel=batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        }else{
            val intent=ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED) )
            batteryLevel=intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)*100
        }
        return batteryLevel
    }
}
