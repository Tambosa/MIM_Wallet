package com.aroman.mimwallet.utils

import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Color.applyElevationOverlay(elevation: Dp = 0.dp): Color {
    val absoluteElevation = LocalAbsoluteElevation.current + elevation
    return this.copy(alpha = (absoluteElevation.value) / 100f).compositeOver(this)
}