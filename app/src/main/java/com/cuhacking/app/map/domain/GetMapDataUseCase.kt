package com.cuhacking.app.map.domain

import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.api.models.BuildingData
import com.cuhacking.app.data.map.MapDataSource
import com.cuhacking.app.map.ui.FloorDataUiModel
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMapDataUseCase @Inject constructor(
    private val mapDataSource: MapDataSource,
    private val dispatchers: CoroutinesDispatcherProvider,
    moshi: Moshi
) {
    private val sourceMap = mutableMapOf<String, GeoJsonSource>()

    private val type =
        Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    private val adapter = moshi.adapter<Map<String, Any>>(type)

    suspend operator fun invoke(): Flow<List<FloorDataUiModel>> {
        return mapDataSource.getData().map { data ->
            val output = mutableListOf<FloorDataUiModel>()
            data.map["RB"]?.let { building ->
                output.add(makeBuilding(building, "RB"))
            }

            data.map["HS"]?.let { building ->
                output.add(makeBuilding(building, "HS"))
            }

            data.map["SC"]?.let { building ->
                output.add(makeBuilding(building, "SC"))
            }

            data.map["AP"]?.let { building ->
                output.add(makeBuilding(building, "AP"))
            }

            data.map["tunnels"]?.let { building ->
                output.add(makeBuilding(building, "tunnels"))
            }

            return@map output
        }
    }

    private fun makeBuilding(building: BuildingData, id: String): FloorDataUiModel {
        val json = adapter.toJson(building.geometry)
        if (sourceMap[id] == null) {
            sourceMap[id] = GeoJsonSource(id, json)
        } else {
            sourceMap[id]?.setGeoJson(json)
        }

        return FloorDataUiModel(
            sourceMap[id]!!, id, building.floors
        )
    }
}