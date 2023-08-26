package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.ui.PortfolioUiEvent
import com.example.core_ui.theme.spacing

@Composable
fun EditCoinCountLandscapeDialog(
    onDismissRequest: () -> Unit,
    clickedCoin: DisplayableCoin,
    oldCount: Double,
    onEvent: (PortfolioUiEvent) -> Unit
) {
    var saveEnabled by remember { mutableStateOf(true) }
    var newCount by remember { mutableStateOf(oldCount.toBigDecimal().toPlainString()) }
    saveEnabled = try {
        newCount.toDouble() != 0.0
    } catch (e: Exception) {
        false
    }
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = clickedCoin.name) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = newCount,
                    onValueChange = { newCount = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.padding(MaterialTheme.spacing.paddingExtraSmall),
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
                modifier = Modifier.padding(MaterialTheme.spacing.paddingExtraSmall),
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
        })
}