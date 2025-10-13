package com.example.cowa_app.utils

import AppEvent
import AppEventBus
import android.Manifest
import android.annotation.SuppressLint
import android.app.devicemanager.DeviceManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object DeviceUtils {

    val deviceManager: DeviceManager = DeviceManager.getInstance()

    /**
     *  获取 Android ID
     *  注意：在不同设备、不同应用之间可能不同，恢复出厂设置会重置
     */
    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {

        var id =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""

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
        return try {
            deviceManager.imei
        } catch (e: Exception) {
            ""
        }
    }

    /**
     *  前往系统设置页面
     */
    fun goToSetting(context: Context) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(intent)
    }

    /**
     *  清除缓存
     */
    fun cleanCache(context: Context) {
        try {
            clearInternalCache(context)
            clearExternalCache(context)
            clearCustomDirectories(context)
            AppEventBus.emitFromAnywhere(AppEvent.ShowToast("清除成功"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  清除内部缓存
     */
    fun clearInternalCache(context: Context) {
        try {
            val cacheDir = context.cacheDir
            if (cacheDir.exists() && cacheDir.isDirectory) {
                cacheDir.deleteRecursively()
                cacheDir.mkdirs()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除外部缓存
     */
    fun clearExternalCache(context: Context) {
        try {
            val externalCacheDir = context.externalCacheDir
            externalCacheDir?.let { dir ->
                if (dir.exists() && dir.isDirectory) {
                    dir.deleteRecursively()
                    dir.mkdirs()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除自定义目录
     */
    private fun clearCustomDirectories(context: Context) {
        try {
            // 示例：清除下载目录
            val downloadDir = File(context.getExternalFilesDir(null), "downloads")
            clearDirectory(downloadDir)

            // 示例：清除日志目录
            val logDir = File(context.filesDir, "logs")
            clearDirectory(logDir)

            // 示例：清除图片缓存目录
            val imageCacheDir = File(context.externalCacheDir, "images")
            clearDirectory(imageCacheDir)

            // 添加你的其他自定义目录...
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除指定目录下的所有文件
     */
    fun clearDirectory(directory: File?): Boolean {
        return try {
            if (directory == null || !directory.exists()) {
                return true
            }

            if (directory.isDirectory) {
                val files = directory.listFiles()
                files?.forEach { file ->
                    if (file.isDirectory) {
                        clearDirectory(file)
                    } else {
                        file.delete()
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     *  设置cpu唤醒锁，防止休眠
     */

}