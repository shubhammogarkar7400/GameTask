package com.example.gametask.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.gametask.R
import com.example.gametask.SoundManager
import com.example.gametask.enums.ScreenDestinations
import com.example.gametask.models.games
import com.example.gametask.utils.Constants.APP_URL

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    soundManager: SoundManager
) {
    val context = LocalContext.current

    val trainingReminder by viewModel.isTrainingReminderEnabled.collectAsState(initial = true)
    val trainingReminderTime by viewModel.trainingReminderTime.collectAsState(initial = 0)

    var showRateAppDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = trainingReminder, trainingReminderTime, block = {
        viewModel.setNotificationReminder(trainingReminder, trainingReminderTime)
    })

    fun shareLink() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, APP_URL)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
    Scaffold(
        topBar = {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ){
                val (firstChild, secondChild, thirdChild) = createRefs()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.constrainAs(firstChild) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Download, 
                        contentDescription = "Download other apps", 
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            soundManager.playSound(R.raw.click)
                            Toast.makeText(context, "Not Implemented.", Toast.LENGTH_SHORT).show()
                        }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share this app",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            soundManager.playSound(R.raw.click)
                            shareLink()
                        }
                    )
                }
                Icon(
                    imageVector = Icons.Default.StarRate,
                    contentDescription = "Rate this app on google play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            soundManager.playSound(R.raw.click)
                            showRateAppDialog = true
                        }
                        .size(50.dp)
                        .constrainAs(secondChild) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Go to setting screen",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .constrainAs(thirdChild) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                        .clickable {
                            soundManager.playSound(R.raw.click)
                            navController.navigate(ScreenDestinations.SETTING_SCREEN.route)
                        })

            }
        }
    ) { padding ->

        LazyVerticalGrid(
            modifier = Modifier
                .padding(paddingValues = padding)
                .padding(horizontal = 32.dp, vertical = 8.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(games.size) { index ->
                    val game = games[index]
                    Box( contentAlignment = Alignment.Center,) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(70.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .clickable {
                                    soundManager.playSound(R.raw.click)
                                    viewModel.currentGame = game
                                    navController.navigate(ScreenDestinations.GAME_START_SCREEN.route)
                                }
                        ) {
                            Icon(
                                imageVector = game.icon,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        )

        if (showRateAppDialog){
            RateAppDialog(){
                soundManager.playSound(R.raw.click)
                showRateAppDialog = false
            }
        }
    }
}

@Composable
fun RateAppDialog(onCancelClick: () -> Unit){
    val context = LocalContext.current
    Dialog(onDismissRequest = { onCancelClick() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .padding(all = 12.dp)) {
            Text(text = "Rate our app, please", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
            Image(
                painter = painterResource(id = R.drawable.rating),
                alignment = Alignment.Center, contentDescription = null,
                modifier = Modifier.clickable {
                    val uri = Uri.parse(APP_URL)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                    onCancelClick()
                }
            )
             Text(text = "CANCEL", modifier = Modifier
                 .fillMaxWidth()
                 .clickable { onCancelClick() },textAlign = TextAlign.Start, color = MaterialTheme.colorScheme.primary)
        }
    }
}