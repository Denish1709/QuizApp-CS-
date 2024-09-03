package com.denish.quizappcs.core.utils

import com.denish.quizappcs.data.model.quiz.Question
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object CSVUtil {
    fun parseCSV(inputStream: InputStream) : List<Question> {
        val questions = mutableListOf<Question>()

        try {
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine()

            reader.forEachLine { line ->
                val tokens = line.split(",")
                if (tokens.size >= 7) {  // Ensure there's enough data for a Question
                    val questionText = tokens[0].trim()
                    val options = tokens.subList(1, 5).map { it.trim() }
                    val correctAnswer = tokens[5].trim()
                    val time = tokens[6].trim().toIntOrNull() ?: 30
                    val mark = tokens.getOrNull(7)?.trim()?.toIntOrNull() ?: 1

                    val question = Question(
                        question = questionText,
                        option = options,
                        correctAnswer = correctAnswer,
                        time = time,
                        mark = mark
                    )

                    questions.add(question)
                }
            }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return questions
    }
}