package com.florescer

import android.app.Application
import com.florescer.di.AppContainer

class MyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}