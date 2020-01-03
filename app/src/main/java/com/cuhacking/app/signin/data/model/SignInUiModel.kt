package com.cuhacking.app.signin.data.model

import androidx.annotation.StringRes

sealed class SignInUiModel {
    /**
     * Represents the UI state of the success state of a sign in attempt.
     */
    object Success : SignInUiModel()

    object Loading : SignInUiModel()

    /**
     * Represents the UI state of the failure state of a sign in attempt.
     * @property errorMessage The resource id of a string that indicates what error has occurred.
     */
    data class Failure(@StringRes val errorMessage: Int) : SignInUiModel()
}