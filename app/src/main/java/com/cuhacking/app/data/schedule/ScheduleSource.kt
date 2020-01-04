package com.cuhacking.app.data.schedule

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.DataInfoProvider
import com.cuhacking.app.data.api.ApiService
import com.cuhacking.app.data.db.Event
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleSource @Inject constructor(
    private val database: Database,
    private val api: ApiService,
    private val dataInfoProvider: DataInfoProvider,
    private val dispatchers: CoroutinesDispatcherProvider
) {

    suspend fun checkAndUpdateSchedule() = withContext(dispatchers.io) {
        val response = try {
            api.getSchedule()
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext
        }

        if (response.version > dataInfoProvider.scheduleDataVersion) {
            database.eventQueries.deleteAll()

            database.transaction {
                response.schedule.entries.forEach { (id, event) ->
                    database.eventQueries.insert(
                        id,
                        event.location,
                        event.title,
                        event.description,
                        event.startTime,
                        event.endTime,
                        event.type,
                        event.countdown,
                        event.scan
                    )
                }
            }
        }
    }

    fun getSchedule(): Flow<List<Event>> =
        database.eventQueries.getAll().asFlow().mapToList(dispatchers.io)

    fun getEvent(id: String): Flow<Event?> =
        database.eventQueries.getById(id).asFlow().mapToOneOrNull(dispatchers.io)
}