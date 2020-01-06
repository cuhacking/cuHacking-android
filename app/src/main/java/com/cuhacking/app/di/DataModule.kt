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

package com.cuhacking.app.di

import android.content.Context
import android.content.SharedPreferences
import com.cuhacking.app.BuildConfig
import com.cuhacking.app.Database
import com.cuhacking.app.data.DataInfoProvider
import com.cuhacking.app.data.DefaultDataInfoProvider
import com.cuhacking.app.data.api.ApiService
import com.cuhacking.app.data.auth.UserRole
import com.cuhacking.app.data.db.Announcement
import com.cuhacking.app.data.db.Event
import com.cuhacking.app.data.db.User
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideDataInfoProvider(sharedPreference: SharedPreferences): DataInfoProvider =
        DefaultDataInfoProvider(sharedPreference)

    @Provides
    @Singleton
    fun provideApiService(converter: MoshiConverterFactory, client: OkHttpClient): ApiService =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(converter)
            .apply {
                if (BuildConfig.DEBUG) {
                    client(client)
                }
            }
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database =
        Database(
            AndroidSqliteDriver(Database.Schema, context, "cuHacking.db"),
            userAdapter = User.Adapter(roleAdapter = UserRoleAdapter),
            announcementAdapter = Announcement.Adapter(deliveryTimeAdapter = OffsetDateTimeAdapter),
            eventAdapter = Event.Adapter(
                startTimeAdapter = OffsetDateTimeAdapter,
                endTimeAdapter = OffsetDateTimeAdapter
            )
        )

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(OffsetDateTime::class.java, Rfc3339OffsetDateTimeJsonAdapter)
        .build()

    companion object {
        private val OffsetDateTimeAdapter = object : ColumnAdapter<OffsetDateTime, String> {
            override fun decode(databaseValue: String): OffsetDateTime =
                OffsetDateTime.parse(databaseValue)

            override fun encode(value: OffsetDateTime): String =
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)
        }

        private val UserRoleAdapter = object : ColumnAdapter<UserRole, Long> {
            override fun decode(databaseValue: Long): UserRole =
                UserRole.values()[databaseValue.toInt()]

            override fun encode(value: UserRole): Long = value.ordinal.toLong()
        }

        private val Rfc3339OffsetDateTimeJsonAdapter = object : JsonAdapter<OffsetDateTime>() {
            override fun fromJson(reader: JsonReader): OffsetDateTime? {
                if (reader.peek() == JsonReader.Token.NULL) {
                    return reader.nextNull()
                }

                return OffsetDateTime.parse(reader.nextString())
            }

            override fun toJson(writer: JsonWriter, value: OffsetDateTime?) {
                if (value == null) {
                    writer.nullValue()
                } else {
                    writer.value(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value))
                }
            }
        }
    }
}