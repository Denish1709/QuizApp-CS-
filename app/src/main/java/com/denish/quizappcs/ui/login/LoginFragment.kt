package com.denish.quizappcs.ui.login

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.denish.quizappcs.R
import com.denish.quizappcs.data.model.user.Role
import com.denish.quizappcs.data.model.user.User
import com.denish.quizappcs.data.repo.UserRepo
import com.denish.quizappcs.databinding.FragmentLoginBinding
import com.denish.quizappcs.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_login

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.btnRegister?.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding?.btnLogin?.setOnClickListener {
            val email = binding?.etEmail?.text.toString()
            val pass = binding?.etPass?.text.toString()
            viewModel.login(email,pass)
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.success.collect {role ->
                when(role) {
                    Role.TEACHER -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashBoardFragment())
                    Role.STUDENT -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
            }
        }
    }

}