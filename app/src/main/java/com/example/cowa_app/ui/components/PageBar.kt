package com.example.cowa_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cowa_app.ui.theme.Text_32_500

@Composable
fun PageBar(
    title: String,
    actionLeft: @Composable (() -> Unit),
    actionRight: @Composable (BoxScope.() -> Unit),
    function: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(60.dp)
        ) {
            actionLeft()
        }

        Box() {
            Text(
                color = Color(255, 251, 241),
                text = title,
                style = MaterialTheme.typography.Text_32_500,
            )
        }

        Box(
            modifier = Modifier
                .width(70.dp)
                .height(60.dp)
        ) {
            actionRight()
        }
    }
}