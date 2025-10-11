package com.example.cowa_app.data.model

import androidx.lifecycle.ViewModel
import com.example.cowa_app.data.repository.AppInitializer
import com.example.cowa_app.utils.RouterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val routerManager: RouterManager,
    val appInitializer: AppInitializer
) : ViewModel() {
    // 其他全局状态...

    suspend fun checkLoginStatus(): Boolean {
        return try {
            appInitializer.checkLoginStatus()
        } catch (e: Exception) {
            false
        }
    }
}