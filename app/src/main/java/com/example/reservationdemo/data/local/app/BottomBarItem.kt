package com.example.reservationdemo.data.local.app

import androidx.compose.ui.graphics.painter.Painter

data class BottomBarItem(
    val route: String,
    val icon: Painter,
    val selectedIcon: Painter,
    val label: String,
    val type : String = "normal"
)
