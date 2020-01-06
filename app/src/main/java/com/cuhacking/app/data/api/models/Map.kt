package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json
import org.threeten.bp.OffsetDateTime

data class MapDataResponse(
    @field:Json(name = "version") val version: OffsetDateTime,
    @field:Json(name = "map") val map: MapData
)

data class MapData(
    @field:Json(name = "map") val map: Map<String, Any>
)
