package com.example.juegomastermind

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.postDelayed({
            startButton.visibility = View.VISIBLE
            startButton.isEnabled = true
        }, 5000) // 5 segundos de retraso

        startButton.setOnClickListener {
            startActivity(Intent(this, GameModeActivity::class.java))
            finish()
        }
    }
}