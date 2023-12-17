package com.example.gametask

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gametask.enums.AppTheme
import com.example.gametask.ui.theme.GameTaskTheme
import com.example.gametask.enums.ScreenDestinations
import com.example.gametask.screens.AppLoadingAnimation
import com.example.gametask.screens.GamePlayingScreen
import com.example.gametask.screens.GameStartScreen
import com.example.gametask.screens.HomeScreen
import com.example.gametask.screens.MainViewModel
import com.example.gametask.screens.SettingScreen
import com.example.gametask.ui.theme.DarkColorScheme
import com.example.gametask.ui.theme.LightColorScheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var soundManager: SoundManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            val activityThemeState by viewModel.activityThemeState.collectAsStateWithLifecycle()

            if (activityThemeState.isLoading){
                GameTaskTheme {
                    val systemUiController = rememberSystemUiController()
                    systemUiController.isStatusBarVisible = false // Status bar
                    systemUiController.setStatusBarColor(color = Color.Black)

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppLoadingAnimation()

                        Spacer(modifier = Modifier.height(height = 16.dp))

                        Text(
                            text = "Game Task",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                }
            }else{
                val isDarkTheme = when(activityThemeState.currentTheme){
                    AppTheme.LIGHT.theme -> false
                    AppTheme.DARK.theme -> true
                    else -> isSystemInDarkTheme()
                }
                GameTaskTheme(isDarkTheme) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {



                        val systemUiController = rememberSystemUiController()
                        systemUiController.isStatusBarVisible = false // Status bar
                        systemUiController.setStatusBarColor(color = Color.Black)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // Code to be executed on Android 13 and above
                            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }

                        InterviewGameTask(viewModel = viewModel, soundManager)

                    }

                }

            }


         }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        soundManager.releaseMediaPlayerMusic()
    }
}



@Composable
fun InterviewGameTask(viewModel: MainViewModel, soundManager: SoundManager) {
    val navController = rememberNavController()
    val startDestination = ScreenDestinations.HOME_SCREEN.route

    NavHost(
        navController = navController,
        startDestination = ScreenDestinations.HOME_SCREEN.route
    ) {
        composable(route = startDestination) {
            HomeScreen(navController, viewModel, soundManager)
        }
        composable(ScreenDestinations.SETTING_SCREEN.route) {
            SettingScreen(navController, viewModel, soundManager)
        }
        composable(ScreenDestinations.GAME_START_SCREEN.route) {
            GameStartScreen(navController, viewModel, soundManager)
        }
        composable(ScreenDestinations.GAME_PLAYING_SCREEN.route) {
            GamePlayingScreen(navController, viewModel, soundManager)
        }
    }

}

