package com.example.birthdayapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val birthDateEditText: EditText = findViewById(R.id.birthDateEditText)
        val startButton: Button = findViewById(R.id.startButton)

        startButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val birthDate = birthDateEditText.text.toString()

            val intent = Intent(this, BirthdayActivity::class.java).apply {
                putExtra("USERNAME", username)
                putExtra("BIRTHDATE", birthDate)
            }
            startActivity(intent)
        }
    }
}
