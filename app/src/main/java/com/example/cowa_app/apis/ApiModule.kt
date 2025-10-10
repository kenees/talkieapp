package com.example.cowa_app.apis

import com.example.cowa_app.apis.auth.AuthService
import com.example.cowa_app.utils.NetworkUtils


object ApiModule {
    private const val BASE_URL = "https://mmp.softtest.cowarobot.com"

//    private const val BASE_URL = "http://127.0.0.1:5500"

    private val retrofit by lazy {
        NetworkUtils.createRetrofit(BASE_URL)
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }


    fun setToken(token: String) = NetworkUtils.TokenManager.setToken(token)
    fun setRefreshToken(token: String) = NetworkUtils.TokenManager.setRefreshToken(token)
    fun getToken() = NetworkUtils.TokenManager.getToken()
    fun clearToken() = NetworkUtils.TokenManager.clearToken()
}