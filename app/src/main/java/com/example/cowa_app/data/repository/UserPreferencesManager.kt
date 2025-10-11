package com.example.cowa_app.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey

import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

data class UserPreferences(
    val token: String = "",
    val refreshToken: String = "",
    val userId: Int = -1,
    val account: String = "",
    val phone: String = "",
    val tenant: String = "",
    val userName: String = "",
    val category: String = "",
    val categoryId: String = "",
    val loginTime: Long = 0,
    val isLoggedIn: Boolean = false
)

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

    // 定义键
    private object PreferencesKeys {
        val TOKEN = stringPreferencesKey("token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_ID = intPreferencesKey("user_id")
        val ACCOUNT = stringPreferencesKey("account")
        val PHONE = stringPreferencesKey("phone")
        val TENANT = stringPreferencesKey("tenant")
        val USER_NAME = stringPreferencesKey("user_name")
        val CATEGORY = stringPreferencesKey("category")
        val CATEGORY_ID = stringPreferencesKey("category_id")
        val LOGIN_TIME = longPreferencesKey("login_time")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    // 用户信息流
    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            // 处理异常
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                token = preferences[PreferencesKeys.TOKEN] ?: "",
                refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN] ?: "",
                userId = preferences[PreferencesKeys.USER_ID] ?: -1,
                account = preferences[PreferencesKeys.ACCOUNT] ?: "",
                phone = preferences[PreferencesKeys.PHONE] ?: "",
                tenant = preferences[PreferencesKeys.TENANT] ?: "",
                userName = preferences[PreferencesKeys.USER_NAME] ?: "",
                category = preferences[PreferencesKeys.CATEGORY] ?: "",
                categoryId = preferences[PreferencesKeys.CATEGORY_ID] ?: "",
                loginTime = preferences[PreferencesKeys.LOGIN_TIME] ?: 0,
                isLoggedIn = preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
            )
        }

    // 保存用户信息
    suspend fun saveUserPreferences(userPrefs: UserPreferences) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOKEN] = userPrefs.token
            preferences[PreferencesKeys.REFRESH_TOKEN] = userPrefs.refreshToken
            preferences[PreferencesKeys.USER_ID] = userPrefs.userId
            preferences[PreferencesKeys.ACCOUNT] = userPrefs.account
            preferences[PreferencesKeys.ACCOUNT] = userPrefs.account
            preferences[PreferencesKeys.PHONE] = userPrefs.phone
            preferences[PreferencesKeys.TENANT] = userPrefs.tenant
            preferences[PreferencesKeys.USER_NAME] = userPrefs.userName
            preferences[PreferencesKeys.CATEGORY] = userPrefs.category
            preferences[PreferencesKeys.CATEGORY_ID] = userPrefs.categoryId
            preferences[PreferencesKeys.LOGIN_TIME] = userPrefs.loginTime
            preferences[PreferencesKeys.IS_LOGGED_IN] = userPrefs.isLoggedIn
        }
    }



    @Volatile
    private var cachedToken: String? = null

    suspend fun getToken(): String {
        cachedToken?.let { return  it }

        return  userPreferencesFlow.first().token.also { token -> cachedToken = token }
    }

    // 单独更新 token
    suspend fun updateToken(token: String) {
        cachedToken = token
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOKEN] = token
        }
    }

    // 清除用户信息（登出）
    suspend fun clearUserPreferences() {
        cachedToken = null
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getCachedToken(): String? {
        return  cachedToken
    }
}