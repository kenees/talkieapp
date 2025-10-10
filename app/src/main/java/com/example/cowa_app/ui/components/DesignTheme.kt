package com.example.cowa_app.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cowa_app.ui.components.LocalDesignScale

@Composable
fun DesignTheme(
    designWidth: Int = 480,
    content: @Composable () -> Unit
) {
    Log.d("DesignTheme", LocalConfiguration.current.screenWidthDp.toString())
    val scale: Float = LocalConfiguration.current.screenWidthDp / designWidth.toFloat()

    CompositionLocalProvider(
        LocalDesignScale provides scale
    ) {
        content()
    }
}

val LocalDesignScale = staticCompositionLocalOf { 1f }

// 基于主题的扩展

val Int.dp: Dp
    @Composable get() = (this * LocalDesignScale.current).dp
val Float.dp: Dp
    @Composable get() = (this * LocalDesignScale.current).dp

val Int.sp: TextUnit
    @Composable get() = (this * LocalDesignScale.current).sp

val Float.spt: TextUnit
    @Composable get() = (this * LocalDesignScale.current).sp

val Int.spt: TextUnit
    @Composable get() = (this * LocalDesignScale.current).sp