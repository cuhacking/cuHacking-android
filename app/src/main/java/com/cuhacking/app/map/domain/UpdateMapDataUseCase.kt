package com.cuhacking.app.map.domain

import com.cuhacking.app.data.map.MapDataSource
import javax.inject.Inject

class UpdateMapDataUseCase @Inject constructor(private val mapDataSource: MapDataSource) {
    suspend operator fun invoke() {
        mapDataSource.checkAndUpdateData()
    }
}