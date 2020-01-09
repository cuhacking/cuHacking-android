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

package com.cuhacking.app.schedule.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.schedule.data.models.EventUiModel
import com.cuhacking.app.schedule.domain.GetScheduleUseCase
import com.cuhacking.app.schedule.domain.UpdateScheduleUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val getSchedule: GetScheduleUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase
) : ViewModel() {
    private val _scheduleData = MutableLiveData<List<EventUiModel>>()
    val scheduleData: LiveData<List<EventUiModel>> = _scheduleData

    init {
        viewModelScope.launch {
            getSchedule().collect {
                _scheduleData.postValue(it)
            }
        }

        viewModelScope.launch {
            updateScheduleUseCase()
        }
    }

    fun getNextEventIndex(): Int {
        scheduleData.value?.forEachIndexed { index, event ->
            if (event.startTime > OffsetDateTime.now()) return index
        }

        return -1
    }
}
