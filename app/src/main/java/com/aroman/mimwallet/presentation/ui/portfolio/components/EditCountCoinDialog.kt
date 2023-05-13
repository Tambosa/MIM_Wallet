package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.ui.PortfolioUiEvent
import com.aroman.mimwallet.presentation.ui.shared_compose_components.RoundedButton

@Composable
fun EditCoinCountDialog(
    onDismissRequest: () -> Unit,
    clickedCoin: DisplayableCoin,
    oldCount: Double,
    onEvent: (PortfolioUiEvent) -> Unit,
) {
    var saveEnabled by remember { mutableStateOf(true) }
    var newCount by remember { mutableStateOf(oldCount.toBigDecimal().toPlainString()) }
    saveEnabled = newCount.toDouble() != 0.0
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = clickedCoin.name) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleLarge,
                    text = newCount
                )
                val buttonModifier =
                    Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                val buttonSpacing = 15.dp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    RoundedButton(symbol = "1", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "1" else newCount + "1"
                    }
                    RoundedButton(symbol = "2", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "2" else newCount + "2"
                    }
                    RoundedButton(symbol = "3", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "3" else newCount + "3"
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    RoundedButton(symbol = "4", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "4" else newCount + "4"
                    }
                    RoundedButton(symbol = "5", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "5" else newCount + "5"
                    }
                    RoundedButton(symbol = "6", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "6" else newCount + "6"
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    RoundedButton(symbol = "7", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "7" else newCount + "7"
                    }
                    RoundedButton(symbol = "8", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "8" else newCount + "8"
                    }
                    RoundedButton(symbol = "9", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "9" else newCount + "9"
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    RoundedButton(symbol = ".", modifier = buttonModifier) {
                        if (!newCount.contains(".")) newCount += "."
                    }
                    RoundedButton(symbol = "0", modifier = buttonModifier) {
                        if (newCount != "0") newCount += "0"
                    }
                    RoundedButton(
                        symbol = "C",
                        modifier = buttonModifier,
                        color = (MaterialTheme.colorScheme.tertiaryContainer),
                        textColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        newCount = if (newCount.length == 1) "0" else newCount.dropLast(1)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    clickedCoin.count = newCount.toDouble()
                    onEvent(PortfolioUiEvent.UpdateCoin(clickedCoin))
                    onDismissRequest()
                },
                enabled = saveEnabled
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    onEvent(PortfolioUiEvent.DeleteCoin(clickedCoin))
                    onDismissRequest()
                },
            ) {
                Text(
                    text = stringResource(id = R.string.delete),
                    color = Color.Red
                )
            }
        }
    )
}