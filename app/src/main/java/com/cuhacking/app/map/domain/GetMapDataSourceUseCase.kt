package com.cuhacking.app.map.domain

import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.map.MapDataSource
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetMapDataSourceUseCase @Inject constructor(
    private val mapDataSource: MapDataSource,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    private var source: GeoJsonSource? = null
    suspend operator fun invoke(): Flow<GeoJsonSource> =
        mapDataSource.getData().mapNotNull { data ->
            if (source == null) {
                source = GeoJsonSource("rb", data)
            } else {
                source?.setGeoJson(data)
            }

            source
        }
            .flowOn(dispatchers.main)
}