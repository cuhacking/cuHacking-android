package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json

data class UpdatesResponse(
    @field:Json(name = "version") val version: Long,
    @field:Json(name = "updates") val updates: Map<String, Update>
)

data class Update(
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "locationId") val locationId: String,
    @field:Json(name = "deliveryTime") val deliveryTime: Long,
    @field:Json(name = "eventId") val eventId: String
)