package com.example.cowa_app.data.model

import androidx.lifecycle.ViewModel
import com.example.cowa_app.utils.RouterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val routerManager: RouterManager
) : ViewModel() {
    // 其他全局状态...
}