package com.aroman.mimwallet.presentation_compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.presentation_compose.ui.theme.AppTheme
import com.aroman.mimwallet.presentation_compose.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    private val walletViewModel by viewModels<ComposeWalletViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val portfolioState by walletViewModel.portfolio.collectAsState()

            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Header()
                        when (portfolioState) {
                            is ViewState.Success -> {
                                TotalPrice(portfolioState.data!!)
                            }
                            is ViewState.Loading -> {}
                            is ViewState.Error -> {}
                        }
                    }
                }
            }

            walletViewModel.getPortfolio()
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp, end = 12.dp, top = 20.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val context = LocalContext.current
        Text(
            text = "Portfolio",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.titleLarge
        )
        IconButton(
            onClick = {
                Toast.makeText(context, "Night mode clicked", Toast.LENGTH_SHORT)
                    .show()
            }) {
            Icon(
                painter = if (!isSystemInDarkTheme()) painterResource(id = R.drawable.ic_baseline_nights_stay_24)
                else painterResource(id = R.drawable.ic_baseline_wb_sunny_24),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = "toggle dark mode"
            )
        }
    }
}

@Composable
fun TotalPrice(portfolio: Portfolio) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = String.format("$%.2f", portfolio.totalPrice),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.titleMedium,
            maxLines = 1,
        )

        val percentFormat = DecimalFormat("0.##'%'")
        percentFormat.roundingMode = RoundingMode.CEILING
        Text(
            text = percentFormat.format(portfolio.totalPercentChange24h),
            color = if (portfolio.totalPercentChange24h > 0) Color(android.graphics.Color.GREEN)
            else Color(android.graphics.Color.RED),
            style = Typography.titleMedium,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Surface(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Header()
        }
    }
}