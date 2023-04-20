package com.aroman.mimwallet.presentation.ui.portfolio

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.animation.doOnEnd
import androidx.core.graphics.applyCanvas
import androidx.navigation.NavController
import com.aroman.mimwallet.presentation.ui.portfolio.components.CoinContent
import com.aroman.mimwallet.presentation.ui.portfolio.components.Header
import com.aroman.mimwallet.presentation.ui.viewmodels.ThemeViewModel
import com.aroman.mimwallet.presentation.ui.viewmodels.WalletViewModel

@Composable
fun PortfolioScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    walletViewModel: WalletViewModel,
) {
    LaunchedEffect(true) {
        walletViewModel.getPortfolio()
    }
    val portfolioState by walletViewModel.portfolio.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val view = LocalView.current
            val resources = LocalContext.current.resources
            Header(
                themeViewModel = themeViewModel,
                walletViewModel = walletViewModel,
                navController = navController,
                isLoading = portfolioState.isLoading,
                onThemeChange = {
                    setScreenshot(view, resources)
                }
            )
            CoinContent(
                viewModel = walletViewModel,
                navController = navController,
                portfolio = portfolioState
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