package com.denish.quizappcs.core.utils

import com.denish.quizappcs.data.model.Field


object ValidationUtil {
    fun validate(vararg fields: Field): String? {
        fields.forEach { field ->
            if(!Regex(field.regExp).matches(field.value)) {
                return field.errMsg
            }
        }
        return null
    }
}