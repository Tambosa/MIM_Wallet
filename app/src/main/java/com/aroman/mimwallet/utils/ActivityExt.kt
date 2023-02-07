package com.aroman.mimwallet.utils

import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.disableTouch() {
    this.window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun FragmentActivity.enableTouch() {
    this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun FragmentActivity.showMessage(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}