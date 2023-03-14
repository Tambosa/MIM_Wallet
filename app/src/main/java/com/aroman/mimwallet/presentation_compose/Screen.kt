package com.aroman.mimwallet.presentation_compose

sealed class Screen(val route: String) {
    object Portfolio : Screen("portfolio")
    object CoinDetails : Screen("coinDetails")

    fun withArg(arg: Int): String {
        return buildString {
            append(route)
            append("/$arg")
        }
    }
}
