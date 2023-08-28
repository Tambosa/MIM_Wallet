package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.utils.pie_chart_view.PieData
import com.aroman.mimwallet.utils.pie_chart_view.PieSlice
import com.example.core_ui.theme.Typography
import com.example.core_ui.theme.spacing
import com.example.data_network.domain.entity.DisplayableCoin

@Composable
fun PieChart(
    coins: List<DisplayableCoin>
) {
    val pieData = PieData()
    val colors = remember {
        listOf(
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Magenta,
            Color.DarkGray,
            Color.Cyan,
            Color.Yellow,
            Color.Gray
        )
    }
    if (coins.isNotEmpty()) {
        coins.sortedBy { it.price * it.count }
        coins.forEachIndexed { index, coin ->
            pieData.add(
                name = coin.symbol,
                value = coin.price * coin.count,
                color = try {
                    colors[index]
                } catch (e: ArrayIndexOutOfBoundsException) {
                    Color.Black
                }
            )
        }
        var lastAngle = 270f
        pieData.pieSlices.toList().sortedByDescending { (_, slice) -> slice.value }.toMap()
            .forEach {
                it.value.startAngle = lastAngle
                it.value.sweepAngle = (((it.value.value / pieData.totalValue)) * 360f).toFloat()
                lastAngle += it.value.sweepAngle
            }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            ) {
                pieData.pieSlices.forEach {
                    drawArc(
                        color = it.value.color,
                        startAngle = it.value.startAngle,
                        sweepAngle = it.value.sweepAngle,
                        useCenter = true
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp)
        ) {
            val pieSlices =
                pieData.pieSlices.toList().sortedByDescending { (_, slice) -> slice.value }
            if (pieSlices.size >= 5) {
                for (i in 0..4) {
                    PieHint(pieSlice = pieSlices[i].second)
                }
            } else {
                pieData.pieSlices.forEach {
                    PieHint(it.value)
                }
            }
        }
    }
}

@Composable
private fun PieHint(pieSlice: PieSlice) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = MaterialTheme.spacing.paddingSmall,
                end = MaterialTheme.spacing.paddingSmall,
                top = MaterialTheme.spacing.paddingExtraSmall
            ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.paddingLarge)
    ) {
        Canvas(modifier = Modifier) {
            drawCircle(pieSlice.color, radius = 20f, center = Offset(0f, 10.dp.toPx()))
        }
        Text(text = pieSlice.name, style = Typography.bodySmall)
    }
}