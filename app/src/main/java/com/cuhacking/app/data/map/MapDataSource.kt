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
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapDataSource @Inject constructor(
    private val context: Context,
    private val dataInfoProvider: DataInfoProvider,
    private val dispatchers: CoroutinesDispatcherProvider
) {

    private val dataChannel = ConflatedBroadcastChannel<String>()

    suspend fun getData(): GeoJsonSource = withContext(dispatchers.io) {
        if (!dataInfoProvider.mapDataCopied) {
            copyAssetData()
        }

        val file = File(context.filesDir, "RB.geojson")
        if (!file.exists()) { throw FileNotFoundException("Could not find RB.geojson") }

        return@withContext withContext(dispatchers.main) { GeoJsonSource("rb", file.readText()) }
    }

    private fun copyAssetData() {
        context.assets.open("RB.geojson").use { inputStream ->
            context.openFileOutput("RB.geojson", Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(inputStream.readBytes())
            }
        }

        dataInfoProvider.mapDataCopied = true
    }
}
