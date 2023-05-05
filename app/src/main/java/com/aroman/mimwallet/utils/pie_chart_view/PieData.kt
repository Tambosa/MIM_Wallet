package com.aroman.mimwallet.utils.pie_chart_view

import androidx.compose.ui.graphics.Color

class PieData {
    val pieSlices = HashMap<String, PieSlice>()
    var totalValue = 0.0

    fun add(name: String, value: Double, color: Color) {
        if (pieSlices.containsKey(name)) {
            pieSlices[name]?.let { it.value += value }
        } else {
            pieSlices[name] =
                PieSlice(name, value, 0f, 0f, color)
        }
        totalValue += value
    }
}