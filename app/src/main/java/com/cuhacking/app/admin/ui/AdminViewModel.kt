package com.cuhacking.app.admin.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.admin.domain.GetNextEventUseCase
import com.cuhacking.app.admin.domain.GetScanEventsUseCase
import com.cuhacking.app.admin.domain.ScanQrCodeUseCase
import com.cuhacking.app.data.db.Event
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdminViewModel @Inject constructor(
    private val scanQrCode: ScanQrCodeUseCase,
    private val getScanEvents: GetScanEventsUseCase,
    private val getNextEvent: GetNextEventUseCase
) : ViewModel() {
    private val _scanStatus = MutableLiveData<ScanResponseUiModel?>()
    val scanStatus: LiveData<ScanResponseUiModel?> = _scanStatus

    private val _scanEvents = MutableLiveData<List<EventSelectUiModel>>()
    val scanEvents: LiveData<List<EventSelectUiModel>> = _scanEvents

    private val _selectedEvent = MutableLiveData<EventSelectUiModel>()
    val selectedEvent: LiveData<EventSelectUiModel> = _selectedEvent

    init {
        viewModelScope.launch {
            scanQrCode.flow.collect {
                _scanStatus.postValue(it)
            }
        }

        viewModelScope.launch {
            getScanEvents.invoke().collect {
                _scanEvents.postValue(it)
                _selectedEvent.postValue(getNextEvent(it))
            }
        }
    }

    fun scan(qrCodes: List<FirebaseVisionBarcode>) {
        scanQrCode(qrCodes, "bcnOvGUM")
    }


}
