package com.example.juegomastermind

import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class GuessHandler(
    private val activity: MainActivity, // Usar MainActivity para acceder a elementos de la UI
    private val feedbackCalculator: FeedbackCalculator,
    private val historialLayout: LinearLayout,
    private val intentosLabel: TextView,
    private var intentos: Int,
    private val textField: TextView, // Asumo que textField es un TextView en este contexto
    private val numeroSecreto: String
) {

    fun procesarAdivinanza(numeroAdivinar: String) {
        val feedback = feedbackCalculator.obtenerFeedback(numeroAdivinar, numeroSecreto)
        intentos++
        intentosLabel.text = "Intentos: $intentos"
        agregarAdivinanzaAlHistorial("$numeroAdivinar -> $feedback")

        if (numeroAdivinar == numeroSecreto) {
            // Acceder a elementos de la UI a través de la actividad
            activity.findViewById<TextView>(R.id.textField).isEnabled = false
            activity.findViewById<TextView>(R.id.button).isEnabled = false
            // Llama a mostrarCelebracion desde MainActivity
            activity.mostrarCelebracion()
        } else {
            textField.text = ""
        }
    }

    private fun agregarAdivinanzaAlHistorial(adivinanza: String) {
        val textView = TextView(activity).apply {
            text = adivinanza
            textSize = 17f
            setTextColor(Color.RED)
            setPadding(10, 10, 10, 10)
            gravity = Gravity.START
        }
        historialLayout.addView(textView, 0)
    }

    fun getIntentos(): Int = intentos
}
