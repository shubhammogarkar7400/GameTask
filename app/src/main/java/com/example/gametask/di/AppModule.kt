package com.example.gametask.di

import android.content.Context
import com.example.gametask.SoundManager
import com.example.gametask.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideAppPreferences(context: Context): AppPreferences {
        return AppPreferences(context)
    }

    @Provides
    @Singleton
    fun provideSoundManager(@ApplicationContext context: Context, appPreferences: AppPreferences): SoundManager {
        return SoundManager(context, appPreferences)
    }

}