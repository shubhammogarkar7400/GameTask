package com.example.gametask

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import com.example.gametask.preferences.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor( private val appContext: Context, appPreferences: AppPreferences) {

    private val musicPlayer = MediaPlayer.create(appContext, R.raw.music).also { it.isLooping = true }
    private val isMusicEnabled = appPreferences.isMusicEnabled
    private var musicEnabled = true

    private val isSoundEnabled = appPreferences.isSoundEnabled
    private var soundEnabled = true

    init {
        try {
            CoroutineScope(Dispatchers.Default).launch {
                isSoundEnabled.collectLatest { value ->
                    soundEnabled = value
                }
            }

            CoroutineScope(Dispatchers.Default).launch {
                isMusicEnabled.collectLatest { value ->
                    musicEnabled = value
                }
            }
        }catch (e: Exception){
            Log.e(SoundManager::class.simpleName, e.message.toString())
        }
    }
    fun playSound(resId: Int) {
        try {
            if (soundEnabled){
                val mediaPlayer = MediaPlayer.create(appContext, resId)
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener { mp ->
                    mp.release()
                }
            }
        }catch (e: Exception){
            Log.e(SoundManager::class.simpleName, e.message.toString())
        }
    }


    fun playMusic(){
        try {
            if (musicEnabled){
                musicPlayer.start()
            }
        }catch (e: Exception){
            Log.e(SoundManager::class.simpleName, e.message.toString())
        }
    }

    fun pauseMusic(){
        try {
            musicPlayer.pause()
        }catch (e: Exception){
            Log.e(SoundManager::class.simpleName, e.message.toString())
        }
    }

    fun releaseMediaPlayerMusic() {
        try {
            musicPlayer.stop()
            musicPlayer.release()
        }catch (e: Exception){
            Log.e(SoundManager::class.simpleName, e.message.toString())
        }
    }

}