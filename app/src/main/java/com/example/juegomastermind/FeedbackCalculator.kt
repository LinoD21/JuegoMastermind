package com.example.juegomastermind

class FeedbackCalculator {
    fun obtenerFeedback(adivinanza: String, numeroSecreto: String): String {
        var posicionCorrecta = 0
        var numerosAcertados = 0
        val NUMERO_DIGITOS = 4 // This should ideally be a constant, but we're keeping it local for now.

        for (i in 0 until NUMERO_DIGITOS) {
            if (adivinanza[i] == numeroSecreto[i]) {
                posicionCorrecta++
            }
        }
        val secretChars = StringBuilder(numeroSecreto)
        for (char in adivinanza) {
            val index = secretChars.indexOf(char)
            if (index != -1) {
                numerosAcertados++
                secretChars.setCharAt(index, 'x')
            }
        }
        return "Num. Acertados: $numerosAcertados, Pos. Correctas: $posicionCorrecta"
    }
}