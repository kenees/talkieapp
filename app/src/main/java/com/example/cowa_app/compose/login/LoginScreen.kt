package com.example.cowa_app.compose.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.cowa_app.R
import com.example.cowa_app.data.model.GlobalCompViewModal
import com.example.cowa_app.data.model.UserDataViewModal
import com.example.cowa_app.ui.components.PageScreen
import com.example.cowa_app.ui.components.dp
import com.example.cowa_app.ui.theme.Text_16_400
import com.example.cowa_app.ui.theme.Text_24_400
import com.example.cowa_app.ui.theme.Text_28_400


@Composable
fun LoginScreen(
    navController: NavHostController,
) {
    PageScreen() {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 20.dp, start = 28.dp, end = 28.dp)
        ) {
            LoginBar()
            LoginContext()
        }
    }
}

@Composable
fun LoginBar() {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.width(140.dp).height(40.dp),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .width(90.dp)
                .height(40.dp)
                .background(Color(255, 255, 255, 51), RoundedCornerShape(8.dp))
                .clickable {
                    Log.d("LoginScreen: ", "click")
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.setting),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    color = Color(255, 251, 241),
                    text = "设置",
                    style = MaterialTheme.typography.Text_16_400,
                )
            }
        }
    }
}

@Composable
fun LoginContext(
    viewModel: UserDataViewModal = hiltViewModel(),
    globalCompViewModal: GlobalCompViewModal = hiltViewModel(),
) {
    val context = LocalContext.current
    val loginDisabled by viewModel.loginDisabled.collectAsStateWithLifecycle()
    val toastData by globalCompViewModal.toastState.collectAsStateWithLifecycle()

    var hasBind = true


    if (hasBind) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // 水平居中
            verticalArrangement = Arrangement.Center // 垂直居中
        ) {
            Text(
                color = Color(255, 251, 241),
                text = "欢迎登录",
                style = MaterialTheme.typography.Text_28_400,
            )

            Row(
                modifier = Modifier.padding(top = 56.dp).width(330.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "本设备",
                    style = MaterialTheme.typography.Text_24_400,
                )
                Text(
                    color = Color(255, 116, 57),
                    text = "暂未激活",
                    style = MaterialTheme.typography.Text_28_400,
                    modifier = Modifier.padding(start = 10.dp, end = 15.dp),
                    fontWeight = FontWeight.W600,
                )
                Text(
                    text = "无法登录",
                    style = MaterialTheme.typography.Text_24_400,
                )
            }

            Row(
                modifier = Modifier.padding(top = 11.dp).width(330.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "请联系",
                    style = MaterialTheme.typography.Text_24_400,
                )
                Text(
                    text = "管理员",
                    style = MaterialTheme.typography.Text_28_400,
                    modifier = Modifier.padding(start = 10.dp, end = 15.dp),
                    fontWeight = FontWeight.W600,
                )
                Text(
                    text = "处理",
                    style = MaterialTheme.typography.Text_24_400,
                )
            }

            Box(
                modifier = Modifier.height(134.dp)
            )

            Box(
                modifier = Modifier
                    .width(360.dp)
                    .height(60.dp)
                    .run {
                        if (loginDisabled) {
                            this.background(Color(100, 100, 100), RoundedCornerShape(30.dp))
                        } else {
                            this.background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(92, 193, 255),
                                        Color(31, 94, 255)
                                    ),
                                    startX = 0f,
                                    endX = Float.POSITIVE_INFINITY
                                ),
                                RoundedCornerShape(30.dp)
                            )
                        }
                    }
                    .clickable() {
                        Log.d("LoginScreen: ", "click")
                        viewModel.loginDisabledHandle(!loginDisabled)
                        globalCompViewModal.loading("加载中")
                        viewModel.login(context)
                    },
                contentAlignment = Alignment.Center
            ) {
                    Text(
                        color = Color(255, 251, 241),
                        text = "登录",
                        style = MaterialTheme.typography.Text_24_400,
                    )
            }
        }
    } else {
        Column {

        }
    }

}

