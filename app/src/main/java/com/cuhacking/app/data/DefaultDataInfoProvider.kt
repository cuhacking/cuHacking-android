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

package com.cuhacking.app.data

import android.content.SharedPreferences
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class DefaultDataInfoProvider @Inject constructor(private val sharedPreferences: SharedPreferences) :
    DataInfoProvider {

    override var mapDataCopied: Boolean
        get() = sharedPreferences.getBoolean(KEY_MAP_DATA_COPIED, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_MAP_DATA_COPIED, value).apply()
        }

    override var scheduleDataVersion: OffsetDateTime
        get() = OffsetDateTime.parse(
            sharedPreferences.getString(
                KEY_SCHEDULE_DATA_VERSION, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                    RANDOM_OLD_DATE
                )
            )
        )
        set(value) {
            sharedPreferences.edit().putString(
                KEY_SCHEDULE_DATA_VERSION,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)
            )
                .apply()
        }

    override var mapDataVersion: OffsetDateTime
        get() = OffsetDateTime.parse(
            sharedPreferences.getString(
                KEY_MAP_DATA_VERSION, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                    RANDOM_OLD_DATE
                )
            )
        )
        set(value) {
            sharedPreferences.edit().putString(
                KEY_MAP_DATA_VERSION,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)
            )
                .apply()
        }

    companion object {
        const val KEY_MAP_DATA_COPIED = "map_data_copied"
        const val KEY_MAP_DATA_VERSION = "map_data_version"
        const val KEY_SCHEDULE_DATA_VERSION = "schedule_data_version"

        const val INFO_PREF = "data_info"
        private val RANDOM_OLD_DATE = OffsetDateTime.of(2000, 5, 5, 5, 5, 5, 0, ZoneOffset.UTC)
    }
}