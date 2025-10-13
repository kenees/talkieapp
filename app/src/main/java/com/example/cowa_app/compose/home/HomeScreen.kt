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

package com.example.cowa_app.compose.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cowa_app.R
import com.example.cowa_app.data.model.AppViewModel
import com.example.cowa_app.ui.components.PageScreen
import com.example.cowa_app.ui.components.dp
import com.example.cowa_app.ui.theme.Text_28_400
import com.example.cowa_app.utils.RouterManager

enum class EntryData(
    val id: Int,
    @StringRes val titleResId: Int,
    @DrawableRes val drawableResId: Int,
) {
    TALKIE(1, R.string.home_menu_talkie, R.drawable.v2_menu_01),
    VIDEO(2, R.string.home_menu_video, R.drawable.v2_menu_05),
    EVENT(3, R.string.home_menu_event, R.drawable.v2_menu_02),
    COLLABORATION(4, R.string.home_menu_collaboration, R.drawable.v2_menu_06),
    SETTING(5, R.string.home_menu_setting, R.drawable.v2_menu_03),
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
) {

    val groupedEntries = remember {
        EntryData.entries.chunked(4)
    }
    val pageCount = groupedEntries.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

    PageScreen() {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.weight(1f)
            ) { index ->
                DefaultMenuPage(menuItems = groupedEntries[index])
            }
            PageIndicator(
                pageCount = pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}


@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) {
                Color(0, 122, 255, 255)
            } else {
                Color(204, 204, 204, 255)
            }
            Box(
                modifier = Modifier
                    .size(23.dp, 23.dp)
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .background(color, RoundedCornerShape(23.dp))
            )
        }
    }
}

@Composable
private fun DefaultMenuPage(menuItems: List<EntryData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 固定2列
        modifier = Modifier.fillMaxSize(),
    ) {
        items(menuItems) { item ->
            GridItem(data = item)
        }
    }
}

@Composable
fun GridItem(
    data: EntryData,
    routerManager: RouterManager = hiltViewModel<AppViewModel>().routerManager
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .padding(top = 30.dp)
            .clickable {
                if (data.id == 1) {

                } else if (data.id == 2) {

                } else if (data.id == 3) {

                } else if (data.id == 4) {

                } else if (data.id == 5) {
                    routerManager.navigate("setting")
                }
            }, // 保持正方形
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = data.drawableResId),
                contentDescription = "Menu1",
                modifier = Modifier.size(140.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(id = data.titleResId),
                style = MaterialTheme.typography.Text_28_400,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class)
//@Preview
//@Composable
//private fun HomeScreenPreview() {
//    TalkieAppTheme {
//        val pages = TalkieAppPage.values()
//        HomePagerScreen(
////            onPlantClick = {},
//            navController = nav,
//            pagerState = rememberPagerState(pageCount = { pages.size }),
//            pages = pages
//        )
//    }
//}
