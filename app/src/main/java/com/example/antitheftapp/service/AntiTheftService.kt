package com.example.antitheftapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.display.DisplayManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.antitheftapp.R

class AntiTheftService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private val TAG = "AntiTheftService"
    private var isFirstReading = true
    private var mediaPlayer: MediaPlayer? = null
    private var isScreenOn = true


    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        proximitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }



        startForegroundServiceNotification()


    }

    private fun startForegroundServiceNotification() {
        val channelId = "anti_theft_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Anti Theft Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Anti Theft Active")
            .setContentText("Monitoring phone movement...")
            .setSmallIcon(com.example.antitheftapp.R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(event: SensorEvent) {


        val value = event.values[0]
        val max = proximitySensor?.maximumRange ?: 5f

        if (isFirstReading) {
            isFirstReading = false
            Log.d("AlarmUpdate", "Initial reading ignored")
            return
        }

        if (value >= max) {
            Log.d("AlarmUpdate", "Phone snatched!")
            playAlarm()
        } else {
            Log.d("AlarmUpdate", "Phone still in pocket")
            stopAlarm()
        }
    }

    override fun onDestroy() {

        sensorManager.unregisterListener(this)
        stopAlarm()
        super.onDestroy()
    }

    private fun playAlarm() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.annoying_alarm_clock)
            mediaPlayer?.isLooping = true
        }
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }


}