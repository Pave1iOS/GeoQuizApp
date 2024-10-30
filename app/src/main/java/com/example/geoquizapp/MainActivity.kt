package com.example.geoquizapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var buttonTrue: Button
    private lateinit var buttonFalse: Button
    private lateinit var buttonNext: ImageButton
    private lateinit var buttonBack: ImageButton

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
        Log.d(TAG, "onCreate is called")
        setContentView(R.layout.activity_main)

        // properties
        tvQuestion = findViewById(R.id.tv_question)
        buttonTrue = findViewById(R.id.button_true)
        buttonFalse = findViewById(R.id.button_false)
        buttonNext = findViewById(R.id.button_next)
        buttonBack = findViewById(R.id.button_back)

        // listener
        buttonTrue.setOnClickListener {
            checkAnswer(true)
        }

        buttonFalse.setOnClickListener {
            checkAnswer(false)
        }

        buttonNext.setOnClickListener {
            nextQuestion()
        }

        buttonBack.setOnClickListener {
            otherQuestion()
        }

        tvQuestion.setOnClickListener {
            nextQuestion()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart is called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume is called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause is called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop is called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy is called")
    }

    // private fun
    private fun enabledButtons(isEnabled: Boolean) {
//        if (isEnabled) {
//            buttonTrue.isEnabled = true
//            buttonFalse.isEnabled = true
//        } else {
//            buttonTrue.isEnabled = false
//            buttonFalse.isEnabled = false
//        }
    }

    private fun otherQuestion() {
        if (questionIndex > 0) {
            questionIndex = (questionIndex - 1) % questionsList.size
            updateQuestion()
        } else {
            questionIndex = questionsList.size - 1
            updateQuestion()
        }
    }

    private fun nextQuestion() {
        questionIndex = (questionIndex + 1) % questionsList.size
        updateQuestion()
        enabledButtons(false)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val currectAnswer = questionsList[questionIndex].answer

        val messageResID = if (userAnswer == currectAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResID, Toast.LENGTH_LONG).show()

        enabledButtons(false)
    }

    private fun updateQuestion() {
        val currentQuestion = questionsList[questionIndex].textResID
        tvQuestion.setText(currentQuestion)
    }
}