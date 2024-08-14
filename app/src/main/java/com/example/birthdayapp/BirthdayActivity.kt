package com.example.birthdayapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class BirthdayActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)

        val username = intent.getStringExtra("USERNAME") ?: ""
        val birthDate = intent.getStringExtra("BIRTHDATE") ?: ""

        val daysUntilBirthday = calculateDaysUntilNextBirthday(birthDate)
        val notificationMessage = "Welcome，$username！There are still $daysUntilBirthday day(s) until your next birthday."

        val messageTextView: TextView = findViewById(R.id.messageTextView)
        messageTextView.text = notificationMessage

        showNotification(notificationMessage)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDaysUntilNextBirthday(birthDateString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val birthDate = LocalDate.parse(birthDateString, formatter)

        val currentDate = LocalDate.now()
        var nextBirthday = birthDate.withYear(currentDate.year)

        if (nextBirthday.isBefore(currentDate) || nextBirthday.isEqual(currentDate)) {
            nextBirthday = nextBirthday.plusYears(1)
        }

        return ChronoUnit.DAYS.between(currentDate, nextBirthday)
    }

    private fun showNotification(message: String) {
        val notificationId = 101
        val channelId = "birthday_notification_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Birthday Notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = getColor(R.color.pink_500)
                enableVibration(true)
                description = "Notification for upcoming birthday"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_birthday_cake)
            .setContentTitle("Birthday notification")
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
