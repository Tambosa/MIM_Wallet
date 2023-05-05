package com.aroman.mimwallet.presentation.ui.portfolio

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.graphics.applyCanvas
import androidx.navigation.NavController
import com.aroman.mimwallet.domain.model.PortfolioUiEvent
import com.aroman.mimwallet.domain.model.PortfolioUiState
import com.aroman.mimwallet.presentation.ui.portfolio.components.CoinContent
import com.aroman.mimwallet.presentation.ui.portfolio.components.Header
import com.aroman.mimwallet.presentation.ui.portfolio.components.PullRefreshCryptoIndicator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PortfolioScreen(
    navController: NavController,
    onThemeChange: () -> Unit,
    state: PortfolioUiState,
    onEvent: (PortfolioUiEvent) -> Unit,
) {
    LaunchedEffect(true) {
        onEvent(PortfolioUiEvent.ShowData(state))
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onEvent(PortfolioUiEvent.ShowData(state)) })
    val animatedOffset by animateFloatAsState(
        targetValue = if (state.isLoading) 1f else (pullRefreshState.progress)
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        PullRefreshCryptoIndicator(state = state)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .offset(x = 0.dp, y = (animatedOffset * PULL_REFRESH_OFFSET).dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            val view = LocalView.current
            val resources = LocalContext.current.resources
            val onThemeChangeWithScreenshot = remember {
                {
                    onThemeChange()
                    setScreenshot(view, resources)
                }
            }
            Header(
                onThemeChange = onThemeChangeWithScreenshot,
                navController = navController,
            )
            CoinContent(
                state = state,
                onEvent = onEvent,
                navController = navController
            )
        }
    }
}

private fun setScreenshot(view: View, resources: Resources) {
    val bmp = Bitmap.createBitmap(
        view.width, view.height,
        Bitmap.Config.ARGB_8888
    ).applyCanvas {
        view.draw(this)
    }
    view.foreground = BitmapDrawable(resources, bmp)

    ObjectAnimator.ofInt(view.foreground, "alpha", 255, 0).apply {
        duration = 800
        doOnEnd {
            view.foreground = null
        }
    }.start()
}

private const val PULL_REFRESH_OFFSET = 120