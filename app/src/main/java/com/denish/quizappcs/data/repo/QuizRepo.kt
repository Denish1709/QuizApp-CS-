package com.denish.quizappcs.data.repo

import com.denish.quizappcs.data.model.quiz.Question
import com.denish.quizappcs.data.model.quiz.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepo {

    fun getAllQuestions() : Flow<List<Quiz>>

    suspend fun addQuestion(quiz: Quiz): String

    suspend fun deleteQuestion(id: String)

    suspend fun updateQuestion(quiz: Quiz): String

    suspend fun getQuestionById(id: String): Quiz?

    suspend fun getQuestionByAccessCode(accessCode: String): Quiz?

    fun getMarks(id: String): Flow<List<Pair<String, Int>>>

}