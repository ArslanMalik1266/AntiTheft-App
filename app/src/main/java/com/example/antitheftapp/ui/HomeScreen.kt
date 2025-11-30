package com.example.antitheftapp.ui

import android.content.Context
import android.content.Intent
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
import com.example.antitheftapp.service.AntiTheftService
import java.util.Objects

class HomeScreen : AppCompatActivity() {

    private lateinit var btnArm : Button
    private var isArmed = false


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


        btnArm.setOnClickListener {
            if (isArmed){
                isArmed = false
                btnArm.text = "START"
              stopService(Intent(this, AntiTheftService::class.java))
            }else {
                isArmed = true
                btnArm.text = "STOP"
              startForegroundService(Intent(this, AntiTheftService::class.java))
            }
        }
    }

}