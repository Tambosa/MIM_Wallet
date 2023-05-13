package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.ui.PortfolioUiState

@Composable
fun PullRefreshCryptoIndicator(
    modifier: Modifier = Modifier,
    state: PortfolioUiState,
    pullRefreshProgress: Float
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cryptoloader))
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = state.isLoading
    )
    LottieAnimation(
        modifier = modifier.rotate(pullRefreshProgress * 180f),
        composition = lottieComposition,
        progress = { progress },
    )
}