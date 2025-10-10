package com.example.cowa_app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.cowa_app.ui.components.spt

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val Typography.Text_32_500: TextStyle @Composable
get() = TextStyle(
    fontWeight = FontWeight.W500,
    fontSize = 32.spt,
    color = Color(255, 255, 255)
)

val Typography.Text_32_400: TextStyle @Composable
get() = TextStyle(
    fontWeight = FontWeight.W400,
    fontSize = 32.spt,
    color = Color(255, 255, 255)
)

val Typography.Text_28_400: TextStyle @Composable
get() = TextStyle(
    fontWeight = FontWeight.W400,
    fontSize = 28.spt,
    color = Color(255, 255, 255)
)

val Typography.Text_24_400: TextStyle @Composable
get() = TextStyle(
    fontWeight = FontWeight.W400,
    fontSize = 24.spt,
    color = Color(255, 255, 255)
)

val Typography.Text_16_400: TextStyle @Composable
get() = TextStyle(
    fontWeight = FontWeight.W400,
    fontSize = 16.spt,
    color = Color(255, 255, 255, 204)
)

val Typography.Text_20_400_1: TextStyle @Composable
get() = TextStyle(
    fontWeight = FontWeight.W400,
    fontSize = 20.spt,
    color = Color(255, 255, 255)
)