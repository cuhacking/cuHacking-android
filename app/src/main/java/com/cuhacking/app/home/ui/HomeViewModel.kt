package com.cuhacking.app.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.data.Result
import com.cuhacking.app.home.domain.GetHomeCardsUseCase
import com.cuhacking.app.home.domain.ShouldShowOnboardingUseCase
import com.cuhacking.app.home.domain.UpdateAnnouncementsUseCase
import com.cuhacking.app.home.domain.UpdateOnboardingFlagUseCase
import com.cuhacking.app.ui.cards.Card
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val updateAnnouncements: UpdateAnnouncementsUseCase,
    private val getHomeCards: GetHomeCardsUseCase,
    private val shouldShowOnboarding: ShouldShowOnboardingUseCase,
    private val updateOnBoardingFlag: UpdateOnboardingFlagUseCase
) : ViewModel() {

    private val _refreshState = MutableLiveData<Result<Unit>>()
    val refreshState: LiveData<Result<Unit>> = _refreshState

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    private val _showOnboarding = MutableLiveData<Boolean>()
    val showOnboarding: LiveData<Boolean> = _showOnboarding

    init {
        viewModelScope.launch {
            getHomeCards().collect {
                _cards.postValue(it)
            }
        }

        _showOnboarding.value = shouldShowOnboarding()
    }

    fun refreshInfo() = viewModelScope.launch {
        _refreshState.value = Result.Loading()
        _refreshState.postValue(updateAnnouncements())
    }

    fun dismissOnboarding() {
        updateOnBoardingFlag()
        _showOnboarding.value = false
    }

}