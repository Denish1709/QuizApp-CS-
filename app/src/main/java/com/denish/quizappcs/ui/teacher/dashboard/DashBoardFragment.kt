package com.denish.quizappcs.ui.teacher.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denish.quizappcs.R
import com.denish.quizappcs.data.model.quiz.Quiz
import com.denish.quizappcs.databinding.FragmentDashBoardBinding
import com.denish.quizappcs.ui.adapter.QuizAdapter
import com.denish.quizappcs.ui.base.BaseFragment
import com.denish.quizappcs.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<FragmentDashBoardBinding>() {

    override val viewModel: DashBoardViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_dash_board

    private lateinit var adapter: QuizAdapter

    override fun onBindView(view: View) {
        super.onBindView(view)

        setupAdapter()
        binding?.btnAddQuiz?.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashBoardFragmentToAddQuestionFragment())
        }

        binding?.btnLogut?.setOnClickListener {
            viewModel.logOut()
            findNavController().navigate(DashBoardFragmentDirections.actionDashBoardFragmentToLoginFragment())
        }

        binding?.btnRank?.setOnClickListener {
            findNavController().navigate(R.id.action_dashBoardFragment_to_rankingFragment)
        }

    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.getAllQuizes().collect {
                adapter.setQuizes(it)
                binding?.ivEmpty?.isInvisible = adapter.itemCount != 0
            }
        }
    }

    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList())
        binding?.rvQuiz?.adapter = adapter
        binding?.rvQuiz?.layoutManager = LinearLayoutManager(requireContext())
        adapter.listener = object: QuizAdapter.Listener {
            override fun onEditItem(quiz: Quiz) {
                findNavController().navigate(DashBoardFragmentDirections.actionDashBoardFragmentToEditQuestionFragment(quiz.id!!))
            }

            override fun onClickItem(quiz: Quiz) {
                findNavController().navigate(DashBoardFragmentDirections.actionDashBoardFragmentToDetailFragment(quiz.id!!))
            }

            override fun onDeleteItem(quiz: Quiz) {
                viewModel.deleteQuizes(quiz.id!!)
            }

        }
    }

}