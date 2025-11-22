package com.example.cowa_app.apis.auth


import com.example.cowa_app.apis.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("/api/v1/mmp/talkie/user/login_imei")
    suspend fun login(@Body request: LoginRequest): Response<LoginMessage>

    @GET("/api/v1/mmp/talkie/room/list")
    suspend fun getAudioGroup(): LoginResponse
}