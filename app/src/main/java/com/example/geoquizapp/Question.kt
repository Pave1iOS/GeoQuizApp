package com.example.geoquizapp

import androidx.annotation.StringRes

data class Question(
    @StringRes val textResID: Int,
    val answer: Boolean
)