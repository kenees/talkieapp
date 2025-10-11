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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.cowa_app.R
import com.example.cowa_app.ui.components.PageBar
import com.example.cowa_app.ui.components.PageScreen
import com.example.cowa_app.ui.components.dp
import com.example.cowa_app.ui.theme.Text_24_400
import com.example.cowa_app.utils.DeviceUtils

private data class ItemValue(
    val title: String = "", val onClick: (() -> Unit)? = null, val alpha: Float = 1.0f
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutScreen() {
    val imei = DeviceUtils.getImei()
//    val androidId = DeviceUtils.getAndroidId(LocalContext.current)
    val aboutMenus = listOf<ItemValue>(
        ItemValue(
            title = "版本(-.-.-)", onClick = {
                Log.d("AboutScreen", "检测版本更新")
            }, alpha = 1.0f
        ),
//        ItemValue(
//            title = "设备编号：$androidId", alpha = 0.0f
//        ),
        ItemValue(
            title = "IMEI：$imei", alpha = 0.0f
        )
    )

    PageScreen() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 20.dp, start = 28.dp, end = 28.dp)
        ) {
            PageBar(
                actionLeft = { Box {} },
                title = "关于",
                actionRight = { Box {} },
            ) {}
            LazyColumn(
                modifier = Modifier.fillMaxHeight()
//                    .verticalScroll(rememberScrollState()) // 垂直滚动
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
    menu: ItemValue
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .background(Color(44, 46, 50), RoundedCornerShape(16.dp))
            .padding(top = 17.dp, bottom = 17.dp, start = 20.dp, end = 20.dp)
            .clickable {
                menu.onClick?.let { it() }
            }) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = menu.title,
                softWrap = true,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.Text_24_400,
            )
            Image(
                painter = painterResource(id = R.drawable.expand),
                contentDescription = null,
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
                    .rotate(-90f)
                    .alpha(menu.alpha),
                contentScale = ContentScale.Crop,
            )
        }
    }
}