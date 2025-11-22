package com.example.cowa_app.apis.auth

import java.util.Objects


data class LoginRequest (
    val serial_number: String,
    val android_id: String,
    val platform: String,
    val device_name: String,
)

data class LoginResponse(
    val message: LoginMessage,
)

data class LoginMessage(
    var access_token: String,
    var category: String,
    var category_id: String,
    var refresh_token: String,
    var tenant: String,
    var user_info: UserInfo
)

data class UserInfo(
    val account: String,
    val id: Int,
    val is_admin: Boolean,
    val phone: String,
    val tenant: String,
    val user_name: String,
)
