package com.denish.quizappcs.ui.student.quizPage

import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.denish.quizappcs.core.service.AuthService
import com.denish.quizappcs.data.model.quiz.Quiz
import com.denish.quizappcs.data.model.result.Result
import com.denish.quizappcs.data.repo.QuizRepo
import com.denish.quizappcs.data.repo.ResultRepo
import com.denish.quizappcs.ui.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizAnsweringViewModel @Inject constructor(
    private val authService: AuthService,
    private val resultRepo: ResultRepo
) : BaseViewModel(){

    private val db = FirebaseFirestore.getInstance()


    private fun getStudentById(): String {
        return authService.getUId() ?: throw Exception("User not authenticated")
    }


    fun fetchQuizData(accessCode: String) {
        viewModelScope.launch {
            db.collection("quizzes")
                .whereEqualTo("accessCode", accessCode)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null && !documents.isEmpty) {
                        val quiz = documents.documents[0].data?.let { Quiz.fromMap(it) }
                        _quiz.value = quiz
                    } else {
                        _errorMessage.value = "Quiz not found"
                    }
                }
                .addOnFailureListener { exception ->
                    _errorMessage.value = "Error fetching quiz: ${exception.message}"
                }
        }
    }

    fun submitAnswer(answer: String) {
        _studentAnswers.value.add(answer)
    }


    fun moveToNextQuestion() {
        val currentIndex = _currentQuestionIndex.value
        if ((_quiz.value?.question?.size ?: 0) > currentIndex + 1) {
            _currentQuestionIndex.value += 1
        } else {
            _errorMessage.value = "Quiz completed!" }
    }

    suspend fun addResult(quizId: String, mark: Int) {
        try {
            if (!resultRepo.checkTheResult(quizId, getStudentById())) {

                val result = Result(
                    finishId = quizId,
                    studentId = getStudentById(),
                    mark = mark
                )

                resultRepo.addResult(result)
                finish.emit(Unit)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}