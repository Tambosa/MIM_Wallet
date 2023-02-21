package com.aroman.mimwallet.presentation_compose.ui.navigation

sealed class Screen(val route: String) {
    object Portfolio : Screen("portfolio")
}
