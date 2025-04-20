package com.example.juegomastermind

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val instructionsTextView: TextView = findViewById(R.id.instructionsTextView)
        val gameMode = intent.getStringExtra("gameMode")

        val instructions = when (gameMode) {
            "manual" -> "Modo Manual: Instrucciones para el modo manual."
            "automatic" -> "Modo Automático: Instrucciones para el modo automático."
            else -> "Instrucciones no disponibles"
        }

        instructionsTextView.text = instructions

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, GameModeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}