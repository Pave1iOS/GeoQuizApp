package com.example.geoquizapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "CheatActivity"
private const val EXTRA_ANSWER_IS_TRUE = "answer_is_true"
const val EXTRA_ANSWER_SHOWN = "answer_show"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView

    private val viewModel: CheatViewModel by lazy {
        ViewModelProvider(this)[CheatViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text_view)

        viewModel.answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        viewModel.isCheating =
            savedInstanceState?.getBoolean(EXTRA_ANSWER_SHOWN, false) ?: false

        showAnswerButton.setOnClickListener {
            val answerText = when {
                viewModel.answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)

            viewModel.isCheating = true

            setAnswerShownResult(viewModel.isCheating)

            Log.d(TAG, "setOnClickListener - is cheating = ${viewModel.isCheating}")
        }

        apiLevelTextView.text = getString(R.string.api_level_text, Build.VERSION.SDK_INT)

        setAnswerShownResult(viewModel.isCheating)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(EXTRA_ANSWER_SHOWN, viewModel.isCheating)

        Log.i(TAG, "onSaveInstanceState - is cheating = ${viewModel.isCheating}")
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }

        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}