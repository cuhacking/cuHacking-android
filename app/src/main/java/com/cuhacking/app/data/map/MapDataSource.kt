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
import com.cuhacking.app.data.api.ApiService
import com.cuhacking.app.data.api.models.MapData
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapDataSource @Inject constructor(
    private val context: Context,
    private val dataInfoProvider: DataInfoProvider,
    private val dispatchers: CoroutinesDispatcherProvider,
    private val api: ApiService,
    private val moshi: Moshi
) {

    private val dataChannel = ConflatedBroadcastChannel<MapData>()

    suspend fun getData() = withContext(dispatchers.io) {
        if (!dataInfoProvider.mapDataCopied) {
            copyAssetData()
        } else {
            loadData()
        }

        val file = File(context.filesDir, "RB.geojson")
        if (!file.exists()) {
            throw FileNotFoundException("Could not find RB.geojson")
        }

        dataChannel.asFlow()
    }

    suspend fun checkAndUpdateData() = withContext(dispatchers.io) {
        val versionData = api.getMapDataVersion()
        if (versionData.version > dataInfoProvider.mapDataVersion) {
            val newData = api.getMapData()
            context.openFileOutput(DATA_FILE, Context.MODE_PRIVATE).use { outputStream ->
                val adapter = moshi.adapter(MapData::class.java)
                outputStream.write(adapter.toJson(newData.map).toByteArray())
            }
        }
    }

    private suspend fun copyAssetData() = withContext(dispatchers.io) {
        context.assets.open("RB.geojson").use { inputStream ->
            context.openFileOutput(DATA_FILE, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(inputStream.readBytes())
            }
        }

        loadData()
        dataInfoProvider.mapDataCopied = true
    }

    private suspend fun loadData() = withContext(dispatchers.io) {
        context.openFileInput(DATA_FILE).use { inputStream ->
            val bytes = inputStream.readBytes()
            val adapter = moshi.adapter(MapData::class.java)
            dataChannel.offer(adapter.fromJson(bytes.toString(Charsets.UTF_8))!!)
        }
    }

    companion object {
        const val DATA_FILE = "RB.geojson"
    }
}
