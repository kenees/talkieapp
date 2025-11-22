/*
 * Copyright 2023 Google LLC
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

package com.example.cowa_app.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cowa_app.compose.home.HomeScreen
import com.example.cowa_app.compose.login.LoginScreen
import com.example.cowa_app.compose.setting.AboutScreen
import com.example.cowa_app.compose.setting.MyInfoScreen
import com.example.cowa_app.compose.setting.SettingScreen
import com.example.cowa_app.data.model.AppViewModel

@Composable
fun TalkieApp() {
    val navController = rememberNavController()
    val routerManager = hiltViewModel<AppViewModel>().routerManager

    LaunchedEffect(Unit) {
        routerManager.setNavController(navController)
    }

    TalkieAppNavHost(
        navController = navController
    )
}

@Composable
fun TalkieAppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = {
                // 进入设置页面时，从右向左滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                // 关键：离开设置页面时（前进到其他页面），向左滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                // 从设置页面返回时，前一个页面从右侧滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            popExitTransition = {
                // 从设置页面返回时，设置页面向右滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            HomeScreen()
        }

        // 设置相关 - 修复关键在这里
        composable(
            route = Screen.Setting.route,
            arguments = Screen.Setting.navArguments,
            enterTransition = {
                // 进入设置页面时，从右向左滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                // 关键：离开设置页面时（前进到其他页面），向左滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                // 从设置页面返回时，前一个页面从右侧滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            popExitTransition = {
                // 从设置页面返回时，设置页面向右滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            SettingScreen()
        }

        // 其他页面使用相同的完整配置
        composable(
            route = Screen.About.route,
            enterTransition = {
                // 进入设置页面时，从右向左滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                // 关键：离开设置页面时（前进到其他页面），向左滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                // 从设置页面返回时，前一个页面从右侧滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            popExitTransition = {
                // 从设置页面返回时，设置页面向右滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            AboutScreen()
        }

        composable(
            route = Screen.MyInfo.route,
            enterTransition = {
                // 进入设置页面时，从右向左滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                // 关键：离开设置页面时（前进到其他页面），向左滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                // 从设置页面返回时，前一个页面从右侧滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            popExitTransition = {
                // 从设置页面返回时，设置页面向右滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            MyInfoScreen()
        }

        composable(
            route = Screen.Login.route,
            enterTransition = {
                // 进入设置页面时，从右向左滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                // 关键：离开设置页面时（前进到其他页面），向左滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                // 从设置页面返回时，前一个页面从右侧滑入
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            popExitTransition = {
                // 从设置页面返回时，设置页面向右滑出
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            LoginScreen()
        }
    }
}

// Helper function for calling a share functionality.
// Should be used when user presses a share button/menu item.
//private fun createShareIntent(activity: Activity, plantName: String) {
//    val shareText = activity.getString(R.string.share_text_plant, plantName)
//    val shareIntent = ShareCompat.IntentBuilder(activity)
//        .setText(shareText)
//        .setType("text/plain")
//        .createChooserIntent()
//        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
//    activity.startActivity(shareIntent)
//}