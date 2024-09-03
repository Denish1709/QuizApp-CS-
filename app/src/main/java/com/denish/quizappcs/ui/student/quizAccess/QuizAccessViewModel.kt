package com.denish.quizappcs.ui.student.quizAccess

import androidx.lifecycle.viewModelScope
import com.denish.quizappcs.data.repo.QuizRepo
import com.denish.quizappcs.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizAccessViewModel @Inject constructor(
    private val quizRepo: QuizRepo
) :BaseViewModel() {

    val quizExists = MutableStateFlow<Boolean?>(null)

    fun verifyAccessCode(accessCode: String) {
        viewModelScope.launch {
            val quiz = quizRepo.getQuestionByAccessCode(accessCode.lowercase())  // Convert to lowercase
            quizExists.value = quiz != null
        }
    }
}