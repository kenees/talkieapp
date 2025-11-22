package com.example.cowa_app.apis.app

data class PostNewest(
    val serial_number: String,
    val version: String,
)

data class DeviceGpsPost(
    val android_id: String,
    val time_stamp: Int,
    val latitude: Int,
    val longitude: Int,
    val lat: Int,
    val lon: Int,
)

data class LatestVersionResponse(
    val data: String,
    val id: Int,
    val name: String,
    val path: String,
    val version: Version
) {
    data class Version(
        val major: Int,
        val minor: Int,
        val revision: Int,
    )
}
