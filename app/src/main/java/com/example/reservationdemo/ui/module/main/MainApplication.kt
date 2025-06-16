package com.example.reservationdemo.ui.module.main

import android.app.Application
import com.example.reservationdemo.di.myAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(myAppModules)
        }
    }
}