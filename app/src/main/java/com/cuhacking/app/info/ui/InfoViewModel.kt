/*
 *    Copyright 2019 cuHacking
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cuhacking.app.info.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.data.Result
import com.cuhacking.app.info.domain.GetInfoCardsUseCase
import com.cuhacking.app.info.domain.RefreshInfoCardsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfoViewModel @Inject constructor(
    private val getInfoCards: GetInfoCardsUseCase,
    private val refreshInfoCards: RefreshInfoCardsUseCase
) :
    ViewModel() {

    private val _refreshState = MutableLiveData<Result<Unit>>()
    val refreshState: LiveData<Result<Unit>> = _refreshState

    private val _cards = MutableLiveData<List<InfoCard>>()
    val cards: LiveData<List<InfoCard>> = _cards

    init {
        viewModelScope.launch {
            getInfoCards().collect {
                _cards.postValue(it)
            }
        }
    }

    fun refreshInfo() = viewModelScope.launch {
        _refreshState.value = Result.Loading()
        _refreshState.postValue(refreshInfoCards())
    }

    // TODO: Implement the ViewModel
}
