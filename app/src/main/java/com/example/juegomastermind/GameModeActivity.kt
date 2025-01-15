package com.example.juegomastermind

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GameModeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_mode_activity)

        findViewById<Button>(R.id.buttonAutomatic).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "automatic")
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonManual).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "manual")
            startActivity(intent)
            finish()
        }
    }
}