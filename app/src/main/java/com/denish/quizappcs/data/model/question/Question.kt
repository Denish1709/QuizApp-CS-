package com.denish.quizappcs.data.model.question

import java.util.Timer

data class Question(
    val name: String,
    val desc: String,
    val timer: Timer
)
