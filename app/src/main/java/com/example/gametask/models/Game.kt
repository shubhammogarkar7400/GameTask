package com.example.gametask.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.PlayForWork
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gametask.enums.MyGames

data class Game(
    val name: String,
    val icon: ImageVector,
    val description: String
)


val games = listOf(
    Game(name = MyGames.CLOCK.value, icon = Icons.Default.Alarm, description = ""),
    Game(name = "", icon = Icons.Default.HourglassEmpty, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
    Game(name = "", icon = Icons.Default.PlayForWork, description = ""),
)