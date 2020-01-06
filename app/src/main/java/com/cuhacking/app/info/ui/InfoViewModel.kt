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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.data.Result
import com.cuhacking.app.info.data.WifiInfo
import com.cuhacking.app.info.domain.GetInfoCardsUseCase
import com.cuhacking.app.info.domain.UpdateInfoUseCase
import com.cuhacking.app.info.domain.WifiInstaller
import com.cuhacking.app.ui.cards.Card
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfoViewModel @Inject constructor(
    private val getInfoCards: GetInfoCardsUseCase,
    private val wifiInstaller: WifiInstaller,
    private val updateInfoUseCase: UpdateInfoUseCase
) :
    ViewModel() {

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    init {
        viewModelScope.launch {
            getInfoCards().collect { cards ->
                _cards.postValue(cards)
            }
        }

        viewModelScope.launch {
            updateInfoUseCase()
        }
    }

    fun setupWifi(wifiInfo: WifiInfo) {
        wifiInstaller.installWifi(wifiInfo)
    }
}
