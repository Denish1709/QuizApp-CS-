package com.denish.quizappcs.ui.teacher.base

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denish.quizappcs.core.utils.CSVUtil
import com.denish.quizappcs.data.model.quiz.Question
import com.denish.quizappcs.databinding.FragmentManageQuestionBinding
import com.denish.quizappcs.ui.base.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseManageQuestionFragment: BaseFragment<FragmentManageQuestionBinding>() {

    override val viewModel: BaseManageQuestionViewModel by viewModels()

}