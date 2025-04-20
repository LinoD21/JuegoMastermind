package com.example.juegomastermind

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CelebrationHandler(
    private val activity: AppCompatActivity,
    private val mainLayout: LinearLayout,
    private val nuevaPartidaButton: TextView,
    private val numeroSecreto: String,
    private val intentos: Int
) {

    private var celebracionText: TextView? = null
    fun handleWin() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("¡Has Ganado!")
        builder.setMessage("¡Felicidades! Has adivinado el código en $intentos intentos.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            restartGame()
        }
        builder.setCancelable(false)
        builder.show()
    }
    private fun restartGame() {
        val intent = Intent(activity, GameModeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }



//    fun mostrarCelebracion() {
//        for (i in 0 until mainLayout.childCount) {
//            mainLayout.getChildAt(i).visibility = View.GONE
//        }
//        celebracionText?.visibility = View.VISIBLE
//        celebracionText = TextView(activity).apply {
//            text = "¡Felicidades!\nAdivinaste el número secreto\n$numeroSecreto\nen $intentos intentos."
//            textSize = 26f
//            setTextColor(Color.parseColor("#9C27B0"))
//            gravity = Gravity.CENTER
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//                gravity = Gravity.CENTER
//                setMargins(0, 0, 0, 50)
//            }
//        mainLayout.post { mainLayout.removeView(celebracionText) }
//    }
}
