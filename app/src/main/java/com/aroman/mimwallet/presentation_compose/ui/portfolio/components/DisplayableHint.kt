package com.aroman.mimwallet.presentation_compose.ui.portfolio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.R
import com.aroman.mimwallet.presentation_compose.ui.theme.Typography

@Composable
fun DisplayableHint() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Click button below to add your first coin to your portfolio",
            style = Typography.bodyMedium
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_downward_24),
            contentDescription = ""
        )
    }
}