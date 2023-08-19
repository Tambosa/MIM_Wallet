package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.R
import com.aroman.mimwallet.presentation.Screen
import com.aroman.mimwallet.presentation.ui.theme.Typography

@Composable
fun TopAppBarContent(
    onThemeChange: () -> Unit,
    navController: NavController,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp, end = 12.dp
            ), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.portfolio),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.titleLarge
        )
        IconButton(onClick = {
            navController.navigate(Screen.PortfolioNotifications.route)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_notifications_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(id = R.string.open_notification_screen),
            )
        }
        IconButton(onClick = { onThemeChange() }) {
            Icon(
                painter = if (isSystemInDarkTheme()) painterResource(
                    id = R.drawable.ic_baseline_nights_stay_24
                )
                else painterResource(id = R.drawable.ic_baseline_wb_sunny_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(id = R.string.enable_dark_mode),
            )
        }
    }
}