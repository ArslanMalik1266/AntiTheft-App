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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.antitheftapp.R
import com.example.antitheftapp.service.AntiTheftService
import java.util.Objects

class HomeScreen : AppCompatActivity() {

    private lateinit var btnArm : Button
    private lateinit var tvInstruction : TextView
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
        tvInstruction = findViewById<TextView>(R.id.tvInstruction)


        btnArm.setOnClickListener {
            if (isArmed){
                isArmed = false
                btnArm.text = "ACTIVE"
                tvInstruction.text = "Tap to activate protection"
                tvInstruction.setTextColor(ContextCompat.getColor(this,R.color.lightGrey))
                btnArm.background =ContextCompat.getDrawable(this,R.drawable.circular_btn_unselected)
                btnArm.setTextColor(ContextCompat.getColor(this,R.color.white))
              stopService(Intent(this, AntiTheftService::class.java))
            }else {
                isArmed = true
                btnArm.text = "ACTIVATED"
                tvInstruction.text = "Protection is active"
                tvInstruction.setTextColor(ContextCompat.getColor(this,R.color.green))
                btnArm.background = ContextCompat.getDrawable(this,R.drawable.circular_button)
                btnArm.setTextColor(ContextCompat.getColor(this,R.color.white))
                startForegroundService(Intent(this, AntiTheftService::class.java))
            }
        }
    }

}