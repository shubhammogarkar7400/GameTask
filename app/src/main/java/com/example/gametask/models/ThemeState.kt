package com.example.gametask.models

import com.example.gametask.enums.AppTheme

data class ThemeState(
    val isLoading: Boolean = true,
    val currentTheme: String = AppTheme.SYSTEM.theme
)
