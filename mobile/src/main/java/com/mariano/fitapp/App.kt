package com.mariano.fitapp

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appSingleton = this
        //database =  Room.databaseBuilder(this, RegistersDatabase::class.java, "registers-db").build()
    }

    companion object {
        //lateinit var database: RegistersDatabase

        lateinit var appSingleton: App

        fun getInstance(): App {
            return appSingleton
        }
    }
}