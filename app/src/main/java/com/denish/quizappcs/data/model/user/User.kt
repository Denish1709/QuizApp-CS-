package com.denish.quizappcs.data.model.user

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: Role = Role.STUDENT
) {
    companion object {
        fun fromMap(map: Map<*,*>): User {
            return User(
                firstName = map["firstName"].toString(),
                lastName = map["lastName"].toString(),
                email = map["email"].toString(),
                role = map["role"]?.let { Role.valueOf(it.toString()) } ?: Role.STUDENT
            )
        }
    }
}
