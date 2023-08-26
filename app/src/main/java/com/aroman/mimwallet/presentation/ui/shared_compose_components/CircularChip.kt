package com.aroman.mimwallet.presentation.ui.shared_compose_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core_ui.theme.spacing

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
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.spacing.paddingMedium,
                    vertical = MaterialTheme.spacing.paddingExtraSmall
                )
            )
        }
    }
}