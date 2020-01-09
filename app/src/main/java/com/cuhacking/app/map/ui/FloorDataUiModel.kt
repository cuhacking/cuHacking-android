package com.cuhacking.app.map.ui

import com.cuhacking.app.data.api.models.FloorData
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

data class FloorDataUiModel(
    val source: GeoJsonSource,
    val prefix: String,
    val floors: List<FloorData>,
    val center: LatLng
)