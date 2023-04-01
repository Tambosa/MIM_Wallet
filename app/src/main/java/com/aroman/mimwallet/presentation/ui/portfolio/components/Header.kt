package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.R
import com.aroman.mimwallet.presentation.Screen
import com.aroman.mimwallet.presentation.ui.theme.Typography
import com.aroman.mimwallet.presentation.ui.viewmodels.ThemeViewModel
import com.aroman.mimwallet.presentation.ui.viewmodels.WalletViewModel

@Composable
fun Header(
    themeViewModel: ThemeViewModel,
    walletViewModel: WalletViewModel,
    isLoading: Boolean,
    onThemeChange: () -> Unit,
    navController: NavController,
) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp, end = 12.dp, top = 20.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val angle by rememberInfiniteTransition().animateFloat(
                initialValue = 0F,
                targetValue = 360F,
                animationSpec = infiniteRepeatable(
                    animation = tween(500, easing = LinearEasing)
                )
            )
            Text(
                text = "Portfolio",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.titleLarge
            )
            IconButton(
                onClick = {
                    walletViewModel.getPortfolio()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_refresh_24),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "refresh data",
                    modifier = Modifier.graphicsLayer { if (isLoading) rotationZ = angle }
                )
            }
            IconButton(
                onClick = {
                    navController.navigate(Screen.PortfolioNotifications.route)
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_notifications_24),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "open notification screen",
                )
            }
            IconButton(
                onClick = {
                    themeViewModel.inverseTheme()
                    onThemeChange()
                }) {
                Icon(
                    painter = if (themeViewModel.isDarkTheme.collectAsState().value) painterResource(
                        id = R.drawable.ic_baseline_nights_stay_24
                    )
                    else painterResource(id = R.drawable.ic_baseline_wb_sunny_24),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "toggle dark mode",
                )
            }
        }
    }
}