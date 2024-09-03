package com.denish.quizappcs.ui.student.home

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.denish.quizappcs.R
import com.denish.quizappcs.databinding.FragmentHomeBinding
import com.denish.quizappcs.ui.base.BaseFragment
import com.denish.quizappcs.ui.student.quizAccess.QuizAccessFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_home

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.ivAbout?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAboutFragment())
        }

        binding?.ivQuizAccess?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToQuizAccessFragment())
        }

        binding?.btnLogout?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }
    }

}