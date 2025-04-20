package com.example.juegomastermind

import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
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
    private val feedbackCalculator = FeedbackCalculator()
    private lateinit var gameRestarter: GameRestarter
    private lateinit var guessHandler: GuessHandler
    private var intentos = 0
    private lateinit var intentosLabel: TextView
    private lateinit var textField: EditText
    private lateinit var historial: LinearLayout
    private lateinit var mainLayout: LinearLayout
    private lateinit var nuevaPartidaButton: Button
    private lateinit var numerosInteractivosLayout: LinearLayout
    private lateinit var celebrationHandler: CelebrationHandler
    private var modoAutomatico = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        numeroSecreto = generarNumeroSecreto()
        gameRestarter = GameRestarter(this, numeroSecreto)

        mainLayout = findViewById(R.id.main_layout)
        intentosLabel = findViewById<TextView>(R.id.intentosLabel).apply {
            text = "Intentos: 0"        }
        textField = findViewById<EditText>(R.id.textField)
        historial = findViewById<LinearLayout>(R.id.historialLayout)
        numerosInteractivosLayout = findViewById(R.id.numerosInteractivos)
        val button = findViewById<Button>(R.id.button)
        val toggleButton = findViewById<Button>(R.id.toggleButton)

        numerosInteractivosLayout.visibility = View.GONE // Ocultar inicialmente


        toggleButton.setOnClickListener {
            if (numerosInteractivosLayout.visibility == View.GONE) {
                numerosInteractivosLayout.visibility = View.VISIBLE
            } else {
                numerosInteractivosLayout.visibility = View.GONE
            }
        }
        nuevaPartidaButton = findViewById(R.id.nuevaPartidaButton)

        // Centrando el botón en la parte inferior
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            layoutParams.setMargins(0, 20, 0, 20)
            setLayoutParams(layoutParams)


        //Inicializar GuessHandler ANTES del listener del botón "Adivinar"
        guessHandler = GuessHandler(
            this,
            feedbackCalculator,
            historial,
            intentosLabel,
            intentos,
            textField,
            numeroSecreto
        )
         intentos = guessHandler.getIntentos()
        }

        val modoJuego = intent.getStringExtra("mode")
        if (modoJuego == Constants.GAME_MODE_AUTOMATIC) {
            modoAutomatico = true
            generarNumeroSecreto()
        } else {
            modoAutomatico = false
           solicitarNumeroAdivinar()
        }
        button.setOnClickListener {
            val adivinanza = textField.text.toString()
            if (!esNumeroValido(adivinanza)) {
                Toast.makeText(this, Constants.TOAST_INVALID_NUMBER, Toast.LENGTH_LONG).show()
            } else {
                guessHandler.procesarAdivinanza(adivinanza)
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
            textView.setOnClickListener {
                val numero = textView.text.toString()
                val textoActual = textField.text.toString()
                if (textoActual.length < NUMERO_DIGITOS && !textoActual.contains(numero)) {
                    textField.append(numero)
                }
            }
        }

        numerosTextViews.forEach { textView ->
            textView.setOnClickListener {
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

        nuevaPartidaButton.setOnClickListener {
            gameRestarter.confirmarReinicioJuego()
        }

        celebrationHandler = CelebrationHandler(
            this, mainLayout, nuevaPartidaButton, numeroSecreto, intentos
        )
    }

    private fun generarNumeroSecreto() {
        val posiblesDigitos = (1..9).toMutableList()
        numeroSecreto = (1..NUMERO_DIGITOS)
            .map { posiblesDigitos.removeAt(Random.nextInt(posiblesDigitos.size)).toString() }
            .joinToString("")
        Toast.makeText(this, Constants.TOAST_SECRET_GENERATED, Toast.LENGTH_LONG).show()
    }

    private fun solicitarNumeroAdivinar() {
        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        AlertDialog.Builder(this)
            .setTitle("Ingrese el número para adivinar")
            .setTitle(Constants.DIALOG_TITLE_INPUT_NUMBER)
            .setView(input)
            .setPositiveButton(Constants.DIALOG_BUTTON_OK) { dialog, _ ->
                val numeroAdivinar = input.text.toString()
                if (!esNumeroValido(numeroAdivinar)) {
                    Toast.makeText(this, Constants.TOAST_INVALID_NUMBER, Toast.LENGTH_LONG).show()
                    solicitarNumeroAdivinar()
                } else {
                    numeroSecreto = numeroAdivinar
                    mostrarInstrucciones()
                }
            }
            .setNegativeButton(Constants.DIALOG_BUTTON_BACK) { dialog, _ ->
                finish()
            }
            .show()
    }


    private fun mostrarInstrucciones() {
        Toast.makeText(this, "Ahora intenta adivinar el número secreto.", Toast.LENGTH_LONG).show()
    }

    private fun tieneNumerosRepetidos(numero: String): Boolean {
        return numero.toSet().size != numero.length
    }

    private fun esNumeroValido(numero: String): Boolean {
        return numero.length == NUMERO_DIGITOS && numero.matches("[1-9]+".toRegex()) && !tieneNumerosRepetidos(numero)
    }
}