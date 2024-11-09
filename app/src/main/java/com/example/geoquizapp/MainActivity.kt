package com.example.geoquizapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var buttonTrue: Button
    private lateinit var buttonFalse: Button
    private lateinit var buttonCheat: Button
    private lateinit var buttonNext: ImageButton
    private lateinit var buttonBack: ImageButton

    private val viewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    private var correctAnswerCount = 0

    private val cheatActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                viewModel.isCheater =
                    data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false

                Log.d(TAG, "cheatActivityLauncher - ${viewModel.isCheater}") }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate is called")
        setContentView(R.layout.activity_main)

        // save state
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        viewModel.questionIndex = currentIndex

        // properties
        tvQuestion = findViewById(R.id.tv_question)
        buttonTrue = findViewById(R.id.button_true)
        buttonFalse = findViewById(R.id.button_false)
        buttonCheat = findViewById(R.id.cheat_button)
        buttonNext = findViewById(R.id.button_next)
        buttonBack = findViewById(R.id.button_back)

        // listener
        buttonTrue.setOnClickListener {
            checkAnswer(true, it)
        }

        buttonFalse.setOnClickListener {
            checkAnswer(false, it)
        }
        
        buttonCheat.setOnClickListener {
            val answerIsTrue = viewModel.getCurrentQuestionAnswer
            val intent = CheatActivity.newIntent(this, answerIsTrue)
            cheatActivityLauncher.launch(intent)
        }

        buttonNext.setOnClickListener {
            nextQuestion()
        }

        buttonBack.setOnClickListener {
            pastQuestion()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.i(TAG, "onSaveInstanceState is called")
        outState.putInt(KEY_INDEX, viewModel.questionIndex)
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
        if (isEnabled) {
            buttonTrue.isEnabled = true
            buttonFalse.isEnabled = true
            buttonTrue.alpha = 1f
            buttonFalse.alpha = 1f
        } else {
            buttonTrue.isEnabled = false
            buttonFalse.isEnabled = false
            buttonTrue.alpha = 0.5f
            buttonFalse.alpha = 0.5f
        }
    }

    private fun showResult(correctAnswerCount: Int) {

        if (viewModel.questionIndex == viewModel.getLastQuestionIndex) {
            android.app.AlertDialog.Builder(this)
                .setTitle(R.string.final_title)
                .setMessage("${getString(R.string.final_message)} $correctAnswerCount")
                .setPositiveButton("OK") { _, _ ->
                    viewModel.firstQuestion()
                    updateQuestion()
                    enabledButtons(true)
                }
                .show()
        }
    }

    private fun pastQuestion() {
        if (viewModel.questionIndex > 0) {
            viewModel.pastQuestion()
            updateQuestion()
        } else {
            viewModel.lastQuestion()
            updateQuestion()
        }
    }

    private fun nextQuestion() {
        viewModel.nextQuestion()
        updateQuestion()
        enabledButtons(true)
    }

    private fun checkAnswer(userAnswer: Boolean, view: View) {
        val correctAnswer = viewModel.getCurrentQuestionAnswer

        val messageResID = when {
            viewModel.isCheater -> {
                viewModel.isCheater = false
                R.string.judgment_wrong
            }
            userAnswer == correctAnswer -> {
                correctAnswerCount += 1
                R.string.correct_toast
            }
            else -> R.string.incorrect_toast
        }

        Snackbar.make(view, messageResID, Snackbar.LENGTH_SHORT).show()

        enabledButtons(false)

        showResult(correctAnswerCount)
    }

    private fun updateQuestion() {
        val currentQuestion = viewModel.getCurrentQuestionText
        tvQuestion.setText(currentQuestion)
    }
}