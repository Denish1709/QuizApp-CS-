package com.denish.quizappcs.data.model.quiz

data class Quiz(
    val id: String ?= null,
    val title : String = "",
    val desc: String = "",
    val accessCode: String = "",
    val publishDate: String = "",
    val question: List<Question> ?= listOf(),
    val ownerId: String
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "desc" to desc,
            "accessCode" to accessCode,
            "publishDate" to publishDate,
            "question" to question,
            "ownerId" to ownerId
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Quiz {
            return Quiz(
                id = map["id"].toString(),
                title = map["title"].toString(),
                desc = map["desc"].toString(),
                accessCode = map["accessCode"].toString(),
                publishDate = map["publishDate"].toString(),
                question = (map["question"] as? List<Map<String, Any>>)?.map {Question.fromMap(it)},
                ownerId = map["ownerId"] as? String ?: ""
            )
        }
    }
}
