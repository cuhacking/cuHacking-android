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

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.Result
import com.cuhacking.app.data.api.ApiService
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.util.*
import javax.inject.Inject

class InfoRepository @Inject constructor(
    val database: Database,
    val dispatchers: CoroutinesDispatcherProvider,
    val api: ApiService
) {

    fun getCuHackingDate() = Calendar.getInstance().apply {
        set(2020, 1, 11, 9, 0)
    }

    fun getWifiInfo(): WifiInfo = if(LocalDate.now().isBefore(LocalDate.of(2019, 12, 10))) {
        WifiInfo("Local Hack Day", "cuhacking")
    } else {
        WifiInfo("cuHacking 2020", "richcraft")
    }



    suspend fun getUpdates() = database.announcementQueries.getAll().asFlow().mapToList(dispatchers.io)
}