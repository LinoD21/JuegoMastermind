package com.example.juegomastermind

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class GameModeActivity : AppCompatActivity() {

    private lateinit var instructionsTextView: TextView
    private lateinit var startButton: Button
    private lateinit var backButton: Button
    private lateinit var automaticButton : Button
    private lateinit var manualButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_mode_activity)

        instructionsTextView = findViewById(R.id.gameModeInstructions)
        startButton = findViewById(R.id.startGameButton)
        backButton = findViewById(R.id.backButton)
        automaticButton = findViewById(R.id.buttonAutomatic)
        manualButton = findViewById(R.id.buttonManual)
        
        instructionsTextView.text = ""
        startButton.visibility = View.GONE
        backButton.visibility = View.GONE

        automaticButton.setOnClickListener {
            showInstructions("automatic")
        }

        manualButton.setOnClickListener {
            showInstructions("manual")
        }

        startButton.setOnClickListener {
            val selectedMode = intent.getStringExtra("mode") ?: ""
            startGame(selectedMode)
        }

        backButton.setOnClickListener {
            instructionsTextView.visibility = View.GONE
            startButton.visibility = View.GONE
            backButton.visibility = View.GONE
            automaticButton.visibility = View.VISIBLE
            manualButton.visibility = View.VISIBLE
            
        }
    }
    private fun showInstructions(mode: String) {
        automaticButton.visibility = View.GONE
        manualButton.visibility = View.GONE
        intent.putExtra("mode", mode)
        instructionsTextView.text = if (mode == "automatic") "Modo Automático: El juego genera un código secreto y debes adivinarlo." else "Modo Manual: Tú estableces el código secreto y otro jugador debe adivinarlo."
        instructionsTextView.visibility = View.VISIBLE
        startButton.visibility = View.VISIBLE
        backButton.visibility = View.VISIBLE
    }
    private fun startGame(mode: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("mode", mode)
        startActivity(intent)
        finish()
        }
    }
