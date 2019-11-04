package com.cuhacking.app.signin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.signin.data.model.SignInUiModel
import com.cuhacking.app.signin.domain.SignInUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val signIn: SignInUseCase) : ViewModel() {
    private val _userData = MutableLiveData<SignInUiModel>()
    val userData: LiveData<SignInUiModel> = _userData

    fun performSignIn(email: String, password: String) = viewModelScope.launch {
        _userData.postValue(signIn(email, password))
    }
}