package com.cuhacking.app.signin.domain

import com.cuhacking.app.R
import com.cuhacking.app.data.Result
import com.cuhacking.app.data.db.User
import com.cuhacking.app.signin.data.AuthenticationManager
import com.cuhacking.app.signin.data.model.SignInUiModel
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val authenticationManager: AuthenticationManager) {
    suspend operator fun invoke(email: String, password: String): SignInUiModel =
        when (val result = authenticationManager.authenticateUser(email, password)) {
            is Result.Success<User> -> SignInUiModel.Success(result.data.id)
            else -> SignInUiModel.Failure(R.string.authentication_failure)
        }
}