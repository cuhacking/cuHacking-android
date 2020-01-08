package com.cuhacking.app.map.domain

import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.map.MapDataSource
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetMapDataSourceUseCase @Inject constructor(
    private val mapDataSource: MapDataSource,
    private val dispatchers: CoroutinesDispatcherProvider,
    private val moshi: Moshi
) {
    private val type =
        Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    private val adapter = moshi.adapter<Map<String, Any>>(type)
    private var source: GeoJsonSource? = null
    suspend operator fun invoke(): Flow<GeoJsonSource> =
        mapDataSource.getData().mapNotNull { data ->
            val json = adapter.toJson(data.map["RB"]?.geometry)
            if (source == null) {
                source = GeoJsonSource("rb", json)
            } else {
                source?.setGeoJson(json)
            }

            source
        }
            .flowOn(dispatchers.main)
}