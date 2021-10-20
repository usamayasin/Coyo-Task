package com.android.coyotask

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoyoApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
