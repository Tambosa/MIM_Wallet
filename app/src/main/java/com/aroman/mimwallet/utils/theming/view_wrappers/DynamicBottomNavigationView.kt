package com.aroman.mimwallet.utils.theming.view_wrappers

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.aroman.mimwallet.utils.theming.ThemeManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class DynamicBottomNavigationView @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr),
    ThemeManager.ThemeChangedListener {

    override fun onFinishInflate() {
        super.onFinishInflate()
        ThemeManager.addListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ThemeManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ThemeManager.removeListener(this)
    }

    override fun onThemeChanged(theme: ThemeManager.Theme) {
        backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(
            context,
            theme.bottomNavigationViewTheme.backgroundColor
        ))
    }
}