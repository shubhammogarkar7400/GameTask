package com.example.gametask.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gametask.enums.MyGames
import com.example.gametask.models.Game
import com.example.gametask.models.ThemeState
import com.example.gametask.preferences.AppPreferences
import com.example.gametask.worker.TrainingReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val appContext: Context
) : ViewModel() {
    var currentGame: Game? = null

    val isSoundEnabled: Flow<Boolean> = appPreferences.isSoundEnabled
    val isMusicEnabled: Flow<Boolean> = appPreferences.isMusicEnabled
    val isTrainingReminderEnabled: Flow<Boolean> = appPreferences.isTrainingReminderEnabled
    val trainingReminderTime: Flow<Long> = appPreferences.getTrainingReminderTime
    val appTheme: Flow<String> = appPreferences.getAppTheme

    private val themeState = MutableStateFlow(value = ThemeState())

    val activityThemeState = themeState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = themeState.value
    )

    init {
        watchAppTheme()
    }

    private fun watchAppTheme() {
        viewModelScope.launch {
            themeState.update { it.copy(isLoading = true) }
            delay(timeMillis = 1000)

            appPreferences.getAppTheme.collectLatest { theme ->
                themeState.update { state ->
                    state.copy(
                        isLoading = false,
                        currentTheme = theme
                    )
                }
            }
        }
    }

    fun updateSoundEnabled(soundEnabled: Boolean) {
        viewModelScope.launch {
            appPreferences.updateSoundEnabled(soundEnabled)
        }
    }

    fun updateMusicEnabled(musicEnabled: Boolean) {
        viewModelScope.launch {
            appPreferences.updateMusicEnabled(musicEnabled)
        }
    }

    fun updateTrainingReminderEnabled(trainingReminderEnabled: Boolean) {
        viewModelScope.launch {
            appPreferences.updateTrainingReminderEnabled(trainingReminderEnabled)
        }
    }

    fun updateTrainingReminderTime(millis: Long) {
        viewModelScope.launch {
            appPreferences.updateTrainingReminderTime(millis)
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            appPreferences.updateAppTheme(theme)
        }
    }

    fun updateGameLevel(keyGameName: String, currentLevel: Int) {
        viewModelScope.launch {
            appPreferences.updateGameLevel(keyGameName = keyGameName, currentLevel = currentLevel)
        }
    }
    fun getCurrentGameLevel(keyGameName: String): Flow<Int> = appPreferences.getCurrentGameLevel(keyGameName = keyGameName)

    fun updateGameCurrentHighScore(keyGameName: String, highScore: Int) {
        viewModelScope.launch {
            appPreferences.updateGameCurrentHighScore(keyGameName = keyGameName, highScore = highScore)
        }
    }
    fun getGameCurrentHighScore(keyGameName: String) : Flow<Int> = appPreferences.getGameCurrentHighScore(keyGameName = keyGameName)


    fun updateGameCurrentScore(keyGameName: String, currentScore: Int) {
        viewModelScope.launch {
            appPreferences.updateGameCurrentScore(keyGameName = keyGameName, currentScore = currentScore)
        }
    }
    fun getGameCurrentScore(keyGameName: String) : Flow<Int> = appPreferences.getGameCurrentScore(keyGameName = keyGameName)




    fun setNotificationReminder(trainingReminder: Boolean, trainingReminderTime: Long) {
        if (trainingReminder){
            val constraints = Constraints.Builder()
                .setRequiresCharging(false)
                .build()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<TrainingReminderWorker>(
                repeatInterval = trainingReminderTime, // Repeat interval in minutes
                repeatIntervalTimeUnit = TimeUnit.MICROSECONDS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
                TRAINING_REMINDER_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }else{
            WorkManager.getInstance(appContext).cancelUniqueWork(TRAINING_REMINDER_WORKER)
        }

    }



    companion object {
        private const val TRAINING_REMINDER_WORKER = "training reminder worker"
    }
}
