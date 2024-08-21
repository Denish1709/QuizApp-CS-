package com.denish.quizappcs.ui.login

import androidx.lifecycle.viewModelScope
import com.denish.quizappcs.core.service.AuthService
import com.denish.quizappcs.data.repo.UserRepo
import com.denish.quizappcs.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
): BaseViewModel() {
    val success: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler{
                validate(email, pass)
                authService.loginWithEmailAndPass(email, pass)
            }?.let {
                success.emit(Unit)
            }
        }
    }

    private fun validate(email: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty()) {
            throw Exception("Email or password cannot be empty")
        }
    }
}