package com.example.cowa_app.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

enum class ToastType {
    SUCCESS, ERROR, WARNING, INFO, LOADING
}

data class ToastData(
    val visible: Boolean = true,
    val title: String = "",
    val type: ToastType = ToastType.INFO,
    val duration: Int = 3000
)

@Singleton
class GlobalCompRepository @Inject constructor() {

    private val _toastState = MutableStateFlow(ToastData())
    val toastState: Flow<ToastData> = _toastState

    private var autoHideJob: Job? = null


    fun handleToast(
        title: String,
        type: ToastType = ToastType.INFO,
        duration: Int = 3000,
        coroutineScope: CoroutineScope
    ) {
        autoHideJob?.cancel()

        _toastState.value = ToastData(
            visible = true,
            title = title,
            type = type,
            duration = duration,
        )

        autoHideJob = coroutineScope.launch {
            delay(duration.toLong())
            hideToast()
        }
    }

    fun hideToast() {
        autoHideJob?.cancel()
        _toastState.value = ToastData()
    }
}