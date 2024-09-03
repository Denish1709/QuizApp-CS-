package com.denish.quizappcs.ui.teacher.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.denish.quizappcs.data.model.quiz.Question
import com.denish.quizappcs.data.model.quiz.Quiz
import com.denish.quizappcs.data.repo.QuizRepo
import com.denish.quizappcs.ui.teacher.base.BaseManageQuestionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuestionViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val state: SavedStateHandle
): BaseManageQuestionViewModel() {

    override val quiz: MutableStateFlow<Quiz?> = MutableStateFlow(null)
    private val accessCode = state.get<String>("quizId")

    init {
        viewModelScope.launch {
            accessCode?.let {  id ->
                quiz.value = quizRepo.getQuestionById(id)
            }
        }
    }

    override fun submitQuestion(
        questions: List<Question>,
        title: String,
        accessCode: String,
        desc: String,
        publishDate: String
    ) {
        quiz.value?.let {
            val updatedQuiz = it.copy(
                question = questions,
                title = title,
                accessCode = accessCode,
                desc = desc,
                publishDate = publishDate
            )
            viewModelScope.launch {
                quizRepo.updateQuestion(updatedQuiz)
                finish.emit(Unit)
            }
        }
    }

    fun updateQuiz(questions: List<Question>, title: String, quizId: String, desc: String, publishDate: String

    ): Boolean {
        return try {
            submitQuestion(questions, title, quizId, desc, publishDate)
            true
        } catch (e: Exception) {
            false
        }

    }

}