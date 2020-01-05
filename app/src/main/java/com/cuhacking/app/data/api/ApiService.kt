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

package com.cuhacking.app.data.api

import com.cuhacking.app.data.api.models.*
import com.cuhacking.app.profile.data.model.UserFromApi
import com.cuhacking.app.profile.data.model.UserResponse
import retrofit2.http.*

interface ApiService {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String, @Header("Authorization") auth: String): UserResponse

    @GET("updates")
    suspend fun getUpdates(): UpdatesResponse

    @GET("schedule")
    suspend fun getSchedule(): ScheduleResponse

    @POST("scan")
    suspend fun scanUser(@Body request: ScanRequest, @Header("Authorization") auth: String): String?

    @GET("map/version")
    suspend fun getMapDataVersion(): VersionResponse

    @GET("map")
    suspend fun getMapData(): MapDataResponse
}