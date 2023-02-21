package com.aroman.mimwallet.presentation_compose.ui.portfolio_screen.compose_children

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
            .padding(8.dp),
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

@Composable
fun CircularChip(
    name: String = "Chip",
    isSelected: Boolean = false,
    selectedColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    defaultColor: Color = MaterialTheme.colorScheme.onPrimary,
    onSelectionChanged: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) selectedColor else defaultColor)
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(name)
                }
            )
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}