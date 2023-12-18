package com.example.gametask.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gametask.R
import com.example.gametask.SoundManager
import com.example.gametask.enums.MyGames
import com.example.gametask.enums.ScreenDestinations
import com.example.gametask.utils.Constants.CLOCK_LEVEL_INCREMENT_MULTIPLE_VALUE
import com.example.gametask.utils.Constants.HOURGLASS_LEVEL_SCORE_VALUE

@Composable
fun GameStartScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    soundManager: SoundManager
) {
    val context = LocalContext.current

    val currentScore by viewModel.getGameCurrentScore(viewModel.currentGame!!.name).collectAsState(initial = 0)

    val bestScore by viewModel.getGameCurrentHighScore(viewModel.currentGame!!.name).collectAsState(initial = 0)

    val currentGameLevel by viewModel.getCurrentGameLevel(viewModel.currentGame!!.name).collectAsState(initial = 1)




    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(all = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
                        soundManager.playSound(R.raw.click)
                        navController.popBackStack()
                    }
                )
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh game level",
                    modifier = Modifier.clickable {
                        soundManager.playSound(R.raw.click)
                        viewModel.updateGameLevel(viewModel.currentGame!!.name,  1)
                    }
                )
            }
         }
    ) { paddingValues ->
        if (viewModel.currentGame != null && viewModel.currentGame?.name?.isNotEmpty() == true){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(paddingValues = paddingValues) .padding(all = 12.dp)
                    .fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Stairs,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Level $currentGameLevel",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(32.dp))
                if (viewModel.currentGame!!.name == MyGames.CLOCK.value){
                    Text(
                        text = "Score: $bestScore/${CLOCK_LEVEL_INCREMENT_MULTIPLE_VALUE * currentGameLevel}",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = bestScore / (currentGameLevel.toFloat() * CLOCK_LEVEL_INCREMENT_MULTIPLE_VALUE),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 16.dp),
                        color = Color.Green.copy(0.8f, red = 0.2f, green = 0.7f)
                    )
                } else if (viewModel.currentGame!!.name == MyGames.HOURGLASS.value) {
                    Text(
                        text = "Score: $currentScore/$HOURGLASS_LEVEL_SCORE_VALUE",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = currentScore / HOURGLASS_LEVEL_SCORE_VALUE.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 16.dp),
                        color = Color.Green.copy(0.8f, red = 0.2f, green = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            soundManager.playSound(R.raw.click)
                            when(viewModel.currentGame!!.name){
                                MyGames.CLOCK.value -> {
                                    navController.navigate(ScreenDestinations.CLOCK_GAME_PLAYING_SCREEN.route)
                                }
                                MyGames.HOURGLASS.value -> {
                                    navController.navigate(ScreenDestinations.HOURGLASS_GAME_PLAYING_SCREEN.route)
                                }
                            }
                        }
                )

            }
        }else{
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .fillMaxSize()
            ){
                Text(text = "Coming Soon...")
                Text(text = "Level $currentGameLevel")
            }
        }

    }
}