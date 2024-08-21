package com.denish.quizappcs.data.repo

import com.denish.quizappcs.data.model.user.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserRepo {

    private fun getUid(): String {
        val uid = Firebase.auth.currentUser?.uid ?: throw Exception ("User doesn't exist")
        return uid
    }

    private fun getUserCollRef(): CollectionReference {
        return Firebase.firestore.collection("users")
    }

    suspend fun createUser(user: User) {
        getUserCollRef().document(getUid()).set(user).await()
    }

    suspend fun getUser(): User? {
        val res = getUserCollRef().document(getUid()).get().await()
        return res.data?.let { User.fromMap(it) }
    }

    suspend fun updateUser(user: User) {
        getUserCollRef().document(getUid()).set(user).await()
    }
}