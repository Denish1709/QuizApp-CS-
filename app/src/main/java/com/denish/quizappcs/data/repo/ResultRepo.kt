package com.denish.quizappcs.data.repo

import com.denish.quizappcs.data.model.result.Result
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ResultRepo {

    private fun getCollection(): CollectionReference {
        return Firebase.firestore
            .collection("results")
    }


    suspend fun addResult(result: Result): String? {
        val res = getCollection().add(result.toMap()).await()
        return res?.id
    }

    suspend fun checkTheResult(finishId: String, studentId: String): Boolean {
        val querySnapshot: QuerySnapshot = getCollection()
            .whereEqualTo("finishId", finishId)
            .whereEqualTo("studentId", studentId)
            .get()
            .await()

        return !querySnapshot.isEmpty
    }

}