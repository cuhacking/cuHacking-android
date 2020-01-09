package com.cuhacking.app.schedule.data.models

import androidx.annotation.ColorRes
import org.threeten.bp.OffsetDateTime

data class EventDetailUiModel(
    val id: String,
    val title: String,
    val start: OffsetDateTime,
    val end: OffsetDateTime,
    val locationName: String,
    val category: String,
    val description: String,
    @ColorRes val typeColor: Int
)