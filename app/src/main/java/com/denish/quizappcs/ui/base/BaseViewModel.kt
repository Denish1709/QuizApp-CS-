package com.denish.quizappcs.ui.base

import androidx.lifecycle.ViewModel
import com.denish.quizappcs.data.model.quiz.Quiz
import com.denish.quizappcs.data.model.user.Role
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel: ViewModel() {
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    protected val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    protected val _success = MutableSharedFlow<Role>()
    val success: SharedFlow<Role> = _success

    private val _totalMarks = MutableStateFlow(0)
    val totalMarks: StateFlow<Int> get() = _totalMarks

    protected val _studentAnswers = MutableStateFlow<MutableList<String>>(mutableListOf())
    val studentAnswers: StateFlow<List<String>> get() = _studentAnswers

    protected val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> get() = _currentQuestionIndex

    open val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    protected val _quiz = MutableStateFlow<Quiz?>(null)
    open val quiz: StateFlow<Quiz?> get() = _quiz



    suspend fun <T>errorHandler(func: suspend () -> T?): T? {
        return try {
            func()
        } catch (e: Exception) {
            _error.emit(e.message.toString())
            e.printStackTrace()
            null
        }
    }
}