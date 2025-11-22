package com.example.cowa_app.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowa_app.data.repository.GlobalCompRepository
import com.example.cowa_app.data.repository.ToastData
import com.example.cowa_app.data.repository.ToastType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalCompViewModal @Inject constructor(
    private val globalCompRepository: GlobalCompRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            AppEventBus.events.collect { event ->
                when (event) {
                    is AppEvent.ShowToast -> {
                        when (event.type) {
                            ToastType.INFO -> { info(event.message)}
                            ToastType.ERROR -> { error(event.message)}
                            ToastType.LOADING -> {loading(event.message)}
                            ToastType.SUCCESS -> {success(event.message)}
                            ToastType.WARNING -> {warning(event.message)}
                        }
                    }
                }
            }
        }
    }

    val toastState = globalCompRepository.toastState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ToastData()
    )

    fun info(title: String, duration: Int = 3000) {
        viewModelScope.launch {
            globalCompRepository.handleToast(title, ToastType.INFO, duration, viewModelScope)
        }
    }

    fun loading(title: String, duration: Int = 60000) {
        viewModelScope.launch {
            globalCompRepository.handleToast(title, ToastType.LOADING, duration, viewModelScope)
        }
    }

    fun success(title: String, duration: Int = 2000) {
        viewModelScope.launch {
            globalCompRepository.handleToast(title, ToastType.SUCCESS, duration, viewModelScope)
        }
    }

    fun warning(title: String, duration: Int = 2000) {
        viewModelScope.launch {
            globalCompRepository.handleToast(title, ToastType.WARNING, duration, viewModelScope)
        }
    }

    fun error(title: String, duration: Int = 4000) {
        viewModelScope.launch {
            globalCompRepository.handleToast(title, ToastType.ERROR, duration, viewModelScope)
        }
    }

    fun hide() {
        viewModelScope.launch {
            globalCompRepository.hideToast()
        }
    }
}