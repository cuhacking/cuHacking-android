package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json
import org.threeten.bp.OffsetDateTime

data class UpdatesResponse(
    @field:Json(name = "version") val version: OffsetDateTime,
    @field:Json(name = "updates") val updates: Map<String, Update>
)

data class Update(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "location") val location: String,
    @field:Json(name = "deliveryTime") val deliveryTime: OffsetDateTime
)