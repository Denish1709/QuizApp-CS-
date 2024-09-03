package com.denish.quizappcs.data.model.quiz

import java.sql.Time

data class Question(
    val id: String? = null,
    val question: String = "",
    val option: List<String> = listOf(),
    val correctAnswer: String = "",
    val time: Int = 30,
    val mark: Int = 1,
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "question" to question,
            "option" to option,
            "correctAnswer" to correctAnswer,
            "time" to time,
            "mark" to mark
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>) : Question {
            return Question(
                question = map["question"].toString(),
                option = (map["option"] as List<String>) ?: emptyList(),
                correctAnswer = map["correctAnswer"].toString(),
                time = map["time"].toString().toIntOrNull() ?: 30,
                mark = map["mark"].toString().toIntOrNull() ?: 1
            )
        }
    }
}
