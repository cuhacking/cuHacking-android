package com.cuhacking.app.map.domain

import com.cuhacking.app.data.api.models.FloorData
import com.cuhacking.app.data.api.models.MapData
import com.cuhacking.app.data.map.MapDataSource
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.flow.single
import javax.inject.Inject
import kotlin.math.min

class GetTargetBuildingUseCase @Inject constructor(
    private val mapDataSource: MapDataSource
) {
    suspend operator fun invoke(latLng: LatLng, zoom: Double): Pair<String, List<FloorData>>? {
        if (zoom <= 17) return null
        val data = mapDataSource.dataChannel.valueOrNull ?: return null

        val rbDist = calculateDistance(latLng, data, "RB")
        val hsDist = calculateDistance(latLng, data, "HS")
        val scDist = calculateDistance(latLng, data, "SC")
        val apDist = calculateDistance(latLng, data, "AP")

        return when (min(min(min(rbDist, hsDist), scDist), apDist)) {
            rbDist -> "RB" to data.map.getValue("RB").floors
            hsDist -> "HS" to data.map.getValue("HS").floors
            scDist -> "SC" to data.map.getValue("SC").floors
            apDist -> "AP" to data.map.getValue("AP").floors
            else -> null
        }
    }

    private fun calculateDistance(to: LatLng, data: MapData, code: String): Double {
        val building = data.map[code] ?: return Double.POSITIVE_INFINITY

        return LatLng(building.center[1], building.center[0]).distanceTo(to)
    }
}