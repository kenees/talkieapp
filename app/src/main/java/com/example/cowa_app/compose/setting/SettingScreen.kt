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

package com.example.cowa_app.compose.setting

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cowa_app.R
import com.example.cowa_app.data.model.AppViewModel
import com.example.cowa_app.data.model.UserDataViewModal
import com.example.cowa_app.ui.components.PageBar
import com.example.cowa_app.ui.components.PageScreen
import com.example.cowa_app.ui.components.dp
import com.example.cowa_app.ui.theme.Text_28_400
import com.example.cowa_app.utils.DeviceUtils
import com.example.cowa_app.utils.RouterManager

private enum class MenuDataList(
    @StringRes val titleResId: Int,
    val id: Int,
    ) {
//    SERVER_CONFIG(R.string.setting_menu_server),
    SYSTEM_SETTING(R.string.setting_menu_system, 1),
    CLEAR_CACHE(R.string.setting_menu_cache, 2),
    ABOUT(R.string.setting_menu_about, 3),
    MY(R.string.setting_menu_my, 4),
    LOGOUT(R.string.setting_menu_logout, 5)
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingScreen(
    routerManager: RouterManager = hiltViewModel<AppViewModel>().routerManager
) {
    PageScreen() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 20.dp, start = 28.dp, end = 28.dp)
        ) {
            PageBar(
                actionLeft = { Box {} },
                title = "设置",
                actionRight = { Box {} },
            ) {}

            LazyColumn(
                modifier = Modifier.fillMaxHeight()
//                    .verticalScroll(rememberScrollState()) // 垂直滚动
            ) {
                items(MenuDataList.entries.toTypedArray()) { menu ->
                    ActionItem(
                        menu = menu,
                        routerManager
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionItem(
    menu: MenuDataList,
    routerManager: RouterManager,
    appViewModel: AppViewModel = hiltViewModel<AppViewModel>(),
    userDataViewModel: UserDataViewModal = hiltViewModel<UserDataViewModal>(),
) {
    val context = LocalContext.current
    var renderKey by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (menu.id == 1) {
            appViewModel.startTimer()
        }
        if (menu.id == 4 || menu.id == 5) {
            renderKey = appViewModel.checkLoginStatus()
        }
    }

    if (renderKey) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
                .background(Color(44, 46, 50), RoundedCornerShape(16.dp))
                .padding(top = 17.dp, bottom = 17.dp, start = 20.dp, end = 20.dp)
                .clickable {
                    if (menu.id == 1) {
                        DeviceUtils.goToSetting(context)
                    } else if (menu.id == 2) {
                        DeviceUtils.cleanCache(context)
                    } else if (menu.id == 3) {
                        routerManager.navigate("about")
                    } else if (menu.id == 4) {
                        routerManager.navigate("myInfo")
                    } else if (menu.id == 5) {
                        userDataViewModel.logout()
                    }
                }
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = menu.titleResId),
                    style = MaterialTheme.typography.Text_28_400,
                )
                Image(
                    painter = painterResource(id = R.drawable.expand),
                    contentDescription = null,
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .rotate(-90f),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}
