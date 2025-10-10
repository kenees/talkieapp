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

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.cowa_app.R
import com.example.cowa_app.compose.Screen
import com.example.cowa_app.data.model.CounterViewModel
import com.example.cowa_app.ui.theme.TalkieAppTheme
//import com.example.cowa_app.compose.garden.GardenScreen
//import com.example.cowa_app.compose.plantlist.PlantListScreen
//import com.example.cowa_app.data.Plant
//import com.example.cowa_app.viewmodels.PlantListViewModel
import kotlinx.coroutines.launch

enum class TalkieAppPage(
    @StringRes val titleResId: Int,
    @DrawableRes val drawableResId: Int
) {
    MY_GARDEN(R.string.my_garden_title, R.drawable.ic_my_garden_active),
    PLANT_LIST(R.string.my_garden_title, R.drawable.ic_my_garden_active)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onPlantClick: () -> Unit = {},
//    viewModel: PlantListViewModel = hiltViewModel(),
    pages: Array<TalkieAppPage> = TalkieAppPage.values()
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(
                pagerState = pagerState,
                onFilterClick = { },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->
        HomePagerScreen(
//            onPlantClick = onPlantClick,
            navController= navController,
            pagerState = pagerState,
            pages = pages,
            Modifier.padding(top = contentPadding.calculateTopPadding())
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
//    onPlantClick: (Plant) -> Unit,
    navController: NavHostController,
    pagerState: PagerState,
    pages: Array<TalkieAppPage>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        val coroutineScope = rememberCoroutineScope()

        // Tab Row
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            pages.forEachIndexed { index, page ->
                val title = stringResource(id = page.titleResId)
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = "$title aa") },
                    icon = {
                        Icon(
                            painter = painterResource(id = page.drawableResId),
                            contentDescription = title
                        )
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // Pages
        HorizontalPager(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { index ->
            when (pages[index]) {
                TalkieAppPage.MY_GARDEN -> {
//                    GardenScreen(
//                        Modifier.fillMaxSize(),
//                        onAddPlantClick = {
//                            coroutineScope.launch {
//                                pagerState.scrollToPage(SunflowerPage.PLANT_LIST.ordinal)
//                            }
//                        },
//                        onPlantClick = {
//                            onPlantClick(it.plant)
//                        })

                    Button(
                        onClick = {
                            Log.d("TEST", "点击前往setting")
                            navController.navigate(Screen.Setting.route)
                        }
                    ) {
                        Text(
                            color = Color(255, 21, 1),
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Button(
                        onClick = {
                            Log.d("TEST", "点击前往setting")
                            navController.navigate(Screen.Login.route)
                        }
                    ) {
                        Text(
                            color = Color(255, 21, 1),
                            text = "login page",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                TalkieAppPage.PLANT_LIST -> {
//                    PlantListScreen(
//                        onPlantClick = onPlantClick,
//                        modifier = Modifier.fillMaxSize(),
//                    )
                    Button(
                        onClick = {
                            Log.d("TEST", "点击前往setting2")
                            navController.navigate(Screen.Setting.route)
                        }
                    ) {
                        Text(
                            color = Color(255, 21, 1),
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    pagerState: PagerState,
    onFilterClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = hiltViewModel(),
) {
    val count by viewModel.count.collectAsStateWithLifecycle()
    CenterAlignedTopAppBar(
        title = {
            Button(
                onClick = {
                    Log.d("TEST", "点击测试")
                    viewModel.decrement()
                }
            ) {
                Text(
                    color = Color(255, 221, 1),
//                    text = stringResource(id = R.string.app_name),
                    text = "----- $count",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

        },
        modifier = modifier,
        actions = {
            if (pagerState.currentPage == TalkieAppPage.PLANT_LIST.ordinal) {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = stringResource(
                            id = R.string.menu_filter_by_grow_zone
                        )
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
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
