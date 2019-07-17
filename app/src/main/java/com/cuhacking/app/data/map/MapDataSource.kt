/*
 *    Copyright 2019 cuHacking
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cuhacking.app.data.map

import android.content.Context
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.DataInfoProvider
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

class MapDataSource @Inject constructor(
    private val context: Context,
    private val dataInfoProvider: DataInfoProvider,
    private val dispatchers: CoroutinesDispatcherProvider
) {

    suspend fun getFloorData(floor: Floor): GeoJsonSource = withContext(dispatchers.io) {
        if (!dataInfoProvider.mapDataCopied) {
            copyAssetData()
        }

        val file = File(context.filesDir, floor.fileName)
        if (!file.exists()) { throw FileNotFoundException("Could not find ${floor.fileName}") }

        return@withContext withContext(dispatchers.main) { GeoJsonSource(floor.id, file.readText()) }
    }

    private fun copyAssetData() {
        Floor.values().forEach { floor ->
            context.assets.open(floor.fileName).use { inputStream ->
                context.openFileOutput(floor.fileName, Context.MODE_PRIVATE).use { outputStream ->
                    outputStream.write(inputStream.readBytes())
                }
            }
        }

        dataInfoProvider.mapDataCopied = true
    }
}

enum class Floor(val fileName: String, val id: String) {
    LV01("LV01.geojson", "LV01"),
    LV02("LV02.geojson", "LV02"),
    LV03("LV03.geojson", "LV03"),
    LV04("LV04.geojson", "LV04")
}