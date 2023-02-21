package com.aroman.mimwallet.presentation_compose.ui.navigation

sealed class Screen(val route: String) {
    object Portfolio : Screen("portfolio")
    object CoinDetails : Screen("coinDetails")

    fun withArg(arg: String): String {
        return buildString {
            append(route)
            append("/$arg")
        }
    }
}
