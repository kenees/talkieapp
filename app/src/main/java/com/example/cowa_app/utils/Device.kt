package com.example.cowa_app.utils

import AppEvent
import AppEventBus
import android.Manifest
import android.annotation.SuppressLint
import android.app.devicemanager.DeviceManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.cowa_app.BuildConfig
import com.example.cowa_app.apis.ApiModule
import com.example.cowa_app.data.repository.ToastType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


object DeviceUtils {
    private var checking: Boolean = false

    private var wakeLock: PowerManager.WakeLock? = null

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
            // Android 7.1.1+ 使用 Settings.Global.DEVICE_NAME
            Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)
                ?: getFallbackDeviceName()
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
            BluetoothAdapter.getDefaultAdapter()?.name
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
            AppEventBus.emitScope(AppEvent.ShowToast("清除成功", ToastType.SUCCESS))
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
    fun acquireWakeLock(context: Context) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "WalkieTalkie:CoreService"
        )
        wakeLock?.acquire()
        Log.d("AppTrackingService", "WakeLock 已获取")
    }

    /**
     *  获取当前版本
     */
    fun getAppVersion(context: Context): String {
        try {
            val versionName: String? =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            // 现在versionName变量就包含了当前应用的版本名称，如 "1.0"
            if (versionName != null) return versionName
            return "-,-,-"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            // 处理异常，例如应用包名未找到
            return "-,-,-"
        }
    }

    /**
     *  检测当前版本与服务器版本
     */
    suspend fun checkVersion(context: Context, callback: ((call: () -> Unit) -> Unit)?) {
        if (checking) {
            return
        }
        try {
            Log.d("Device", "start check..")
            this.checking = true
            val version = this.getAppVersion(context)
            var latestVersion: String? = null;
            val response = ApiModule.appService.getLatestVersion()
            this.checking = false
            Log.d("Device", response.toString())
            if (response.status) {
                latestVersion = "${response.message.version.major}.${response.message.version.minor}.${response.message.version.revision}"
            }
            Log.d("Device", "checkVersion: $version / $latestVersion")
            if (latestVersion != null) {
                if (compareVersions(latestVersion, version) > 0) {
                    callback?.invoke() {
                        AppEventBus.emitScope(AppEvent.ShowToast("检测到新版本，正在更新!!!",
                            ToastType.INFO))
                    }
                    this.checking = false;
                    downloadApk(context, response.message.path, latestVersion)
                } else {
                    this.checking = false;
                    callback?.invoke() {
                        AppEventBus.emitScope(AppEvent.ShowToast("暂无需要更新的版本!!!",
                            ToastType.INFO))
                    }
                }
            } else {
                callback?.invoke() {
                    AppEventBus.emitScope(AppEvent.ShowToast("暂无新版本!!!", ToastType.INFO))
                }
            }
        } catch (e: Exception) {
            Log.d("Device Exception", e.message.toString())
            this.checking = false
            callback?.invoke() {
                AppEventBus.emitScope(AppEvent.ShowToast("版本检测失败!!!", ToastType.ERROR))
            }
        }
    }

    /**
     * 下载Apk文件到本地
     */
    suspend fun downloadApk(context: Context, path: String, serverAppVersion: String) {
        try {
            val url = "${ApiModule.getBaseUrl()}/api/v1/mmp/system/version/download";
            val fileName = "talkie_app_latest-armeabi-v7a.apk";

            if (!hasStoragePermission(context)) {
                AppEventBus.emitScope(AppEvent.ShowToast("需要存储权限才可以下载更新", ToastType.WARNING))
                requestStoragePermission(context)
                return
            }

            AppEventBus.emitScope(AppEvent.ShowToast("开始下载新版本...", ToastType.INFO))

            val downloadedFile = downloadFile(context, "$url?file=$path", fileName)

            if (downloadedFile != null && downloadedFile.exists()) {
                Log.d("Device", downloadedFile.path)
                AppEventBus.emitScope(AppEvent.ShowToast("下载完成，准备安装", ToastType.SUCCESS))
                installApk(context, downloadedFile)
            } else {
                AppEventBus.emitScope(AppEvent.ShowToast("下载失败", ToastType.ERROR))
            }
        } catch (e: Exception) {
            Log.e("Device", "下载APK失败: ${e.message}")
            AppEventBus.emitScope(AppEvent.ShowToast("下载失败: ${e.message}", ToastType.ERROR))
        }
    }

    private suspend fun downloadFile(context: Context, url: String, fileName: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val token = NetworkUtils.TokenManager.getToken()

                val client = OkHttpClient.Builder()
                    .build()

                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("authorization", "bearer $token")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("下载失败，HTTP Code: ${response.code}, message: ${response.message}")
                    }

                    val responseBody = response.body
                    if (responseBody == null) {
                        throw IOException("响应体为空")
                    }

                    // 创建下载目录
                    val downloadDir = getDownloadDirectory(context)
                    if (!downloadDir.exists()) {
                        downloadDir.mkdirs()
                    }

                    val file = File(downloadDir, fileName)

                    // 如果文件已存在，先删除
                    if (file.exists()) {
                        file.delete()
                    }

                    responseBody.byteStream().use { inputStream ->
                        FileOutputStream(file).use { outputStream ->
                            val buffer = ByteArray(8192)
                            var bytesRead: Int
                            var totalBytesRead = 0L
                            val contentLength = responseBody.contentLength()

                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                                totalBytesRead += bytesRead

                                if (contentLength > 0) {
                                    val progress = (totalBytesRead * 100 / contentLength).toInt()
                                    Log.d("DownloadFile", "下载进度: $progress% - $totalBytesRead/$contentLength")
                                }
                            }

                            outputStream.flush()
                        }
                    }

                    val fileSize = file.length()
                    val expectedSize = responseBody.contentLength()

                    if (expectedSize > 0 && fileSize != expectedSize) {
                        Log.e("DownloadFile", "文件大小不匹配: 实际=$fileSize, 预期=$expectedSize")
                        file.delete()
                        throw IOException("文件下载不完整")
                    }
                    Log.d("DownloadFile", "文件下载完成: ${file.absolutePath}, 大小: $fileSize bytes")
                    file
                }
            } catch (e: Exception) {
                Log.e("DownloadFile", "下载文件失败: ${e.message}")
                null
            }
        }
    }

    /**
     * 根据文件安装apk
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun installApk(context: Context, apkFile: File) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val apkUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Android 7.0+ 使用 FileProvider
                FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    apkFile
                )
            } else {
                // Android 7.0 以下直接使用 file:// URI
                Uri.fromFile(apkFile)
            }

            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(intent)

        } catch (e: Exception) {
            Log.e("InstallApk", "安装APK失败: ${e.message}")
            AppEventBus.emitScope(AppEvent.ShowToast("安装失败: ${e.message}", ToastType.ERROR))

            // 如果安装失败，使用系统默认安装器
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val apkUri = FileProvider.getUriForFile(
                        context,
                        "${BuildConfig.APPLICATION_ID}.fileprovider",
                        apkFile
                    )
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e2: Exception) {
                Log.e("InstallApk", "备用安装方案也失败: ${e2.message}", e2)
                AppEventBus.emitScope(AppEvent.ShowToast("无法安装应用", ToastType.ERROR))
            }
        }
    }

    /**
     *  获取下载目录
     */
    private fun getDownloadDirectory(context: Context): File {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ 使用应用专属目录
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "apks")
        } else {
            // Android 10 以下使用公共下载目录
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "TalkieApp")
        }.apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    /**
     * 检查存储权限
     */
    private fun hasStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ 检查所有文件访问权限
            Environment.isExternalStorageManager() ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android 10 及以下检查读写权限
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 请求存储权限
     */
    private fun requestStoragePermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ 请求所有文件访问权限
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                context.startActivity(intent)
            }
        } else {
            // Android 10 及以下请求读写权限
            // 这里需要在 Activity 中处理权限请求
//            AppEventBus.emitFromAnywhere(AppEvent.RequestStoragePermission)
        }
    }

    /**
     * 比对2个版本号的差异
     */
    private fun compareVersions(v1: String, v2: String): Int {
        val parts1 = v1.split(".").map { it.toIntOrNull() ?: 0 }
        val parts2 = v2.split(".").map { it.toIntOrNull() ?: 0 }

        if (parts1[0] != parts2[0]) {
            return parts1[0] - parts2[0]
        }
        if (parts1[1] != parts1[1]) {
            return parts1[1] - parts2[1]
        }
       return parts1[2] - parts2[2]
    }
}