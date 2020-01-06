package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json
import org.threeten.bp.OffsetDateTime

data class ScheduleResponse(
    @field:Json(name = "version") val version: OffsetDateTime,
    @field:Json(name = "schedule") val schedule: Map<String, ScheduleEvent>
)


data class ScheduleEvent(
    @field:Json(name = "location") val location: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "startTime") val startTime: OffsetDateTime,
    @field:Json(name = "endTime") val endTime: OffsetDateTime,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "countdown") val countdown: Boolean,
    @field:Json(name = "scan") val scan: Boolean
)