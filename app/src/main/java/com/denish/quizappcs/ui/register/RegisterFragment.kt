package com.denish.quizappcs.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denish.quizappcs.R
import com.denish.quizappcs.data.model.user.Role
import com.denish.quizappcs.databinding.FragmentRegisterBinding
import com.denish.quizappcs.ui.base.BaseFragment
import com.denish.quizappcs.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val viewModel: RegisterViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_register

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.btnRegister?.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        binding?.run {
            btnRegister.setOnClickListener {
                val selectedRoleString = etRole.selectedItem.toString()

                val selectedRole = when (selectedRoleString) {
                    "Teacher" -> Role.TEACHER
                    "Student" -> Role.STUDENT
                    else -> Role.STUDENT
                }
                viewModel.createUser(
                    firstName = etFirstName.text.toString(),
                    lastName = etLastName.text.toString(),
                    email = etEmail.text.toString(),
                    pass = etPass.text.toString(),
                    confirmPass = etConfirmPass.text.toString(),
                    role = selectedRole
                )
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.success.collect{
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                )
            }
        }
    }

}