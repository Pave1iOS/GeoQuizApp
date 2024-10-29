package com.example.geoquizapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var buttonTrue: Button
    private lateinit var buttonFalse: Button
    private lateinit var buttonNext: Button

    private val questionsList = listOf(
        Question(R.string.question_daddy_potter, true),
        Question(R.string.question_volan_de_mort, true),
        Question(R.string.question_second_name_potter, false),
        Question(R.string.question_pridira, true),
        Question(R.string.question_avada_kedabra, false),
        Question(R.string.question_mantia_nevidimka, true)
    )

    private var questionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // properties
        tvQuestion = findViewById(R.id.tv_question)
        buttonTrue = findViewById(R.id.button_true)
        buttonFalse = findViewById(R.id.button_false)
        buttonNext = findViewById(R.id.button_next)

        // listener
        buttonTrue.setOnClickListener {
            checkAnswer(true)
        }

        buttonFalse.setOnClickListener {
            checkAnswer(false)
        }

        buttonNext.setOnClickListener {
            questionIndex = (questionIndex + 1) % questionsList.size
            updateQuestion()
        }

        updateQuestion()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val currectAnswer = questionsList[questionIndex].answer

        val messageResID = if (userAnswer == currectAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResID, Toast.LENGTH_SHORT).show()
    }

    private fun updateQuestion() {
        val currentQuestion = questionsList[questionIndex].textResID
        tvQuestion.setText(currentQuestion)
    }
}