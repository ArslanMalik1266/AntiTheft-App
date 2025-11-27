package com.example.antitheftapp.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.antitheftapp.R
import java.util.Objects

class HomeScreen : AppCompatActivity() {

    private lateinit var btnArm : Button
    private var isArmed = false
    private lateinit var sensorManager: SensorManager
    private var proximitySensor : Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnArm = findViewById(R.id.btnArm)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)



        btnArm.setOnClickListener {
            if (isArmed){
                isArmed = false
                btnArm.text = "START"
                sensorManager.unregisterListener(proximityListener)
            }else {
                isArmed = true
                btnArm.text = "STOP"
                sensorManager.registerListener(
                    proximityListener,
                    proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        }
    }

    private val proximityListener = object : SensorEventListener{
        override fun onSensorChanged(event: SensorEvent) {
            if (!isArmed) return
            val value = event.values[0]
            val max = proximitySensor?.maximumRange ?: 2f

            if (value>= max){
                Log.d("TAG", "Phone Snatched")
            }else{
                Log.d("TAG", "Phone still in pocket")
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

    }
}