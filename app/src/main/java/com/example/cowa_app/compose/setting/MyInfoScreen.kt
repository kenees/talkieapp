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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cowa_app.data.model.UserDataViewModal
import com.example.cowa_app.ui.components.PageBar
import com.example.cowa_app.ui.components.PageScreen
import com.example.cowa_app.ui.components.dp
import com.example.cowa_app.ui.theme.Text_28_400

private data class InfoValue(
    val title: String = "",
    val value: String? = "",
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyInfoScreen(
    userDataViewModel: UserDataViewModal = hiltViewModel<UserDataViewModal>(),
) {

    val userPreferences by userDataViewModel.userPreferences.collectAsStateWithLifecycle()

    val phone = userPreferences?.phone?.length?.let {
        if (it > 11) {
            userPreferences?.phone?.replace(Regex("(\\d{5})\\d{4}(\\d{4})"), "$1****$2")
        } else {
            userPreferences?.phone?.replace(Regex("(\\d{3})\\d{4}(\\d{4})"), "$1****$2")
        }
    }

    val aboutMenus = listOf<InfoValue>(
        InfoValue(
            title = "姓名:", value = userPreferences?.userName
        ),
        InfoValue(
            title = "所在项目:", value = userPreferences?.category
        ),
        InfoValue(
            title = "联系电话:", value = phone
        ),
    )

    PageScreen() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 20.dp, start = 28.dp, end = 28.dp)
        ) {
            PageBar(
                actionLeft = { Box {} },
                title = "我的信息",
                actionRight = { Box {} },
            ) {}
            LazyColumn(
                modifier = Modifier.fillMaxHeight()
            ) {
                items(aboutMenus) { menu ->
                    ActionItem(
                        menu = menu,
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionItem(
    menu: InfoValue
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)

    ) {
        Box(
            Modifier.padding(start = 20.dp, bottom = 10.dp, top = 15.dp)
        ) {
            Text(
                text = menu.title,
                style = MaterialTheme.typography.Text_28_400
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color(44, 46, 50), RoundedCornerShape(16.dp))
                .padding(top = 17.dp, bottom = 17.dp, start = 20.dp, end = 20.dp)
        ) {
            menu.value?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.Text_28_400,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}