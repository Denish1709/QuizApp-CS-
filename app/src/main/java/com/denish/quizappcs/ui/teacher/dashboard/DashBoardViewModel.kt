package com.denish.quizappcs.ui.teacher.dashboard

import androidx.lifecycle.viewModelScope
import com.denish.quizappcs.core.service.AuthService
import com.denish.quizappcs.data.repo.QuizRepo
import com.denish.quizappcs.data.repo.UserRepo
import com.denish.quizappcs.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val repo: QuizRepo,
    private val authService: AuthService
): BaseViewModel() {

    fun getAllQuizes() = repo.getAllQuestions()

    fun deleteQuizes(quizId: String) {
        viewModelScope.launch {
            errorHandler { repo.deleteQuestion(quizId) }
        }
    }

    fun logOut() {
        authService.logout()
    }
}