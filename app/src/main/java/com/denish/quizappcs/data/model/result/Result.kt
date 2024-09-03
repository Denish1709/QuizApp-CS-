package com.denish.quizappcs.data.model.result


data class Result(
    val studentId: String ?= null,
    var studentName: String = "",
    val finishId: String ?= null,
    val mark: Int = 0,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "studentId" to studentId,
            "studentName" to studentName,
            "finishId" to finishId,
            "mark" to mark
        )
    }

    companion object {
        fun fromMap(map: Map<*,*>): Result {
            return Result(
                studentId = map["studentId"].toString(),
                studentName = map["studentName"].toString(),
                finishId = map["finishId"].toString(),
                mark = map["mark"].toString().toIntOrNull() ?: 0
            )
        }
    }
}
