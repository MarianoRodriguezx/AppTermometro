@file:Suppress("DEPRECATION")

package com.mariano.fitapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.DataApi
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.mariano.fitapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

    private lateinit var googleApiClient: GoogleApiClient
    //lateinit var registers: MutableList<RegistersEntity>
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //registers = ArrayList()
        getRegisters()

        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addApi(Wearable.API)
            .build()

        googleApiClient.connect()

    }

    override fun onConnected(p0: Bundle?) {

        Wearable.DataApi.addListener(googleApiClient, this)

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onDataChanged(p0: DataEventBuffer) {
        // Se llama cuando los datos han cambiado en el reloj Wear OS
        Toast.makeText(this, "Cambiooo", Toast.LENGTH_LONG).show()
        for (event in p0) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if ("/sensor_readings" == path) {
                    // Este es el mismo path que usaste al enviar los datos desde Wear
                    val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                    val dataValue = dataMapItem.dataMap.getString("data_sensor")
                    binding.temperature.text = dataValue
                }
            }
        }
    }

    fun getRegisters(){
        //registers = App.database.registerDao().getAllRegisters()
    }
}