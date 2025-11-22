package com.example.cowa_app.data.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cowa_app.utils.RouterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.cowa_app.data.repository.UserDataRepository
import com.example.cowa_app.utils.DeviceUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    val routerManager: RouterManager,
    val userDataRepository: UserDataRepository,
) : ViewModel() {
    // 其他全局状态...
    fun checkVersion(context: Context) {
        try {
            viewModelScope.launch {
                DeviceUtils.checkVersion(context) { call ->
                    call()
                }
            }
        }  catch (e: Exception) {
            Log.e("AppViewModal", e.message.toString())
        }
    }

    suspend fun checkLoginStatus(): Boolean {
        return try {
            Log.d("AppViewModel", "checkLoginStatus")
            return userDataRepository.isUserLoggedIn()
        } catch (e: Exception) {
            false
        }
    }
    private var timerJob: Job? = null

    fun startTimer() {
        Log.d("Timer", "start launch")
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(3000)
                val time = "3秒到了aaaa - ${System.currentTimeMillis()}"
                Log.d("Timer", time)
            }
        }
    }
}