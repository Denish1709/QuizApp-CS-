package com.denish.quizappcs.ui.rank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denish.quizappcs.R
import com.denish.quizappcs.data.model.user.Role
import com.denish.quizappcs.databinding.FragmentRankingBinding
import com.denish.quizappcs.ui.adapter.StudentMarksAdapter
import com.denish.quizappcs.ui.base.BaseFragment
import com.denish.quizappcs.ui.login.LoginFragmentDirections
import com.denish.quizappcs.ui.student.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RankingFragment : BaseFragment<FragmentRankingBinding>() {

    override val viewModel: RankingViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_ranking

    private lateinit var adapter: StudentMarksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeViewModel()
        return binding?.root
    }

    private fun setupRecyclerView() {
        adapter = StudentMarksAdapter()
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerView?.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.studentMarks.collect { marks ->
                adapter.submitList(marks)
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessage.collect { error ->
                error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}