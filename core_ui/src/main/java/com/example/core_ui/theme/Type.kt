package com.example.core_ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.core_ui.R

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)