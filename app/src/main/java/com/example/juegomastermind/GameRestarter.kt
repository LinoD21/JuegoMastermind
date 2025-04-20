package com.example.juegomastermind

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GameRestarter(private val activity: AppCompatActivity, private val numeroSecreto: String) {

    fun confirmarReinicioJuego() {
        AlertDialog.Builder(activity)
            .setTitle(Constants.DIALOG_TITLE_RESTART_CONFIRM)
            .setMessage(Constants.DIALOG_MESSAGE_RESTART_CONFIRM)
            .setPositiveButton(Constants.DIALOG_BUTTON_YES) { _, _ ->
                Toast.makeText(activity, "${Constants.TOAST_SECRET_WAS}$numeroSecreto", Toast.LENGTH_LONG).show()
                reiniciarJuego()
            }
            .setNegativeButton(Constants.DIALOG_BUTTON_NO) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun reiniciarJuego() {
        val intent = Intent(activity, GameModeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}