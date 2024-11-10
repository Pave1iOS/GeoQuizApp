package com.example.geoquizapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var buttonTrue: Button
    private lateinit var buttonFalse: Button
    private lateinit var buttonCheat: Button
    private lateinit var tvNumberAttempts: TextView
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

                viewModel.cheatUsed()

                Log.d(TAG, "cheatActivityLauncher - ${viewModel.isCheater}")
                Log.i(TAG, "cheat used = ${viewModel.getUseCheatCount}")
                Log.i(TAG, "cheat attempts = ${viewModel.getCheatAttempt}")
            }
        }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate is called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        viewModel.questionIndex = currentIndex

        tvQuestion = findViewById(R.id.tv_question)
        buttonTrue = findViewById(R.id.button_true)
        buttonFalse = findViewById(R.id.button_false)
        buttonCheat = findViewById(R.id.cheat_button)
        tvNumberAttempts = findViewById(R.id.number_attempts_tv)
        buttonNext = findViewById(R.id.button_next)
        buttonBack = findViewById(R.id.button_back)

        buttonTrue.setOnClickListener {
            checkAnswer(true, it)
        }

        buttonFalse.setOnClickListener {
            checkAnswer(false, it)
        }
        
        buttonCheat.setOnClickListener {
            goToCheatScene(it)
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

        setUpCheatTextView()

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
    private fun setUpCheatTextView() {
        val attempts = viewModel.getCheatAttempt

        if (attempts in 0..2) {
            tvNumberAttempts.text = getString(R.string.number_attempts_text, attempts)
        }

        if (attempts == 0) {
            enableButtonCheat(false)
        }
    }

    private fun goToCheatScene(view: View) {
        val answerIsTrue = viewModel.getCurrentQuestionAnswer
        val intent = CheatActivity.newIntent(this, answerIsTrue)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val options = ActivityOptionsCompat.makeClipRevealAnimation(
                view,
                0,
                0,
                view.width,
                view.height
            )

            cheatActivityLauncher.launch(intent, options)
        } else {
            cheatActivityLauncher.launch(intent)
        }
    }

    private fun enableButtonCheat(isEnabled: Boolean) {
        setButtonEnabled(buttonCheat, isEnabled)
    }

    private fun enabledButtonTrueAndFalse(isEnabled: Boolean) {
        setButtonEnabled(buttonTrue, isEnabled)
        setButtonEnabled(buttonFalse, isEnabled)
    }

    private fun setButtonEnabled(button: Button, isEnabled: Boolean) {
        button.isEnabled = isEnabled
        button.alpha = if (isEnabled) 1f else 0.5f
    }

    private fun showResult(correctAnswerCount: Int) {

        if (viewModel.questionIndex == viewModel.getLastQuestionIndex) {
            android.app.AlertDialog.Builder(this)
                .setTitle(R.string.final_title)
                .setMessage("${getString(R.string.final_message)} $correctAnswerCount " +
                        "${getString(R.string.question_text)}\n${getString(R.string.cheat_message)} " +
                        "${viewModel.getUseCheatCount} ${getString(R.string.cheat_text)}")
                .setPositiveButton("OK") { _, _ ->
                    viewModel.firstQuestion()
                    updateQuestion()
                    enabledButtonTrueAndFalse(true)
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
        enabledButtonTrueAndFalse(true)
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

        enabledButtonTrueAndFalse(false)

        showResult(correctAnswerCount)
    }

    private fun updateQuestion() {
        val currentQuestion = viewModel.getCurrentQuestionText
        tvQuestion.setText(currentQuestion)
    }
}