package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.domain.model.ui.PortfolioUiEvent
import com.aroman.mimwallet.presentation.ui.shared_compose_components.CircularChip
import com.aroman.mimwallet.presentation.ui.viewmodels.PortfolioViewModel

@Composable
fun TimePeriodSelection(
    onEvent: (PortfolioUiEvent) -> Unit, timePeriodSelection: PortfolioViewModel.TimePeriod
) {
    val timePeriodList = PortfolioViewModel.TimePeriod.values().asList()
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
                count = PortfolioViewModel.TimePeriod.values().size,
                itemContent = { index ->
                    CircularChip(
                        name = stringResource(id = timePeriodList[index].value),
                        isSelected = timePeriodList[index] == timePeriodSelection,
                        onSelectionChanged = {
                            onEvent(
                                PortfolioUiEvent.ChangeTimePeriod(
                                    timePeriodList[index]
                                )
                            )
                        })
                })
        }
    }
}