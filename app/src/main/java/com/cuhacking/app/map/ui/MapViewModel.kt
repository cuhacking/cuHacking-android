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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.data.api.models.FloorData
import com.cuhacking.app.map.domain.GetEventsInRoomUseCase
import com.cuhacking.app.map.domain.GetMapDataUseCase
import com.cuhacking.app.map.domain.GetTargetBuildingUseCase
import com.cuhacking.app.map.domain.UpdateMapDataUseCase
import com.cuhacking.app.schedule.data.models.EventUiModel
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val getTargetBuilding: GetTargetBuildingUseCase,
    private val updateMapData: UpdateMapDataUseCase,
    private val getMapData: GetMapDataUseCase,
    private val getEventsInRoom: GetEventsInRoomUseCase
) :
    ViewModel() {

    private val _selectedFloor = MutableLiveData<MutableMap<String, String>>()
    val selectedFloor: LiveData<out Map<String, String>> = _selectedFloor

    private val _targetBuilding = MutableLiveData<Pair<String, List<FloorData>>?>()
    val targetBuilding: LiveData<Pair<String, List<FloorData>>?> = _targetBuilding

    private val _selectedRoom = MutableLiveData<String?>()
    val selectedRoom: LiveData<String?> = _selectedRoom

    private val _selectedEvents = MutableLiveData<List<EventUiModel>>()
    val selectedEvents: LiveData<List<EventUiModel>> = _selectedEvents

    private val _buildings = MutableLiveData<List<FloorDataUiModel>>()
    val buildings: LiveData<List<FloorDataUiModel>> = _buildings

    private var job: Job? = null

    init {
        viewModelScope.launch {
            updateMapData()
        }

        _selectedFloor.value = mutableMapOf()
    }

    fun setupData() {
        job?.cancel()
        job = viewModelScope.launch {
            getMapData().collect {
                _buildings.postValue(it)
            }
        }
    }

    fun selectFloor(building: String, floor: String) {
        val map = _selectedFloor.value ?: return
        map[building] = floor
        _selectedFloor.value = map
    }

    fun updateCenter(latLng: LatLng, zoom: Double) {
        viewModelScope.launch {
            val target = getTargetBuilding(latLng, zoom)
            if (target != _targetBuilding.value) {
                _targetBuilding.value = target
            }
        }
    }

    fun selectRoom(id: String?) {
        _selectedRoom.value = id
        _selectedEvents.value = emptyList()

        if (id != null) {
            viewModelScope.launch {
                _selectedEvents.value = getEventsInRoom(id)
            }
        }
    }

    fun cleanupSources() {
        getMapData.clearCachedSources()
    }
}
