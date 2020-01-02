package com.cuhacking.app.admin.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuhacking.app.admin.domain.ScanQrCodeUseCase
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdminViewModel @Inject constructor(private val scanQrCode: ScanQrCodeUseCase) : ViewModel() {
    private val _scanStatus = MutableLiveData<ScanResponseUiModel?>()
    val scanStatus: LiveData<ScanResponseUiModel?> = _scanStatus

    init {
        viewModelScope.launch {
            scanQrCode.flow.collect {
                _scanStatus.postValue(it)
            }
        }
    }


    fun scan(qrCodes: List<FirebaseVisionBarcode>) {
        scanQrCode(qrCodes, "id000")
    }
}
