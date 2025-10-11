package com.example.cowa_app.data.repository

import android.content.Context
import android.util.Log
import com.example.cowa_app.apis.ApiModule
import com.example.cowa_app.apis.auth.LoginRequest
import com.example.cowa_app.apis.auth.LoginResponse
import com.example.cowa_app.utils.DeviceUtils
import com.example.cowa_app.utils.NetworkResult
import com.example.cowa_app.utils.RouterManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val globalCompRepository: GlobalCompRepository,
    private val userPrefsManager: UserPreferencesManager,
    private val routerManager: RouterManager,
) {

    val userPreferences: Flow<UserPreferences> = userPrefsManager.userPreferencesFlow
    // 使用 StateFlow 来保存计数状态，初始值为 0
    private val _loginDisabled = MutableStateFlow(false)

    // 对外暴露只读的 Flow
    val loginDisabled: Flow<Boolean> = _loginDisabled

    fun setLoginDisabled(value: Boolean = false) {
        _loginDisabled.value = value
    }

    suspend fun login(context: Context): NetworkResult<LoginResponse> {
        return try {
            val androidId = DeviceUtils.getAndroidId(context)
            val deviceName = DeviceUtils.getDeviceName(context)
            val imei = DeviceUtils.getImei()

            val response = ApiModule.authService.login(
                LoginRequest(
                    serial_number = imei,
                    device_name = deviceName,
                    android_id = androidId,
                    platform = "环卫云"
                )
            )
            globalCompRepository.hideToast()
            if (response.code == 200) {
                setLoginDisabled(false)
                ApiModule.setToken(response.message.access_token)
                ApiModule.setRefreshToken(response.message.refresh_token)

                val userPrefs = UserPreferences(
                    token = response.message.access_token,
                    refreshToken = response.message.refresh_token,
                    userId = response.message.user_info.id,
                    account = response.message.user_info.account,
                    phone = response.message.user_info.phone,
                    tenant = response.message.user_info.tenant,
                    userName = response.message.user_info.user_name,
                    loginTime = System.currentTimeMillis(),
                    isLoggedIn = true
                )

                userPrefsManager.saveUserPreferences(userPrefs)

                Log.d("HTTP", "save user preferences success")

                routerManager.navigateToHome()

                NetworkResult.Success(response)
            } else {
                setLoginDisabled(false)
                NetworkResult.Error("Login failed")
            }
        } catch (e: Exception) {
            setLoginDisabled(false)
            globalCompRepository.hideToast()
            Log.w("HTTP", "6. Login exception: ${e.message}", e)
            NetworkResult.Error(e.message ?: "Login failed", exception = e)
        }
    }

    suspend fun logout() {
        userPrefsManager.clearUserPreferences()
        routerManager.navigateToLogin()
    }

    suspend fun getCurrentToken(): String? {
        return userPrefsManager.userPreferencesFlow.first().token.takeIf {
            it.isNotEmpty()
        }
    }
}