package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.PortfolioUiState

@Composable
fun PullRefreshCryptoIndicator(modifier: Modifier, state: PortfolioUiState) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cryptoloader))
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        isPlaying = state.isLoading
    )
    if (state.isLoading) {
        LottieAnimation(
            modifier = modifier,
            composition = lottieComposition,
            progress = { progress }
        )
    }
}