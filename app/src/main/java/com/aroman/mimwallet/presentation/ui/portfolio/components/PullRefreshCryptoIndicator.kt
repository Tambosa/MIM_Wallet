package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.aroman.mimwallet.R

@Composable
fun PullRefreshCryptoIndicator(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    pullRefreshProgress: Float
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cryptoloader))
        val progress by animateLottieCompositionAsState(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever,
            isPlaying = isLoading
        )
        LottieAnimation(
            modifier = modifier.rotate(pullRefreshProgress * 180f),
            composition = lottieComposition,
            progress = { progress },
        )
    }
}