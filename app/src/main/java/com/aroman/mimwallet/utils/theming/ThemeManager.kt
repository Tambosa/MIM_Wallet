package com.aroman.mimwallet.utils.theming

import androidx.annotation.ColorRes
import com.aroman.mimwallet.R

object ThemeManager {

    private val listeners = mutableSetOf<ThemeChangedListener>()
    var theme = Theme.LIGHT
        set(value) {
            field = value
            listeners.forEach { listener -> listener.onThemeChanged(value) }
        }

    interface ThemeChangedListener {
        fun onThemeChanged(theme: Theme)
    }

    data class TextViewTheme(
        @ColorRes
        val textColor: Int
    )

    data class ViewGroupTheme(
        @ColorRes
        val backgroundColor: Int
    )

    data class BottomNavigationViewTheme(
        @ColorRes
        val backgroundColor: Int,
    )

    enum class Theme(
        val textViewTheme: TextViewTheme,
        val viewGroupTheme: ViewGroupTheme,
        val bottomNavigationViewTheme: BottomNavigationViewTheme,
    ) {
        DARK(
            textViewTheme = TextViewTheme(
                textColor = R.color.md_theme_dark_onPrimaryContainer
            ),
            viewGroupTheme = ViewGroupTheme(
                backgroundColor = R.color.md_theme_dark_primaryContainer
            ),
            bottomNavigationViewTheme = BottomNavigationViewTheme(
                backgroundColor = R.color.md_theme_dark_surfaceTint
            )
        ),
        LIGHT(
            textViewTheme = TextViewTheme(
                textColor = R.color.md_theme_light_onPrimaryContainer
            ),
            viewGroupTheme = ViewGroupTheme(
                backgroundColor = R.color.md_theme_light_primaryContainer
            ),
            bottomNavigationViewTheme = BottomNavigationViewTheme(
                backgroundColor = R.color.md_theme_light_surfaceTint
            )
        )
    }

    fun addListener(listener: ThemeChangedListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ThemeChangedListener) {
        listeners.remove(listener)
    }
}