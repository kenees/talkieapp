package com.example.cowa_app.data.model

// ui/counter/CounterViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowa_app.data.repository.CounterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val counterRepository: CounterRepository
) : ViewModel() {

    // 将 Flow 转换为 StateFlow，便于 UI 观察
    val count = counterRepository.count.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun increment() {
        viewModelScope.launch {
            counterRepository.increment()
        }
    }

    fun decrement() {
        viewModelScope.launch {
            counterRepository.decrement()
        }
    }

    fun reset() {
        viewModelScope.launch {
            counterRepository.reset()
        }
    }
}