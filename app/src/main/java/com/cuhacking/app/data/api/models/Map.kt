package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json
import org.threeten.bp.OffsetDateTime

data class MapDataResponse(
    @field:Json(name = "version") val version: OffsetDateTime,
    @field:Json(name = "map") val map: MapData
)

data class MapData(
    @field:Json(name = "map") val map: Map<String, BuildingData>
)

data class FloorData(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "id") val id: String
)

data class BuildingData(
    @field:Json(name = "center") val center: List<Double>,
    @field:Json(name = "floors") val floors: List<FloorData>,
    @field:Json(name = "geometry") val geometry: Map<String, Any>
)