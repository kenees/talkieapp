package com.example.cowa_app.apis

data class Response<T>(
    val code: Int,
    val status: Boolean,
    val data: T,
    val body: T,
    val message: T
)

