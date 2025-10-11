package com.example.cowa_app.data.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowa_app.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModal @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    // 将 Flow 转换为 StateFlow，便于 UI 观察
    val loginDisabled = userDataRepository.loginDisabled.stateIn(
        scope = viewModelScope, // 作用域
        started = SharingStarted.WhileSubscribed(5000), // 启动策略
        initialValue = false
    )

    val userPreferences = userDataRepository.userPreferences.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun loginDisabledHandle(value: Boolean) {
        viewModelScope.launch {
            userDataRepository.setLoginDisabled(value)
        }
    }

    fun login(context: Context) {
        viewModelScope.launch {
            userDataRepository.login(context)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userDataRepository.logout()
        }
    }
}