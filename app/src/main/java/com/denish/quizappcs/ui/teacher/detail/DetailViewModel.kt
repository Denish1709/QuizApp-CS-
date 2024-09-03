package com.denish.quizappcs.ui.teacher.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.denish.quizappcs.data.model.quiz.Quiz
import com.denish.quizappcs.data.repo.QuizRepo
import com.denish.quizappcs.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: QuizRepo
) : BaseViewModel() {

    private val _quizDetail = MutableLiveData<Quiz>()
    val quizDetail: LiveData<Quiz> get() = _quizDetail

    fun loadQuizDetails(quizId: String) {
        viewModelScope.launch {
            errorHandler {
                val loadedQuiz = repo.getQuestionById(quizId)
                loadedQuiz?.let {
                    _quizDetail.value = it
                } ?: run {
                    _error.emit("Quiz not found")
                }
            }
        }
    }


}