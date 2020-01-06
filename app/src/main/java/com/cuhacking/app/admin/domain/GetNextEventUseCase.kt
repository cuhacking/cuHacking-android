package com.cuhacking.app.admin.domain

import com.cuhacking.app.admin.ui.EventSelectUiModel
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class GetNextEventUseCase @Inject constructor() {
    operator fun invoke(events: List<EventSelectUiModel>): EventSelectUiModel? {
        val now = OffsetDateTime.now()
        events.forEach {
            if (it.startTime > now) {
                return it
            }
        }

        return null
    }
}