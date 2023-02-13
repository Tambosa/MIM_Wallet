package com.aroman.mimwallet.utils.theming

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.aroman.mimwallet.utils.theming.view_wrappers.*

class DynamicLayoutInflater (
    private val delegate: AppCompatDelegate
) : LayoutInflater.Factory2 {

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return when (name) {
            "androidx.appcompat.widget.AppCompatTextView" -> DynamicAppCompatTextView(context, attrs)
            "androidx.appcompat.widget.AppCompatImageButton" -> DynamicAppCompatImageButton(context, attrs)
            "ConstraintLayout" -> DynamicConstraintLayout(context, attrs)
            "FrameLayout" -> DynamicFrameLayout(context, attrs)
            "com.google.android.material.chip.Chip" -> DynamicChip(context, attrs)
            else -> delegate.createView(parent, name, context, attrs)
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }
}