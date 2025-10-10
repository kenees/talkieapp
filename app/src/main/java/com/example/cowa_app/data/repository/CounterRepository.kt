package com.example.cowa_app.data.repository

// data/repository/CounterRepository.kt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CounterRepository @Inject constructor() {

    // 使用 StateFlow 来保存计数状态，初始值为 0
    private val _count = MutableStateFlow(0)

    // 对外暴露只读的 Flow
    val count: Flow<Int> = _count

    // 增加计数
    fun increment() {
        _count.value += 1
    }

    // 减少计数
    fun decrement() {
        _count.value -= 1
    }

    // 重置计数
    fun reset() {
        _count.value = 0
    }

    // 设置特定值
    fun setCount(value: Int) {
        _count.value = value
    }
}