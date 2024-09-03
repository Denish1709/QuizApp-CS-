package com.denish.quizappcs.ui.teacher.base

import com.denish.quizappcs.data.model.quiz.Question
import com.denish.quizappcs.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseManageQuestionViewModel: BaseViewModel() {
    override val finish : MutableSharedFlow<Unit> = MutableSharedFlow()

    abstract fun submitQuestion(questions: List<Question>, title: String, accessCode: String, desc: String, publishDate: String)
}