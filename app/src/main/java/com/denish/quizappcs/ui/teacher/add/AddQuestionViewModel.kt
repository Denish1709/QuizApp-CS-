package com.denish.quizappcs.ui.teacher.add

import android.util.Log
import androidx.fragment.app.Fragment.SavedState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.denish.quizappcs.core.service.AuthService
import com.denish.quizappcs.data.model.quiz.Question
import com.denish.quizappcs.data.model.quiz.Quiz
import com.denish.quizappcs.data.repo.QuizRepo
import com.denish.quizappcs.ui.base.BaseViewModel
import com.denish.quizappcs.ui.teacher.base.BaseManageQuestionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddQuestionViewModel @Inject constructor(
    private val repo: QuizRepo,
    private val authService: AuthService
): BaseManageQuestionViewModel() {

    override fun submitQuestion(questions: List<Question>, title: String, accessCode: String, desc: String, publishDate: String) {
        viewModelScope.launch {
            try {
                val quizId = UUID.randomUUID().toString()
                val ownerId = authService.getUId() ?: throw Exception("User not authenticated")

                Log.d("AddQuestionViewModel", "Submitting quiz with ID: $quizId and ownerId: $ownerId")


                repo.addQuestion(
                    Quiz(
                        id = quizId,
                        question = questions,
                        title = title,
                        accessCode = accessCode,
                        desc = desc,
                        publishDate = publishDate,
                        ownerId = ownerId
                    )
                )
                finish.emit(Unit)
            } catch (e: Exception) {
                Log.e("AddQuestionViewModel", "Error submitting question", e)
            }
        }
    }
}