package com.example.cowa_app.services

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.cowa_app.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppTrackingService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent?): IBinder? = null

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AppTrackingService", "onStartCommand///")
        when (intent?.action) {
            ACTION_START -> {
                Log.d("AppTrackingService", "Starting foreground service")
                startForegroundService()
            }

            ACTION_STOP -> {
                Log.d("AppTrackingService", "Stopping foreground service")
                stopForegroundService()
            }
//            ACTION_UPDATE -> {
//                updateNotification("正在检查植物状态...")
//            }
            else -> {
                Log.d("AppTrackingService", "No action specified, starting as foreground")
                // 如果没有指定action，也启动为前台服务
                startForegroundService()
            }
        }
//        return super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    fun startForegroundService() {
        try {
            // 创建通知渠道
            createNotificationChannel()

            Log.d("AppTrackingService", "Building notification")
            // 构建通知
            val notification = buildNotification()

            startForeground(NOTIFICATION_ID, notification)

            acquireWakeLock()
            // 开始执行
            startBackgroundWork()
        } catch (e: Exception) {
            Log.e("AppTrackingService", "Error starting foreground service: ${e.message}", e)
        }
    }


    fun createNotificationChannel() {
        // 直接在这里创建通知渠道，确保有Context
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "App Tracking Service",
                android.app.NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Tracks app usage and statistics"
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("AppTrackingService", "Notification channel created")
        }
    }

    fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("App Tracking Service")
            .setContentText("Tracking your app usage...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 确保这个图标存在
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    fun startBackgroundWork() {
        serviceScope.launch {
//            while (true) {
//                delay(30 * 60 * 1000)
//                Log.d("AppTrackingService", "background work runnign....")
//            }
            try {
                initializeWebSocket()
                initializeMediaStream()
                initializeKeyListener()
                initializeOtherServices()
                Log.d("AppTrackingService", "所有服务初始化完成")
            }catch (e: Exception) {
                Log.e("AppTrackingService", "初始化失败: ${e.message}", e)
            }
        }
    }

    fun stopForegroundService() {
        Log.d("AppTrackingService", "Stopping foreground service")
        serviceScope.cancel()
        stopForeground(true)
        stopSelf()
    }

    fun updateNotification(content: String) {
//        val updatedNotification = notificationBuilder?.setContentText(content)?.build()
//
//        updatedNotification?.let {
//            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.notify(ServiceNotifications.NOTIFICATION_ID, it)
//        }
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "WalkieTalkie:CoreService"
        )
        wakeLock?.acquire()
        Log.d("AppTrackingService", "WakeLock 已获取")
    }

    // base
    private fun initializeWebSocket() {
        try {
            Log.d("AppInitializer", "WebSocket 连接成功")

        } catch (e: Exception) {
            Log.e("AppInitializer", "WebSocket 连接失败: ${e.message}")
        }
    }

    private fun initializeMediaStream() {
        try {
            Log.d("AppInitializer", "流媒体功能启动成功")
        } catch (e: Exception) {
            Log.e("AppInitializer", "流媒体功能启动失败: ${e.message}")
        }
    }

    private fun initializeKeyListener() {
        try {
            Log.d("AppInitializer", "键盘监听设置成功")
        } catch (e: Exception) {
            Log.e("AppInitializer", "键盘监听设置失败: ${e.message}")
        }
    }

    private fun initializeOtherServices() {
        // 其他需要初始化的服务
        // 比如：推送服务、位置服务、数据库同步等
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val NOTIFICATION_CHANNEL_ID = "talkie_base_server"
        const val NOTIFICATION_ID = 111
    }
}
