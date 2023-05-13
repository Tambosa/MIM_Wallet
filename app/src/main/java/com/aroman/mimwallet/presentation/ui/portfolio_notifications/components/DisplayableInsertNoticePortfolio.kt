package com.aroman.mimwallet.presentation.ui.portfolio_notifications.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.domain.model.ui.NoticePortfolioUiEvent

@Composable
fun DisplayableInsertNoticePortfolio(
    onEvent: (NoticePortfolioUiEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {
            onEvent(
                NoticePortfolioUiEvent.AddItem(
                    NoticePortfolio(
                        hour = 12,
                        minute = 0,
                        isActive = false
                    )
                )
            )
        }) {
            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.add_coin)
            )
        }
    }
}