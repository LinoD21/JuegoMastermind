package com.example.juegomastermind

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    companion object {
        private const val NUMERO_DIGITOS = 4
    }

    private lateinit var numeroSecreto: String
    private var intentos = 0
    private lateinit var intentosLabel: TextView
    private lateinit var textField: EditText
    private lateinit var historial: LinearLayout
    private lateinit var mainLayout: LinearLayout
    private lateinit var nuevaPartidaButton: Button
    private lateinit var numerosInteractivosLayout: LinearLayout
    private val intentosPrevios = HashSet<String>()
    private var celebracionText: TextView? = null
    private var modoAutomatico = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        mainLayout = findViewById(R.id.main_layout)
        intentosLabel = findViewById<TextView>(R.id.intentosLabel).apply {
            visibility = View.GONE
        }
        textField = findViewById<EditText>(R.id.textField)
        historial = findViewById<LinearLayout>(R.id.historialLayout)
        numerosInteractivosLayout = findViewById(R.id.numerosInteractivos)
        val button = findViewById<Button>(R.id.button)
        val toggleButton = findViewById<Button>(R.id.toggleButton)

        numerosInteractivosLayout.visibility = View.GONE // Ocultar inicialmente

        toggleButton.setOnClickListener {
            if (numerosInteractivosLayout.visibility == View.GONE) {
                numerosInteractivosLayout.visibility = View.VISIBLE
                toggleButton.text = "Ocultar Números"
            } else {
                numerosInteractivosLayout.visibility = View.GONE
                toggleButton.text = "Mostrar Números"
            }
        }

        nuevaPartidaButton = findViewById<Button>(R.id.nuevaPartidaButton).apply {
            setOnClickListener {
                confirmarReinicioJuego()
            }
            // Centrando el botón en la parte inferior
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            layoutParams.setMargins(0, 20, 0, 20)
            setLayoutParams(layoutParams)
        }

        val modoJuego = intent.getStringExtra("mode")
        if (modoJuego == "automatic") {
            modoAutomatico = true
            generarNumeroSecreto()
        } else {
            modoAutomatico = false
            solicitarNumeroAdivinar()
        }

        button.setOnClickListener {
            val adivinanza = textField.text.toString()
            if (!esNumeroValido(adivinanza)) {
                Toast.makeText(this, "Número inválido. No se permiten 0 o números repetidos.", Toast.LENGTH_LONG).show()
            } else {
                procesarAdivinanza(adivinanza)
            }
        }

        // Inicializa los TextViews del 1 al 9 y agrega el OnClickListener
        val numerosTextViews = listOf(
            findViewById<TextView>(R.id.num1),
            findViewById<TextView>(R.id.num2),
            findViewById<TextView>(R.id.num3),
            findViewById<TextView>(R.id.num4),
            findViewById<TextView>(R.id.num5),
            findViewById<TextView>(R.id.num6),
            findViewById<TextView>(R.id.num7),
            findViewById<TextView>(R.id.num8),
            findViewById<TextView>(R.id.num9)
        )

        numerosTextViews.forEach { textView ->
            textView.setOnClickListener { cambiarColorTextView(textView) }
        }
    }

    private fun cambiarColorTextView(textView: TextView) {
        when (textView.tag) {
            null -> {
                textView.setBackgroundColor(Color.RED)
                textView.tag = "rojo"
            }
            "rojo" -> {
                textView.setBackgroundColor(Color.GREEN)
                textView.tag = "verde"
            }
            "verde" -> {
                textView.setBackgroundColor(Color.parseColor("#9C27B0")) // Color inicial
                textView.tag = null
            }
        }
    }
    private fun generarNumeroSecreto() {
        val posiblesDigitos = (1..9).toMutableList()
        numeroSecreto = (1..NUMERO_DIGITOS)
            .map { posiblesDigitos.removeAt(Random.nextInt(posiblesDigitos.size)).toString() }
            .joinToString("")
        Toast.makeText(this, "Número secreto generado automáticamente", Toast.LENGTH_LONG).show()
    }

    private fun solicitarNumeroAdivinar() {
        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        AlertDialog.Builder(this)
            .setTitle("Ingrese el número para adivinar")
            .setView(input)
            .setPositiveButton("OK") { dialog, _ ->
                val numeroAdivinar = input.text.toString()
                if (!esNumeroValido(numeroAdivinar)) {
                    Toast.makeText(this, "Número inválido. No se permiten 0 o números repetidos.", Toast.LENGTH_LONG).show()
                    solicitarNumeroAdivinar()
                } else {
                    numeroSecreto = numeroAdivinar
                    mostrarInstrucciones()
                }
            }
            .setNegativeButton("Atrás") { dialog, _ ->
                val intent = Intent(this, GameModeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun mostrarInstrucciones() {
        Toast.makeText(this, "Ahora intenta adivinar el número secreto.", Toast.LENGTH_LONG).show()
    }

    private fun procesarAdivinanza(numeroAdivinar: String) {
        val feedback = obtenerFeedback(numeroAdivinar)
        intentos++
        intentosLabel.text = "Intentos: $intentos"
        agregarAdivinanzaAlHistorial("$numeroAdivinar -> $feedback")
        if (numeroAdivinar == numeroSecreto) {
            textField.isEnabled = false
            findViewById<Button>(R.id.button).isEnabled = false
            mostrarCelebracion()
        } else {
            textField.text.clear()
        }
    }

    private fun agregarAdivinanzaAlHistorial(adivinanza: String) {
        val textView = TextView(this).apply {
            text = adivinanza
            textSize = 17f
            setTextColor(Color.RED)
            setPadding(10, 10, 10, 10)
            gravity = Gravity.START
        }
        historialLayout.addView(textView, 0)
    }
    private fun mostrarCelebracion() {
        for (i in 0 until mainLayout.childCount) {
            mainLayout.getChildAt(i).visibility = View.GONE
        }
        celebracionText?.visibility = View.VISIBLE
        celebracionText = TextView(this).apply {
            text = "¡Felicidades!\nAdivinaste el número secreto\n$numeroSecreto\nen $intentos intentos."
            textSize = 26f
            setTextColor(Color.parseColor("#9C27B0"))
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                setMargins(0, 0, 0, 50)
            }
            startAnimation(AlphaAnimation(0.0f, 1.0f).apply {
                duration = 1000
                repeatCount = 5
            })
            nuevaPartidaButton.visibility = View.VISIBLE
        }
        mainLayout.addView(celebracionText)
    }

    private fun confirmarReinicioJuego() {
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de que quieres empezar una nueva partida?")
            .setPositiveButton("Sí") { _, _ ->
                Toast.makeText(this, "El número secreto era: $numeroSecreto", Toast.LENGTH_LONG).show()
                reiniciarJuego()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun reiniciarJuego() {
        intentos = 0
        intentosLabel.text = "Intentos: $intentos"
        intentosLabel.visibility = View.GONE
        historialLayout.removeAllViews() // Limpia el historial de adivinanzas
        celebracionText?.visibility = View.GONE
        intentosPrevios.clear()
        textField.text.clear()
        textField.isEnabled = true
        findViewById<Button>(R.id.button).isEnabled = true
        for (i in 0 until mainLayout.childCount) {
            mainLayout.getChildAt(i).visibility = View.VISIBLE
        }
        val intent = Intent(this, GameModeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun tieneNumerosRepetidos(numero: String): Boolean {
        return numero.toSet().size != numero.length
    }

    private fun esNumeroValido(numero: String): Boolean {
        return numero.length == NUMERO_DIGITOS && numero.matches("[1-9]+".toRegex()) && !tieneNumerosRepetidos(numero)
    }

    private fun obtenerFeedback(adivinanza: String): String {
        var posicionCorrecta = 0
        var numerosAcertados = 0
        for (i in 0 until NUMERO_DIGITOS) {
            if (adivinanza[i] == numeroSecreto[i]) {
                posicionCorrecta++
            }
        }
        val secretChars = numeroSecreto.toCharArray().toMutableList()
        val guessChars = adivinanza.toCharArray().toMutableList()
        for (i in 0 until NUMERO_DIGITOS) {
            if (guessChars[i] in secretChars) {
                numerosAcertados++
                secretChars.remove(guessChars[i])
            }
        }
        return "Num. Acertados: $numerosAcertados, Pos. Correctas: $posicionCorrecta"
    }
}