package com.example.cowa_app.apis.app

import com.example.cowa_app.apis.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppService {
    @GET("/api/v1/mmp/system/version/latest")
    suspend fun getLatestVersion(): Response<LatestVersionResponse>

    @POST("/api/v1/mmp/system/version/newest")
    suspend fun postNewest(@Body request: PostNewest): Response<Unit>
//
//    @POST("/api/v1/mmp/event/location/report")
//    suspend fun deviceGps(@Body request: DeviceGpsPost): Response
}
