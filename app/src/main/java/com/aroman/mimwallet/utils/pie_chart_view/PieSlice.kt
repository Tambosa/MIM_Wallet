package com.aroman.mimwallet.utils.pie_chart_view

import androidx.compose.ui.graphics.Color

data class PieSlice(
    val name: String,
    var value: Double,
    var startAngle: Float,
    var sweepAngle: Float,
    val color: Color
)