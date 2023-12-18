package com.example.gametask.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.gametask.R
import com.example.gametask.SoundManager
import com.example.gametask.utils.Constants.HOURGLASS_LEVEL_SCORE_VALUE
import com.example.gametask.utils.Constants.HOURGLASS_SCORE_INCREMENT_VALUE
import com.example.gametask.utils.generateResultOption
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun HourglassGamePlayingScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    soundManager: SoundManager
) {

    val currentScore by viewModel.getGameCurrentScore(viewModel.currentGame!!.name).collectAsState(initial = 0)
    val bestScore by viewModel.getGameCurrentHighScore(viewModel.currentGame!!.name).collectAsState(initial = 0)
    val currentGameLevel by viewModel.getCurrentGameLevel(viewModel.currentGame!!.name).collectAsState(initial = 0)

    var score by remember { mutableIntStateOf(0) }

    var isGameCompleted by remember { mutableStateOf(false) }
    var isGameWon by remember { mutableStateOf(false) }

    var firstNum by remember { mutableIntStateOf(-1) }
    var secondNum by remember { mutableIntStateOf(-1) }

    var operationType by remember { mutableStateOf("+") }

    var selectedOption by remember { mutableIntStateOf(-1) }

    var result by remember { mutableIntStateOf(-1) }

    var availableSecond by remember { mutableIntStateOf(30) }

    var option1 by remember { mutableIntStateOf(-1) }
    var option2 by remember { mutableIntStateOf(-1) }
    var option3 by remember { mutableIntStateOf(-1) }
    var option4 by remember { mutableIntStateOf(-1) }

    var successfulStepCount by remember { mutableIntStateOf(1) }

    var isWrongAnswerSelected by remember { mutableStateOf(false) }

    var correctAnswerCount by remember { mutableIntStateOf(0) }


    DisposableEffect(key1 = Unit, effect = {
        soundManager.playMusic()
        onDispose {
            soundManager.pauseMusic()
        }
    })

    fun generateRandomTwoNumber() {
        val random = Random
        firstNum = random.nextInt(1, 31)
        secondNum = random.nextInt(1, 31)

        // Ensure that the two numbers are different
        while (firstNum == secondNum) {
            secondNum = random.nextInt(1, 31)
        }
    }


    fun getResult(){
        result = if (firstNum > secondNum){
            operationType = "-"
            firstNum - secondNum
        }else{
            if (secondNum % firstNum == 0){
                operationType = "/"
                secondNum / firstNum
            }else{
                if (firstNum > 10 || secondNum > 10){
                    operationType = "+"
                    firstNum + secondNum
                }else{
                    operationType = "*"
                    firstNum * secondNum
                }
            }
        }
    }

    // LaunchedEffect to run the logic every second
    LaunchedEffect(true) {
        selectedOption = -1
        generateRandomTwoNumber()
        getResult()
        val opns = generateResultOption(result)
        option1 = opns[0]
        option2 = opns[1]
        option3 = opns[2]
        option4 = opns[3]

        while (true) {
            // Simulate a condition being met

            if (availableSecond == 0) {
                println("Condition met. Breaking loop.")
                isGameCompleted = true
                if (bestScore < score){
                    viewModel.updateGameCurrentHighScore(viewModel.currentGame!!.name, score)
                }
                if (HOURGLASS_LEVEL_SCORE_VALUE <= (score + currentScore)){
                    soundManager.playSound(R.raw.game_level_complete)
                    viewModel.updateGameLevel(viewModel.currentGame!!.name, currentLevel = (currentGameLevel + 1))
                    viewModel.updateGameCurrentScore(viewModel.currentGame!!.name, 0)
                    isGameWon = true
                }else{
                    soundManager.playSound(R.raw.error)
                    viewModel.updateGameCurrentScore(viewModel.currentGame!!.name, currentScore + score)
                }
                break
            }else{
                availableSecond--
            }

            if (selectedOption != -1 && selectedOption != result){
                selectedOption = -1
                soundManager.playSound(R.raw.error)
                successfulStepCount = 1
                correctAnswerCount = 0
                isWrongAnswerSelected = true
            }else if (result == selectedOption){
                selectedOption = -1
                generateRandomTwoNumber()
                getResult()
                val options = generateResultOption(result)
                option1 = options[0]
                option2 = options[1]
                option3 = options[2]
                option4 = options[3]

                if (isWrongAnswerSelected){
                    isWrongAnswerSelected = false
                }else{
                    correctAnswerCount++
                     if (correctAnswerCount == 4){
                        correctAnswerCount = 1
                         successfulStepCount++
                         score += (successfulStepCount * HOURGLASS_SCORE_INCREMENT_VALUE)
                    }else{
                         score += 20
                    }
                }
            }
            if (availableSecond < 5){
                soundManager.playSound(R.raw.warning)
            }else{
                soundManager.playSound(R.raw.timer)
            }
            // Delay for one second
            delay(1000)
        }

    }


    Scaffold { paddingValues ->
        if (isGameCompleted){
            soundManager.pauseMusic()
            if (isGameWon){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(all = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Icon(imageVector = Icons.Default.Stairs, modifier = Modifier.size(size = 50.dp), contentDescription = null)
                    Text(
                        text = "Congratulations",
                        style = MaterialTheme.typography.headlineLarge)
                    Text(
                        text = "You have completed level ${currentGameLevel - 1}",
                        style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(height = 48.dp))
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
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(all = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(text = "Score")
                            Text(text = score.toString(), style = MaterialTheme.typography.headlineLarge)
                        }
                        Icon(imageVector = Icons.Default.Stairs, contentDescription = null)
                        Column {
                            Text(text = "Best Score")
                            Text(text = bestScore.toString(), style = MaterialTheme.typography.headlineLarge)
                        }
                    }
                    Spacer(modifier = Modifier.height(height = 60.dp))
                    Text(
                        text = if (firstNum > secondNum) String.format("%s $operationType %s = ?", firstNum, secondNum) else String.format("%s $operationType %s = ?", secondNum, firstNum),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Red
                    )

                    Spacer(modifier = Modifier.height(height = 32.dp))

                    Text(text = if (selectedOption == -1) "Your answer: ?" else "Your answer: $selectedOption",
                        color = Color.Red, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    Text(text = "Right answer: $result", color = Color.Green, modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(height = 48.dp))
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
        }else{
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .padding(paddingValues = paddingValues)
            ){
                val (topChild, centerChild, bottomChild) = createRefs()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(topChild) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val (clock, level, tik) = createRefs()

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .constrainAs(clock) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                modifier = Modifier.size(size = 40.dp),
                                contentDescription = null)
                            Text(
                                text = availableSecond.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = if (availableSecond < 4) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                            )
                        }


                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .constrainAs(level) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(imageVector = Icons.Default.Stairs,  modifier = Modifier.size(size = 40.dp), contentDescription = null)
                                Text(text = score.toString(),  style = MaterialTheme.typography.headlineLarge,)
                            }
                            Text(text = if(successfulStepCount > 1) (successfulStepCount * HOURGLASS_SCORE_INCREMENT_VALUE).toString()+"+" else "",  style = MaterialTheme.typography.headlineMedium,)
                        }


                        Row(
                            modifier = Modifier
                                .width(width = 150.dp)
                                .constrainAs(tik) {
                                    top.linkTo(level.bottom, margin = 40.dp)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(imageVector = Icons.Default.CloudDone,  contentDescription = null,
                                tint = if (correctAnswerCount >= 1) Color.Green.copy(0.8f, red = 0.2f, green = 0.7f) else MaterialTheme.colorScheme.onBackground
                            )
                            Icon(imageVector = Icons.Default.CloudDone, contentDescription = null,
                                tint = if (correctAnswerCount >= 2) Color.Green.copy(0.8f, red = 0.2f, green = 0.7f) else MaterialTheme.colorScheme.onBackground
                            )
                            Icon(imageVector = Icons.Default.CloudDone, contentDescription = null,
                                tint = if (correctAnswerCount >= 3) Color.Green.copy(0.8f, red = 0.2f, green = 0.7f) else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                Text(
                    text = if (firstNum > secondNum) String.format("%s $operationType %s = ?", firstNum, secondNum) else String.format("%s $operationType %s = ?", secondNum, firstNum),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(centerChild) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(bottomChild) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(height = 50.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                                .clickable {
                                    selectedOption = option1
                                    if (result == option1) {
                                        soundManager.playSound(R.raw.positive)
                                    } else {
                                        soundManager.playSound(R.raw.error)
                                    }
                                }
                        ) {
                            Text(
                                text = option1.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary)
                        }
                        Spacer(modifier = Modifier.width(width = 12.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(height = 50.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                                .clickable {
                                    selectedOption = option2
                                    if (result == option2) {
                                        soundManager.playSound(R.raw.positive)
                                    } else {
                                        soundManager.playSound(R.raw.error)
                                    }
                                }
                        ) {
                            Text(
                                text = option2.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(height = 12.dp))

                    Row(
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(height = 50.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                                .clickable {
                                    selectedOption = option3
                                    if (result == option3) {
                                        soundManager.playSound(R.raw.positive)
                                    } else {
                                        soundManager.playSound(R.raw.error)
                                    }
                                }
                        ) {
                            Text(
                                text = option3.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary)
                        }
                        Spacer(modifier = Modifier.width(width = 12.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(height = 50.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                                .clickable {
                                    selectedOption = option4
                                    if (result == option4) {
                                        soundManager.playSound(R.raw.positive)
                                    } else {
                                        soundManager.playSound(R.raw.error)
                                    }
                                }
                        ) {
                            Text(
                                text = option4.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

            }

        }

    }

}