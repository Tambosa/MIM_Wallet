package com.aroman.mimwallet.utils.pie_chart_view

import android.graphics.Paint
import android.graphics.PointF

data class PieSlice(
    val name: String,
    var value: Double,
    var startAngle: Float,
    var sweepAngle: Float,
    var indicatorCircleLocation: PointF,
    val paint: Paint
)