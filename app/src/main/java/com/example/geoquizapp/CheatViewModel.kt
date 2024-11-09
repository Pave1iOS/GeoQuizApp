package com.example.geoquizapp

import androidx.lifecycle.ViewModel

class CheatViewModel: ViewModel() {
    private var cheatCount = 0

    var isCheating = false
    var answerIsTrue = false

    val getCheatUseCount: Int
        get() = cheatCount

    fun useCheat() {
        cheatCount += 1
    }
}
