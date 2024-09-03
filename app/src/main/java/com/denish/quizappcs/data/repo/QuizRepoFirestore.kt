package com.denish.quizappcs.data.repo

import android.util.Log
import com.denish.quizappcs.core.service.AuthService
import com.denish.quizappcs.data.model.quiz.Quiz
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepoFirestore(
    private val authService: AuthService
): QuizRepo {

    fun getCollection(): CollectionReference {
        val uid = authService.getUId() ?: throw Exception("User doesn't Exist")
        return Firebase.firestore.collection("quizzes")
    }

    override fun getAllQuestions() = callbackFlow<List<Quiz>> {
        val listener = getCollection().addSnapshotListener{ value, error ->
            if (error != null) {
                throw error
            }

            val quizs = mutableListOf<Quiz>()

            value?.documents?.map { item ->
                item.data?.let { quizMap ->
                    val quiz = Quiz.fromMap(quizMap)
                    quizs.add(quiz.copy(id = item.id))
                }
            }
            trySend(quizs)
        }
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun addQuestion(quiz: Quiz): String {
        try {
            val res = getCollection().add(quiz.toMap()).await()
            return res.id
        } catch (e: Exception) {
            Log.e("QuizRepoFirestore", "Error adding question to Firestore", e)
            throw e
        }
    }

    override suspend fun deleteQuestion(id: String) {
        getCollection().document(id).delete().await()
    }

    override suspend fun updateQuestion(quiz: Quiz): String {
        val documentId = quiz.id ?: throw IllegalArgumentException("Quiz ID cannot be null")
        return try {
            getCollection().document(documentId).set(quiz.toMap()).await()
            documentId
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getQuestionById(id: String): Quiz? {
        val res = getCollection().document(id).get().await()
        return res.data?.let {
            Quiz.fromMap(it).copy(id = res.id)
        }
    }

    override suspend fun getQuestionByAccessCode(accessCode: String): Quiz? {
        return try {
            val querySnapshot = getCollection()
                .whereEqualTo("accessCode", accessCode)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                Quiz.fromMap(document.data!!).copy(id = document.id)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("QuizRepoFirestore", "Error fetching quiz by access code", e)
            null

        }
    }

    override fun getMarks(id: String): Flow<List<Pair<String, Int>>> = callbackFlow {
        val listener = getCollection().document(id).collection("marks")
            .orderBy("mark", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    throw error
                }

                val marks = mutableListOf<Pair<String, Int>>()

                value?.documents?.map { document ->
                    val userId = document.id
                    val mark = document.getLong("mark")?.toInt() ?: 0
                    marks.add(Pair(userId, mark))
                }
                trySend(marks)
            }
        awaitClose {
            listener.remove()
        }
    }


}