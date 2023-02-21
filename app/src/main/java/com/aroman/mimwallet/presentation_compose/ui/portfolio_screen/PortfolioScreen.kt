package com.aroman.mimwallet.presentation_compose.ui.portfolio_screen

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.animation.doOnEnd
import androidx.core.graphics.applyCanvas
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.presentation_compose.ui.portfolio_screen.compose_children.*
import com.aroman.mimwallet.presentation_compose.ui.theme.AppTheme
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeThemeViewModel
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel

@Composable
fun PortfolioScreen(
    walletViewModel: ComposeWalletViewModel,
    themeViewModel: ComposeThemeViewModel,
) {
    val portfolioState by walletViewModel.portfolio.collectAsState()
    val timePeriodSelection by walletViewModel.timePeriod.collectAsState()
    val isDarkTheme = themeViewModel.isDarkTheme.collectAsState(initial = true)

    var isLoading by remember { mutableStateOf(true) }
    isLoading = portfolioState is ViewState.Loading

    LaunchedEffect(true) {
        walletViewModel.getPortfolio()
    }

    AppTheme(useDarkTheme = isDarkTheme.value) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                val view = LocalView.current
                Header(
                    themeViewModel = themeViewModel,
                    walletViewModel = walletViewModel,
                    isLoading = isLoading,
                    onThemeChange = {
                        setScreenshot(view)
                    }
                )
                key(portfolioState.data?.coinList) {
                    PieChart(portfolioState = portfolioState)
                }
                TotalPrice(
                    portfolioState = portfolioState,
                    timePeriodSelection = timePeriodSelection
                )
                TimePeriodSelection(
                    walletViewModel = walletViewModel,
                    timePeriodSelection = timePeriodSelection
                )
                CoinContent(
                    portfolioState = portfolioState,
                    timePeriodSelection = timePeriodSelection
                )
            }
        }
    }
}

private fun setScreenshot(view: View) {
    val bmp = Bitmap.createBitmap(
        view.width, view.height,
        Bitmap.Config.ARGB_8888
    ).applyCanvas {
        view.draw(this)
    }
    view.foreground = BitmapDrawable(bmp)

    ObjectAnimator.ofInt(view.foreground, "alpha", 255, 0).apply {
        duration = 800
        doOnEnd {
            view.foreground = null
        }
    }.start()
}