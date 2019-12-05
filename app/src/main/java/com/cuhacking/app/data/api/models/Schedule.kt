package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json

data class ScheduleResponse(
    @field:Json(name = "version") val version: Long,
    @field:Json(name = "events") val events: Map<String, ScheduleEvent>
)


data class ScheduleEvent(
    @field:Json(name = "locationName") val locationName: String,
    @field:Json(name = "locationId") val locationId: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "startTime") val startTime: Long,
    @field:Json(name = "endTime") val endTime: Long,
    @field:Json(name = "type") val type: String
)