package com.aroman.mimwallet.presentation_compose.ui.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.presentation_compose.ui.shared_compose_components.CircularChip
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel

@Composable
fun TimePeriodSelection(
    walletViewModel: ComposeWalletViewModel,
    timePeriodSelection: ComposeWalletViewModel.TimePeriod
) {
    val timePeriodList = ComposeWalletViewModel.TimePeriod.values().asList()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(
                count = ComposeWalletViewModel.TimePeriod.values().size,
                itemContent = { index ->
                    CircularChip(
                        name = timePeriodList[index].value,
                        isSelected = timePeriodList[index] == timePeriodSelection,
                        onSelectionChanged = { walletViewModel.setTimePeriod(timePeriodList[index]) }
                    )
                }
            )
        }
    }
}