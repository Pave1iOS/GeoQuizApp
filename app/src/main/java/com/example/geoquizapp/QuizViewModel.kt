package com.example.geoquizapp

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    private var cheatAttempts = 3
    private var useCheatCount = 0
    var questionIndex = 0
    var isCheater = false

    private val questionsList = listOf(
        Question(R.string.question_daddy_potter, true),
        Question(R.string.question_volan_de_mort, true),
        Question(R.string.question_second_name_potter, false),
        Question(R.string.question_pridira, true),
        Question(R.string.question_avada_kedabra, false),
        Question(R.string.question_mantia_nevidimka, true)
    )

    val getCheatAttempt: Int
        get() = cheatAttempts

    val getUseCheatCount: Int
        get() = useCheatCount

    val getCurrentQuestionAnswer: Boolean
        get() = questionsList[questionIndex].answer

    val getCurrentQuestionText: Int
        get() = questionsList[questionIndex].textResID

    val getLastQuestionIndex: Int
        get() = questionsList.size - 1

    fun cheatUsed() {
        if (isCheater) {
            useCheatCount += 1
            cheatAttempts -= 1
        }
    }

    fun nextQuestion() {
        questionIndex = (questionIndex + 1) % questionsList.size
    }

    fun pastQuestion() {
        questionIndex = (questionIndex - 1) % questionsList.size
    }

    fun lastQuestion() {
        questionIndex = questionsList.size - 1
    }

    fun firstQuestion() {
        questionIndex = 0
    }
}