package com.example.juegomastermind

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, GameModeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3 segundos de retraso
    }
}