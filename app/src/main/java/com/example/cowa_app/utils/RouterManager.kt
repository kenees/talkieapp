package com.example.cowa_app.utils

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouterManager @Inject constructor() {

    private var navController: NavHostController? = null

    // 设置 NavController（在根 Composable 中调用）
    fun setNavController(navController: NavHostController) {
        this.navController = navController
    }

    // 导航方法
    fun navigate(route: String, navOptions: NavOptions? = null) {
        navController?.navigate(route, navOptions)
    }

    fun navigateToHome() {
        navController?.navigate("home") {
            popUpTo("login") { inclusive = true }
        }
    }

    fun navigateToLogin() {
        navController?.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
    }

    fun navigateToProfile() {
        navigate("profile")
    }

    fun navigateToSettings() {
        navigate("settings")
    }

    fun popBackStack() {
        navController?.popBackStack()
    }

    // 带参数的导航
    fun navigateToDetail(itemId: String) {
        navigate("detail/$itemId")
    }

    fun navigateToChat(chatId: String, userName: String) {
        navigate("chat/${chatId}?userName=${userName}")
    }

    // 获取当前路由
    fun getCurrentRoute(): String? {
        return navController?.currentBackStackEntry?.destination?.route
    }
}