package com.example.gametask.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gametask.R
import com.example.gametask.SoundManager
import com.example.gametask.enums.AppTheme
import java.util.Calendar
import java.util.concurrent.TimeUnit

@Composable
fun SettingScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    soundManager: SoundManager
) {
    val context = LocalContext.current

    val sound by viewModel.isSoundEnabled.collectAsState(initial = true)
    val music by viewModel.isMusicEnabled.collectAsState(initial = true)
    val trainingReminder by viewModel.isTrainingReminderEnabled.collectAsState(initial = true)
    val trainingReminderTime by viewModel.trainingReminderTime.collectAsState(initial = 0)
    val appTheme by viewModel.appTheme.collectAsState(initial = AppTheme.LIGHT.theme)

    var expanded by remember { mutableStateOf(false) }

    val appThemes = listOf(AppTheme.SYSTEM.theme, AppTheme.LIGHT.theme, AppTheme.DARK.theme)

    val selectedTime by rememberSaveable { mutableStateOf(Calendar.getInstance()) }

    var showTimePickerDialog by remember { mutableStateOf(false) }

    var time by remember { mutableStateOf("00:00") }

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(all = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Privacy Policy", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(height = 16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sound")
                Switch(
                    modifier = Modifier.scale(scale = 0.7f),
                    checked = sound,
                    onCheckedChange = {
                        soundManager.playSound(R.raw.click)
                        viewModel.updateSoundEnabled(it)
                                      },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Green.copy(0.8f, red = 0.2f, green = 0.7f),
                        uncheckedThumbColor = Color.Gray,
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Music")
                Switch(
                    modifier = Modifier.scale(scale = 0.7f),
                    checked = music,
                    onCheckedChange = {
                        soundManager.playSound(R.raw.click)
                        viewModel.updateMusicEnabled(it)
                                      },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Green.copy(0.8f, red = 0.2f, green = 0.7f),
                        uncheckedThumbColor = Color.Gray,
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Training Reminder")
                Switch(
                    modifier = Modifier.scale(scale = 0.7f),
                    checked = trainingReminder,
                    onCheckedChange = {
                        soundManager.playSound(R.raw.click)
                        viewModel.updateTrainingReminderEnabled(it)
                                      },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Green.copy(0.8f, red = 0.2f, green = 0.7f),
                        uncheckedThumbColor = Color.Gray,
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.clickable {
                        soundManager.playSound(R.raw.click)
                        showTimePickerDialog = !showTimePickerDialog
                    }
                ) {
                     time = String.format("%02d:%02d",
                         TimeUnit.MILLISECONDS.toHours(trainingReminderTime),
                         TimeUnit.MILLISECONDS.toMinutes(trainingReminderTime) -
                                 TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(trainingReminderTime))
                     )
                    Text(text = time)
                    if (showTimePickerDialog){
                        val dialog = TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                selectedTime.set(Calendar.MINUTE, minute)
                                time = "$hourOfDay:$minute"
                                val millis = (hourOfDay.toLong() * 60 * 60 * 1000) + (minute + 60 * 1000)
                                showTimePickerDialog = false
                                viewModel.updateTrainingReminderTime(millis)
                            },
                            selectedTime.get(Calendar.HOUR_OF_DAY),
                            selectedTime.get(Calendar.MINUTE),
                            true
                        )
                        dialog.setOnDismissListener {
                            soundManager.playSound(R.raw.click)
                            showTimePickerDialog = false
                        }
                        dialog.show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(height = 32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Theme")
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            soundManager.playSound(R.raw.click)
                            expanded = !expanded
                        }
                    ) {
                        Text(text = appTheme, modifier = Modifier)
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(8.dp)
                    ) {
                        appThemes.forEach { theme ->
                            DropdownMenuItem(
                                text = { Text(theme, color = Color.White) },
                                onClick = {
                                    soundManager.playSound(R.raw.click)
                                    viewModel.updateTheme(theme = theme)
                                    expanded = !expanded
                                })
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            Text(text = "REMOVE ADS", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(height = 32.dp))
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Go Back",
                modifier = Modifier
                    .size(size = 50.dp)
                    .clickable {
                        soundManager.playSound(R.raw.click)
                        navController.popBackStack()
                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


