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

package com.cuhacking.app.info.data

import android.content.SharedPreferences
import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.api.ApiService
import com.cuhacking.app.data.api.models.Info
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.util.*
import javax.inject.Inject

class InfoRepository @Inject constructor(
    val database: Database,
    val dispatchers: CoroutinesDispatcherProvider,
    val api: ApiService,
    val moshi: Moshi,
    val sharedPreferences: SharedPreferences
) {

    private val infoChannel = ConflatedBroadcastChannel<Info>()
    val infoFlow = infoChannel.asFlow().flowOn(dispatchers.io)

    suspend fun getInfo() {
        val data = sharedPreferences.getString(
            KEY_INFO_DATA,
            "{\"wifi\":{\"network\":\"cuHacking 2020\",\"password\":\"richcraft\"},\"emergency\":\"613-520-4444\",\"help\":\"For help, look for a volunteer (purple shirt) or an organizer (black shirt).\",\"social\":{\"twitter\":\"https://twitter.com/cuHacking/\",\"facebook\":\"https://www.facebook.com/cuhacking/\",\"instagram\":\"https://instagram.com/cuHacking\",\"slack\":\"https://cuhacking.slack.com/\"}}"
        )
        if (data != null) {
            infoChannel.offer(moshi.adapter(Info::class.java).fromJson(data)!!)
        }
    }

    suspend fun updateInfo() = withContext(dispatchers.io) {
        try {
            val data = api.getInfo()

            sharedPreferences.edit()
                .putString(KEY_INFO_DATA, moshi.adapter(Info::class.java).toJson(data.info)).apply()
            infoChannel.offer(data.info)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCuHackingDate() = Calendar.getInstance().apply {
        set(2020, 1, 11, 9, 0)
    }

    fun getWifiInfo(): WifiInfo = if (LocalDate.now().isBefore(LocalDate.of(2019, 12, 10))) {
        WifiInfo("Local Hack Day", "cuhacking")
    } else {
        WifiInfo("cuHacking 2020", "richcraft")
    }

    companion object {
        const val KEY_INFO_DATA = "info_data"
    }
}