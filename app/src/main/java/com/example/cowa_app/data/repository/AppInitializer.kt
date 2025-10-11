package com.example.cowa_app.data.repository

import android.content.Context
import android.util.Log
import com.example.cowa_app.utils.RouterManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializer @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    private val routerManager: RouterManager,
    @ApplicationContext private val context: Context
) {

    suspend fun checkLoginStatus(): Boolean {
            return try {
                val userPrefs = userPreferencesManager.userPreferencesFlow.first()
                userPrefs.isLoggedIn && userPrefs.token.isNotEmpty()
            } catch (e: Exception) {
                Log.e("MyApplication", "检查登录状态失败: ${e.message}", e)
                false
            }
    }

    fun checkLoginStatusAndInitialize() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userPrefs = userPreferencesManager.userPreferencesFlow.first()
                Log.d(
                    "MyApplication",
                    "检查登录状态: isLoggedIn = ${userPrefs.isLoggedIn}; 用户ID: ${userPrefs.userId}; Token: ${if (userPrefs.token.isNotEmpty()) "已设置" else "未设置"}"
                )
                if (userPrefs.isLoggedIn && userPrefs.token.isNotEmpty()) {
                    // initialize app
                    initializeAfterLogin()
                    routerManager.navigateToHome()
                } else {
                    // 未登录
                    Log.d("MyApplication", "未登录..")
                    routerManager.navigateToLogin()
                }
            } catch (e: Exception) {
                Log.e("MyApplication", "检查登录状态失败: ${e.message}", e)
            }
        }
    }

    suspend fun initializeAfterLogin() { // suspend 可暂停执行，不会阻塞线程
        Log.d("AppInitializer", "开始初始化应用功能...")

        // 1. 连接 WebSocket
        initializeWebSocket()

        // 2. 开启流媒体功能
        initializeMediaStream()

        // 3. 设置键盘监听
        initializeKeyListener()

        // 4. 其他初始化...
        initializeOtherServices()

        Log.d("AppInitializer", "应用功能初始化完成")
    }

    private suspend fun initializeWebSocket() {
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
}