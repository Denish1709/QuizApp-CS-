package com.denish.quizappcs.ui.teacher.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.denish.quizappcs.R
import com.denish.quizappcs.databinding.FragmentDashBoardBinding
import com.denish.quizappcs.databinding.FragmentDetailBinding
import com.denish.quizappcs.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun getLayoutResource() = R.layout.fragment_detail

    override fun onBindView(view: View) {
        super.onBindView(view)
        viewModel.loadQuizDetails(args.quizId)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.quizDetail.observe(viewLifecycleOwner) { quiz ->
            // Update your UI with quiz details here
            quiz?.let {
                binding?.tvQuizTitle?.text = "Title: ${it.title}"

                binding?.tvQuizDesc?.text = "Description: ${it.desc}"

                val questionsText = it.question?.joinToString(separator = "\n\n") { question ->
                    "Question Name: ${question.question}"
                }
                binding?.tvQuizQuestion?.text = questionsText

                val optionText = it.question?.joinToString(separator = "\n\n") { question ->
                    "Options:\n${question.option.joinToString(separator = "\n")}"
                }
                binding?.tvQuizOptions?.text = optionText

                val answersText = it.question?.joinToString(separator = "\n\n") { question ->
                    "Correct Answer: ${question.correctAnswer}"
                }
                binding?.tvQuizAnswer?.text = answersText

                binding?.tvAccessCode?.text = "Quiz Id: ${it.accessCode}"
            }
        }
    }
}