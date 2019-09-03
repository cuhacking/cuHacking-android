package com.cuhacking.app.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.profile.domain.GetProfileItemsUseCase
import com.cuhacking.app.profile.domain.LoadProfileUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getProfileItems: GetProfileItemsUseCase,
    private val loadProfile: LoadProfileUseCase
) : ViewModel() {

    private val _profileItems = MutableLiveData<List<ProfileItem>>()
    val profileItems: LiveData<List<ProfileItem>> = _profileItems

    fun setUser(userId: String, imageSize: Int, isPrimary: Boolean) {
        viewModelScope.launch {
            getProfileItems(userId, imageSize)
        }
        viewModelScope.launch {
            loadProfile(userId, isPrimary)
            // TODO: Handle errors
        }
    }
}