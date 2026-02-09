package com.example.camvidgram.core.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CamvidgramApplciation : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}