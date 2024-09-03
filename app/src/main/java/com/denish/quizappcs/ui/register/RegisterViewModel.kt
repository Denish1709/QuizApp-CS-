package com.denish.quizappcs.ui.register

import androidx.lifecycle.viewModelScope
import com.denish.quizappcs.core.service.AuthService
import com.denish.quizappcs.core.utils.ValidationUtil
import com.denish.quizappcs.data.model.Field
import com.denish.quizappcs.data.model.user.Role
import com.denish.quizappcs.data.model.user.User
import com.denish.quizappcs.data.repo.UserRepo
import com.denish.quizappcs.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
): BaseViewModel() {

    fun createUser(
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        confirmPass: String,
        role: String

    ) {
        val error = ValidationUtil.validate(
            Field(firstName, "[a-zA-z ]{2,20}", "Enter a valid name"),
            Field(lastName, "[a-zA-z ]{2,20}", "Enter a valid name"),
            Field(email, "[a-zA-Z0-9 ]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+", "Enter a valid email"),
            Field(pass, "[a-zA-Z0-9#$%]{3,20}", "Enter a valid password")
        )

        val selectedRole = Role.valueOf(role)

        if (error == null) {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler {
                    validate(firstName, lastName, email, pass, confirmPass)
                    authService.createUserWithEmailAndPass(email, pass)
                }?.let {
                    userRepo.createUser(
                        User(firstName, lastName, email, selectedRole)
                    )
                    _success.emit(selectedRole)
                }
            }
        } else {
            viewModelScope.launch {
                _error.emit(error)
            }
        }
    }


    private fun validate(firstName: String, lastName: String, email: String, pass: String, confirmPass: String) {
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            throw Exception("Please make sure all fields are filled in")
        }

        if (confirmPass.isEmpty() || confirmPass != pass) {
            throw Exception("Password and Confirm Password are not same")
        }
    }
}