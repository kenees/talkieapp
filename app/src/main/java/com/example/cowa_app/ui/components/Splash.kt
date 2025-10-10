package com.example.cowa_app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier


@Composable
fun SplashScreen(
    content: @Composable BoxScope.() -> Unit
) {
    SideEffect {

    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
}