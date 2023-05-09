package com.aroman.mimwallet.presentation

sealed class Screen(val route: String) {
    object Portfolio : Screen("portfolio")
    object CoinInsert : Screen("coinDetails")
    object PortfolioNotifications : Screen("portfolioAlarm")

    fun withArg(arg: Int): String {
        return buildString {
            append(route)
            append("/$arg")
        }
    }
}
