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

package com.cuhacking.app.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.data.map.Floor
import com.cuhacking.app.data.map.MapDataSource
import com.cuhacking.app.map.domain.GetMapDataSourceUseCase
import com.cuhacking.app.map.domain.UpdateMapDataUseCase
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(private val getMapDataSource: GetMapDataSourceUseCase,
                                       private val updateMapData: UpdateMapDataUseCase) :
    ViewModel() {

    private val _floorSource = MutableLiveData<GeoJsonSource>()
    val floorSource: LiveData<GeoJsonSource> = _floorSource

    private val _selectedFloor = MutableLiveData<Floor>()
    val selectedFloor: LiveData<Floor> = _selectedFloor

    private val _selectedRoom = MutableLiveData<String>()
    val selectedRoom: LiveData<String> = _selectedRoom

    init {
        viewModelScope.launch {
            getMapDataSource().collect {
                _floorSource.postValue(it)
            }
        }

        viewModelScope.launch {
            updateMapData()
        }
    }

    fun setFloor(floor: Floor) {
        _selectedFloor.value = floor
    }

    fun selectRoom(id: String) {
        _selectedRoom.value = id
    }

}
