package com.cuhacking.app.schedule.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.schedule.data.models.EventDetailUiModel
import com.cuhacking.app.schedule.domain.GetEventDetailUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventDetailViewModel @Inject constructor(
    private val getEventDetail: GetEventDetailUseCase
) : ViewModel() {

    private val _details = MutableLiveData<EventDetailUiModel?>()
    val details: LiveData<EventDetailUiModel?> = _details

    fun init(eventId: String) = viewModelScope.launch {
        getEventDetail(eventId).collect {
            _details.postValue(it)
        }
    }
}