package com.cuhacking.app.admin.domain

import com.cuhacking.app.R
import com.cuhacking.app.admin.ui.ScanResponseUiModel
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.api.ApiService
import com.cuhacking.app.data.api.models.ScanRequest
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ScanQrCodeUseCase @Inject constructor(
    private val api: ApiService,
    dispatchers: CoroutinesDispatcherProvider,
    private val auth: FirebaseAuth
) {
    private var lastCode: String? = null

    private val channel = ConflatedBroadcastChannel<Pair<String, List<FirebaseVisionBarcode>>>()
    val flow = channel.asFlow()
        .filter(::filterEvents)
        .map(::processCodes)
        .flowOn(dispatchers.io)

    @Suppress("RedundantSuspendModifier")
    private suspend fun filterEvents(request: Pair<String, List<FirebaseVisionBarcode>>): Boolean {
        val (_, list) = request

        return when {
            list.isEmpty() -> {
                lastCode = null
                false
            }
            list.size == 1 && lastCode == list[0].rawValue -> false
            list.size > 1 && lastCode == MULTI_SCAN_CODE -> false
            else -> true
        }

    }

    private suspend fun processCodes(request: Pair<String, List<FirebaseVisionBarcode>>): ScanResponseUiModel {
        val (eventId, list) = request

        val user =
            auth.currentUser ?: return ScanResponseUiModel(false, R.string.scan_error_generic)

        return when {
            list.size > 1 -> {
                lastCode = MULTI_SCAN_CODE
                ScanResponseUiModel(false, R.string.scan_error_too_many)
            }
            else -> try {
                val token = Tasks.await(user.getIdToken(false)).token ?: return ScanResponseUiModel(
                    false,
                    R.string.scan_error_generic
                )

                lastCode = list[0].rawValue
                api.scanUser(ScanRequest(list[0].rawValue ?: "", eventId), token)
                ScanResponseUiModel(true, R.string.scan_success)

            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> ScanResponseUiModel(false, R.string.scan_error_already)
                    404 -> ScanResponseUiModel(false, R.string.scan_error_not_found)
                    else -> ScanResponseUiModel(false, R.string.scan_error_generic)
                }
            } catch (e: Exception) {
                ScanResponseUiModel(false, R.string.scan_error_generic)
            }
        }

    }

    operator fun invoke(
        qrCodes: List<FirebaseVisionBarcode>,
        eventId: String
    ) {
        channel.offer(eventId to qrCodes)
    }

    companion object {
        const val MULTI_SCAN_CODE = "multi!"
    }
}