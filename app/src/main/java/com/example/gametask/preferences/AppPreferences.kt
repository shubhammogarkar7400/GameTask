package com.example.gametask.preferences

 import android.content.Context
 import androidx.datastore.core.DataStore
 import androidx.datastore.preferences.core.Preferences
 import androidx.datastore.preferences.core.booleanPreferencesKey
 import androidx.datastore.preferences.core.edit
 import androidx.datastore.preferences.core.intPreferencesKey
 import androidx.datastore.preferences.core.longPreferencesKey
 import androidx.datastore.preferences.core.stringPreferencesKey
 import androidx.datastore.preferences.preferencesDataStore
 import com.example.gametask.enums.AppTheme
 import dagger.hilt.android.qualifiers.ApplicationContext
 import kotlinx.coroutines.flow.Flow
 import kotlinx.coroutines.flow.map
 import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppPreferences.APP_PREFERENCES)

class AppPreferences @Inject constructor(appContext: Context)  {

    private val dataStore = appContext.dataStore

    suspend fun updateSoundEnabled(isEnabled: Boolean) {
        dataStore.edit { settings ->
            settings[SOUND] = isEnabled
        }
    }
    val isSoundEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[SOUND] ?: true
        }

    suspend fun updateMusicEnabled(isEnabled: Boolean) {
        dataStore.edit { settings ->
            settings[MUSIC] = isEnabled
        }
    }
    val isMusicEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[MUSIC] ?: true
        }

    suspend fun updateTrainingReminderEnabled(isEnabled: Boolean) {
        dataStore.edit { settings ->
            settings[TRAINING_REMINDER] = isEnabled
        }
    }
    val isTrainingReminderEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[TRAINING_REMINDER] ?: true
        }

    suspend fun updateTrainingReminderTime(millis: Long) {
        dataStore.edit { settings ->
            settings[TRAINING_REMINDER_TIME] = millis
        }
    }
    val getTrainingReminderTime: Flow<Long> = dataStore.data
        .map { preferences ->
            preferences[TRAINING_REMINDER_TIME] ?: 0
        }

    suspend fun updateAppTheme(theme: String) {
        dataStore.edit { settings ->
            settings[APP_THEME] = theme
        }
    }
    val getAppTheme: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[APP_THEME] ?: AppTheme.SYSTEM.theme
        }


    suspend fun updateGameLevel(keyGameName: String, currentLevel: Int) {
        val key = intPreferencesKey(keyGameName+"GameLevel")
        dataStore.edit { settings ->
            settings[key] = currentLevel
        }
    }
    fun getCurrentGameLevel(keyGameName: String): Flow<Int> {
        val key = intPreferencesKey(keyGameName+"GameLevel")
        return dataStore.data
            .map { preferences ->
                preferences[key] ?: 1
            }
    }

    suspend fun updateGameCurrentHighScore(keyGameName: String, highScore: Int) {
        val key = intPreferencesKey(keyGameName+"GameHighScore")
        dataStore.edit { settings ->
            settings[key] = highScore
        }
    }
    fun getGameCurrentHighScore(keyGameName: String): Flow<Int> {
        val key = intPreferencesKey(keyGameName+"GameHighScore")
        return dataStore.data
            .map { preferences ->
                preferences[key] ?: 0
            }
    }


    val isDarkThe: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[APP_THEMEee] ?: true
        }


    companion object {
        const val APP_PREFERENCES = "app_preferences"

        private val SOUND = booleanPreferencesKey("sound")
        private val MUSIC = booleanPreferencesKey("Music")
        private val TRAINING_REMINDER = booleanPreferencesKey("training_reminder")
        private val TRAINING_REMINDER_TIME = longPreferencesKey("training_reminder_time")
        private val APP_THEME = stringPreferencesKey("app_theme")
        private val APP_THEMEee = booleanPreferencesKey("app_themeee")
    }
}