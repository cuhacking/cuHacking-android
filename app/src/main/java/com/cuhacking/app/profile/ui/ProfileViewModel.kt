package com.cuhacking.app.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.profile.domain.GetProfileItemsUseCase
import com.cuhacking.app.profile.domain.LogoutUseCase
import com.cuhacking.app.profile.domain.UpdateUserUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getProfileItems: GetProfileItemsUseCase,
    private val logout: LogoutUseCase,
    private val updateUser: UpdateUserUseCase
) : ViewModel() {

    private val _profileItems = MutableLiveData<List<ProfileItem>>()
    val profileItems: LiveData<List<ProfileItem>> = _profileItems

    fun setUser(userId: String) {
        viewModelScope.launch {
            getProfileItems(userId).collect {
                _profileItems.postValue(it)
            }
        }

        viewModelScope.launch {
            updateUser(userId)
        }
    }

    fun logUserOut() {
        viewModelScope.launch {
            logout()
        }
    }
}