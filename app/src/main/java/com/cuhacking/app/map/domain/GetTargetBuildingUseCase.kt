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
    suspend operator fun invoke(latLng: LatLng): List<FloorData>? {
        val data = mapDataSource.getData().single()

        val rbDist = calculateDistance(latLng, data, "RB")
        val hsDist = calculateDistance(latLng, data, "HS")
        val scDist = calculateDistance(latLng, data, "SC")
        val apDist = calculateDistance(latLng, data, "AP")

        return when (min(min(min(rbDist, hsDist), scDist), apDist)) {
            rbDist -> data.map.getValue("RB").floors
            hsDist -> data.map.getValue("HS").floors
            scDist -> data.map.getValue("SC").floors
            apDist -> data.map.getValue("AP").floors
            else -> null
        }
    }

    private fun calculateDistance(to: LatLng, data: MapData, code: String): Double {
        val building = data.map[code] ?: return Double.POSITIVE_INFINITY

        return LatLng(building.center[1], building.center[0]).distanceTo(to)
    }
}