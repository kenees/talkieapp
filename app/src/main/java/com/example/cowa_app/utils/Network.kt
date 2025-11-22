package com.example.cowa_app.utils

import AppEvent
import AppEventBus
import android.util.Log
import com.example.cowa_app.data.repository.ToastType
import com.example.cowa_app.utils.NetworkUtils.TokenManager.getToken
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.TlsVersion
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(
        val message: String,
        val code: Int? = null,
        val exception: Exception? = null
    ) : NetworkResult<Nothing>()

    object Loading : NetworkResult<Nothing>()
}


object NetworkUtils {

    object TokenManager {
        private var token: String = ""
        private var refreshToken: String = ""

        fun setToken(value: String) {
            Log.d("NetWork", "setToken: $value")
            token = value
        }

        fun setRefreshToken(value: String) {
            refreshToken = value
        }

        fun getToken(): String {
            return token
        }

        fun getRefreshToken(): String {
            return  refreshToken
        }

        fun clearToken() {
            refreshToken = ""
            token = ""
        }
    }

    class AuthInterceptor:Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            val requestBuilder = originalRequest.newBuilder()

            getToken()?.let { token ->
                requestBuilder.header("authorization", "bearer $token")
                requestBuilder.header("basis-token", "bearer $token")
                Log.d("HTTP", "Adding Authorization header with token: ${token.take(10)}...")
            }

            requestBuilder
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")

            val request = requestBuilder.build()

            Log.d("HTTP", request.toString())
            try {
                val response = chain.proceed(request)

                handleResponseError(response)

                return response
            } catch (e: Exception) {
                Log.e("HTTP_ERROR", "Network request failed: ${e.message}")
                AppEventBus.emitScope(AppEvent.ShowToast(e.message ?: "网络请求失败", ToastType.ERROR))
                return createErrorResponse(chain.request(), e.message ?: "网络请求失败")
            }
        }

        private fun handleResponseError(response: Response) {
            try {
                if (!response.isSuccessful) {
                    handleHttpError(response)
                } else {
                    Log.w("HTTP_ERROR_CHECK", response.code.toString())
                    val responseBody = response.peekBody(Long.MAX_VALUE).string()
                    val jsonObject = JSONObject(responseBody)
                    val code = jsonObject.optInt("code", -1)
                    // val status = jsonObject.optString("status", "")

                    when (code) {
                        200 -> {}
                        401 -> {
                            Log.d("BUSINESS_ERROR", "token过期")
                            val message = jsonObject.optString("message", "")
                            AppEventBus.emitScope(AppEvent.ShowToast(message, ToastType.ERROR))
                        }

                        else -> {
                            val message = jsonObject.optString("message", "")
                            Log.d("BUSINESS_ERROR", "$message")
                            AppEventBus.emitScope(AppEvent.ShowToast(message, ToastType.ERROR))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("BUSINESS_ERROR", "Failed to parse business error: ${e.message}")
                // 如果解析失败，回退到 HTTP 错误处理
                if (!response.isSuccessful) {
                    handleHttpError(response)
                }
            }
        }

        private fun handleHttpError(response: Response) {
            when (response.code) {
                401 -> {
                    Log.w("HTTP_AUTH", "Token expired, clearing token")
                }

                403 -> throw NetworkException("权限不足", 403)
                404 -> throw NetworkException("资源不存在", 404)
                500 -> throw NetworkException("服务器内部错误", 500)
                else -> throw NetworkException("网络请求失败: ${response.code}")
            }
        }

        private fun createErrorResponse(request: Request, errorMessage: String): Response {
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(599) // 自定义错误码，表示客户端错误
                .message(errorMessage)
                .body(
                    ResponseBody.create(
                    "application/json".toMediaTypeOrNull(),
                    "{\"code\": 599, \"message\": \"$errorMessage\"}"
                ))
                .build()
        }
    }

    class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
//            val startTime = System.nanoTime()

//            Log.d("HTTP", "🚀 --> ${request.method} ${request.url}")
            request.headers.forEach { (name, value) ->
                Log.d("HTTP", "   $name: $value")
            }

            val response = chain.proceed(request)
//            val endTime = System.nanoTime()
//            Log.d("HTTP", "⏱️ Time: ${(endTime - startTime) / 1e6}ms")

            return response
        }
    }

    fun createOkHttpClient(): OkHttpClient {

        try {
            // 创建信任所有证书的 TrustManager
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // 安装信任所有证书的 SSLContext
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // 创建使用我们 SSLContext 的 SSLSocketFactory
            val sslSocketFactory = sslContext.socketFactory

            // 创建支持所有 TLS 版本的连接规格
            val connectionSpecs = listOf(
                ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(
                        TlsVersion.TLS_1_3,
                        TlsVersion.TLS_1_2,
                        TlsVersion.TLS_1_1,
                        TlsVersion.TLS_1_0
                    )
                    .supportsTlsExtensions(true)
                    .build(),
                ConnectionSpec.CLEARTEXT  // 支持 HTTP
            )

            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { hostname, session -> true }  // 跳过主机名验证
                .connectionSpecs(connectionSpecs)
                .addInterceptor(LoggingInterceptor())
                .addInterceptor(AuthInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        } catch (e: Exception) {
            throw RuntimeException("Failed to create unsafe OkHttpClient", e)
        }
    }

    fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

// 网络异常类
class NetworkException(
    message: String,
    val code: Int? = null
) : Exception(message)

// Token 过期事件
class TokenExpiredEvent