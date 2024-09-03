package com.denish.quizappcs.ui.rank

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denish.quizappcs.data.model.result.Result
import com.denish.quizappcs.data.model.user.User
import com.denish.quizappcs.data.repo.QuizRepo
import com.denish.quizappcs.data.repo.ResultRepo
import com.denish.quizappcs.ui.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor() : BaseViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _studentMarks = MutableStateFlow<List<Result>>(emptyList())
    val studentMarks: StateFlow<List<Result>> get() = _studentMarks

    init {
        fetchStudentMarks()
    }

    private fun fetchStudentMarks() {
        viewModelScope.launch {
            try {
                val marksList = mutableListOf<Result>()
                val snapshot = db.collection("results").orderBy("mark", Query.Direction.DESCENDING).get().await()

                for (document in snapshot.documents) {
                    val studentMark = document.toObject(Result::class.java)
                    studentMark?.let { mark ->
                        val userSnapshot =
                            mark.studentId?.let { db.collection("users").document(it).get().await() }
                        val user = userSnapshot?.toObject(User::class.java)

                        mark.studentName = "${user?.firstName ?: ""} ${user?.lastName ?: ""}".trim()

                        marksList.add(mark)
                    }

                }

                Log.d("marks", "${marksList}")
                _studentMarks.value = marksList
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching student marks: ${e.message}"
                e.printStackTrace()
            }
        }
    }

}