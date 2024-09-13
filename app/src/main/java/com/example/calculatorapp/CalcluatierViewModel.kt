package com.example.calculatorapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.sqrt

class CalcluatierViewModel: ViewModel() {
    private val _equationText = MutableLiveData("")
    val equationText : LiveData<String> = _equationText

    fun onButtonClick(button: String) {
        when (button) {
            "C" -> _equationText.value = ""
            "AC" -> _equationText.value = ""
            "=" -> {
                val result = calculateResult(_equationText.value ?: "")
                _equationText.value = result
            }
            else -> _equationText.value = _equationText.value + button
        }
    }

    fun onEquationTextChange(newText: String) {
        if (newText.endsWith("\n")) {
            val result = calculateResult(_equationText.value ?: "")
            _equationText.value = result
        } else {
            _equationText.value = newText
        }
    }

    private fun calculateResult(equation: String): String {
        try {
            val tokens = equation.replace("sqt", "√").split(Regex("(?<=[-+*/√%])|(?=[-+*/√%])"))
            return evaluateTokens(tokens).toString()
        } catch (e: Exception) {
            return "Error"
        }
    }

    private fun evaluateTokens(tokens: List<String>): Double {
        var result = tokens[0].toDouble()
        var i = 1

        while (i < tokens.size) {
            when (tokens[i]) {
                "+" -> result += tokens[++i].toDouble()
                "-" -> result -= tokens[++i].toDouble()
                "*" -> result *= tokens[++i].toDouble()
                "/" -> {
                    val divisor = tokens[++i].toDouble()
                    if (divisor == 0.0) throw ArithmeticException("Division by zero")
                    result /= divisor
                }
                "%" -> result %= tokens[++i].toDouble()
                "√" -> result = sqrt(result)
            }
            i++
        }

        return result
    }

}