package com.example.cowa_app.utils

import android.util.Log
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouterManager @Inject constructor() {

    private var navController: NavHostController? = null

    private var callback: (() -> Unit)? = null

    // 设置 NavController（在根 Composable 中调用）
    fun setNavController(navController: NavHostController) {
        Log.d("Navigate", "setNavController")
        this.navController = navController
        if (callback != null) {
            callback.let {
                it?.invoke()
            }
        }
    }

    // 导航方法
    fun navigate(route: String, navOptions: NavOptions? = null) {
        if (getCurrentRoute() == route) return
        if (navController == null) {
            callback = {
                navigate(route, navOptions)
            }
        } else {
            navController?.navigate(route, navOptions)
        }
    }

    fun navigateToHome() {
        if (getCurrentRoute() == "home")  return
        if (navController == null) {
            callback = { navigateToHome() }
        } else {
            navController?.navigate("home") {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }

    fun navigateToLogin() {
        if (getCurrentRoute() == "login") return
        if (navController == null) {
            callback = { navigateToLogin() }
        } else {
            navController?.navigate("login") {
                Log.d("Navigate", "to login  ")
                popUpTo(0) {
                    inclusive = true
                }
            }
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

//    // 带参数的导航
//    fun navigateToDetail(itemId: String) {
//        navigate("detail/$itemId")
//    }
//
//    fun navigateToChat(chatId: String, userName: String) {
//        navigate("chat/${chatId}?userName=${userName}")
//    }

    // 获取当前路由
    fun getCurrentRoute(): String? {
        return navController?.currentBackStackEntry?.destination?.route
    }
}