package com.aroman.mimwallet.presentation.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(getTheme())
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun inverseTheme() {
        sharedPreferences.edit().putBoolean(
            IS_DARK_THEME,
            !(getTheme())
        ).apply()

        //TODO emit???
        viewModelScope.launch {
            _isDarkTheme.emit(getTheme())
        }
    }

    private fun getTheme() = sharedPreferences.getBoolean(IS_DARK_THEME, false)

    companion object {
        const val IS_DARK_THEME = "is night mode"
    }
}