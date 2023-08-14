package com.mariano.fitapp

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.wear.ambient.AmbientModeSupport
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.DataApi
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.mariano.fitapp.databinding.ActivityMainBinding

class MainActivity : Activity(), SensorEventListener, GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

    private lateinit var googleApiClient: GoogleApiClient

    private var activityContext: Context? = null
    private lateinit var binding: ActivityMainBinding

    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityContext = this

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (temperatureSensor == null) {
            // El sensor de temperatura no está disponible en este dispositivo
        }

        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addApi(Wearable.API)
            .build()

        googleApiClient.connect()

    }

    override fun onResume() {
        super.onResume()
        temperatureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {

            var temperature: String = event.values[0].toString() + "°"
            binding.temperatura.text = temperature

            sendDataToPhone("data_sensor", temperature)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // No es necesario implementar esto para el sensor de temperatura
    }

    override fun onConnected(p0: Bundle?) {
        Wearable.DataApi.addListener(googleApiClient, this)
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onDataChanged(p0: DataEventBuffer) {
        // Se llama cuando los datos han cambiado en el teléfono
    }

    private fun sendDataToPhone(dataKey: String, dataValue: String) {
        val dataMap = PutDataMapRequest.create("/sensor_readings").apply {
            dataMap.putString(dataKey, dataValue)
        }.asPutDataRequest()

        Wearable.DataApi.putDataItem(googleApiClient, dataMap)
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.DataApi.removeListener(googleApiClient, this)
        googleApiClient.disconnect()
    }
}