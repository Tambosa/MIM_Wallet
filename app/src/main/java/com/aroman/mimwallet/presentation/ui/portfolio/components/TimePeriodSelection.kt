package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.presentation.ui.shared_compose_components.CircularChip
import com.aroman.mimwallet.presentation.ui.viewmodels.WalletViewModel

@Composable
fun TimePeriodSelection(
    walletViewModel: WalletViewModel,
    timePeriodSelection: WalletViewModel.TimePeriod
) {
    val timePeriodList = WalletViewModel.TimePeriod.values().asList()
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
                count = WalletViewModel.TimePeriod.values().size,
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