package com.cuhacking.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.data.auth.AuthState
import com.cuhacking.app.domain.GetAuthStateUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val getAuthState: GetAuthStateUseCase) :
    ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        viewModelScope.launch {
            getAuthState().collect {
                _authState.postValue(it)
            }
        }
    }
}