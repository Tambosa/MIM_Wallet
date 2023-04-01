package com.aroman.mimwallet.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aroman.mimwallet.R

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.inter)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)