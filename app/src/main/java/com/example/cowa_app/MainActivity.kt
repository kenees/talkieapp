/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cowa_app


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.cowa_app.compose.TalkieApp
import com.example.cowa_app.data.repository.UserDataRepository
import com.example.cowa_app.services.AppTrackingService
import com.example.cowa_app.ui.components.DesignTheme
import com.example.cowa_app.ui.components.GlobalToast
import com.example.cowa_app.ui.theme.TalkieAppTheme
import com.example.cowa_app.utils.RouterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var userDataRepository: UserDataRepository
    @Inject lateinit var routerManager: RouterManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Displaying edge-to-edge
        enableEdgeToEdge()
        setContent {
            DesignTheme(designWidth = 480) {
                TalkieAppTheme {
                    TalkieApp()
                    GlobalToast()
                }
            }
        }

        checkLoginStatusAndInitialize()
    }

    private fun checkLoginStatusAndInitialize() {
        lifecycleScope.launch {
            val isLoggedIn = userDataRepository.isUserLoggedIn()
            if (isLoggedIn) {
                startAppTrackingService()
                routerManager.navigateToHome()
            } else {
                routerManager.navigateToLogin()
            }
        }
    }

    private fun startAppTrackingService() {
        val intent = Intent(this, AppTrackingService::class.java).apply {
            action = AppTrackingService.ACTION_START
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        stopAppTrackingService()
    }


}
