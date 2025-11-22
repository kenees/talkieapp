package com.example.cowa_app.ui.components

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cowa_app.R
import com.example.cowa_app.data.model.GlobalCompViewModal
import com.example.cowa_app.data.repository.ToastType
import com.example.cowa_app.ui.theme.Text_24_400

@Composable
fun GlobalToast(
    viewModel: GlobalCompViewModal = hiltViewModel()
) {
    val toastState by viewModel.toastState.collectAsState()

    if (toastState.visible && toastState.title != "") {
        CustomToast(
            message = toastState.title,
            type = toastState.type,
        )
    }
}

@Composable
fun CustomToast(
    message: String,
    type: ToastType,
) {
    // 创建无限旋转动画
    val infiniteTransition = rememberInfiniteTransition(label = "loading rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Row (
       modifier = Modifier
           .fillMaxWidth()
           .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Box (
            modifier = Modifier
                .background(Color(64,64,64), RoundedCornerShape(15.dp))
                .padding(15.dp)
                .wrapContentWidth() // 宽度根据内容自适应
                .widthIn(max = with(LocalDensity.current) {
                    // 计算屏幕宽度的 70% 作为最大宽度
                    LocalConfiguration.current.screenWidthDp.dp * 0.9f
                })
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (type === ToastType.LOADING) {
                    Image(
                        painter = painterResource(id = R.drawable.loading),
                        contentDescription = null,
                        modifier = Modifier.width(64.dp).height(79.dp).rotate(rotationAngle).padding(bottom = 15.dp),
                        contentScale = ContentScale.Crop,
                    )
                } else if (type === ToastType.INFO) {
                    Row {  }
                } else {
                    var id = R.drawable.success
                    if (type === ToastType.ERROR) {
                        id = R.drawable.fail
                    } else if (type === ToastType.WARNING) {
                        id = R.drawable.warning_circle
                    }
                    Image(
                        painter = painterResource(id = id),
                        contentDescription = null,
                        modifier = Modifier.width(64.dp).height(79.dp).padding(bottom = 15.dp),
                        contentScale = ContentScale.Crop,
                    )
                }

                Text(
//                    modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 15.dp),
                    text = message,
                    color = Color(255, 255, 255),
                    style = MaterialTheme.typography.Text_24_400,
                )
            }
        }
    }
}

