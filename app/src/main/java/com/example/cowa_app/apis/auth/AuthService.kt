package com.example.cowa_app.apis.auth


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("/api/v1/mmp/talkie/user/login_imei")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("/api/v1/mmp/talkie/room/list")
    suspend fun getAudioGroup(): LoginResponse
}