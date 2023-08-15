package com.mariano.fitapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appSingleton = this
    }

    fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences("bucket", Context.MODE_PRIVATE)
    }

    companion object {

        lateinit var appSingleton: App

        fun getInstance(): App {
            return appSingleton
        }
    }
}