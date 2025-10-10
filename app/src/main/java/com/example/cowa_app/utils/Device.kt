package com.example.cowa_app.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import java.io.BufferedReader
import java.io.InputStreamReader
import android.app.devicemanager.DeviceManager

object DeviceUtils {

    val deviceManager: DeviceManager = DeviceManager.getInstance()

    /**
     *  获取 Android ID
     *  注意：在不同设备、不同应用之间可能不同，恢复出厂设置会重置
     */
    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {

        var id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""

         try {
            val process = Runtime.getRuntime().exec("getprop persist.netd.stable_secret")
             val reader = BufferedReader(InputStreamReader(process.inputStream))
             val model = reader.readLine()
             id = "$id-$model"
             Log.d("Device", "exec persist.netd.stable_secret=$model")
        } catch (e: Exception) {
             Log.e("Device", e.toString())
             e.printStackTrace()
        }

        return id
    }

    /**
     *  获取设备名称
     *  注意：需要BLUETOOTH权限，通常可以获取到
     */
//    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @SuppressLint("HardwareIds")
    fun getDeviceName(context: Context): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                // Android 7.1.1+ 使用 Settings.Global.DEVICE_NAME
                Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)
                    ?: getFallbackDeviceName()
            } else {
                // 旧版本使用 Bluetooth 设备名
//                getBluetoothDeviceName() ?: getFallbackDeviceName()
                getFallbackDeviceName()
            }
        } catch (e: Exception) {
            getFallbackDeviceName()
        }
    }

    /**
     * 通过蓝牙适配器获取设备名（不需要蓝牙权限也可以获取）
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @SuppressLint("HardwareIds")
    private fun getBluetoothDeviceName(): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                BluetoothAdapter.getDefaultAdapter()?.name
            } else {
                @Suppress("DEPRECATION")
                BluetoothAdapter.getDefaultAdapter()?.name
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取备用设备名称（当无法获取用户设置的设备名时使用）
     */
    private fun getFallbackDeviceName(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

    /**
     *  获取设备IMEI
     *  只有对讲机才可以利用这个sdk获取到imei
     */
    fun getImei(): String {
        try {
            return deviceManager.getImei()
        } catch (e: Exception) {
            return ""
        }
    }

}