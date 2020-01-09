package com.cuhacking.app.home.data

import com.cuhacking.app.Database
import com.cuhacking.app.R
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import kotlinx.coroutines.withContext
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import javax.inject.Inject

class HomeRepository @Inject constructor(private val database: Database,
                                         private val dispatchers: CoroutinesDispatcherProvider) {

    suspend fun getCountdownTime(): OffsetDateTime = withContext(dispatchers.io) {
        val startEvent = database.eventQueries.getById("hackStart").executeAsOneOrNull()
        val endEvent = database.eventQueries.getById("hackEnd").executeAsOneOrNull()

        if (startEvent != null && OffsetDateTime.now() < startEvent.startTime.plusMinutes(30)) {
            return@withContext startEvent.startTime
        }

        if (endEvent != null && OffsetDateTime.now() < endEvent.startTime.plusDays(1)) {
            return@withContext endEvent.startTime
        }

        return@withContext OffsetDateTime.of(2000, 1, 1, 1, 0, 0, 0, ZoneOffset.UTC)
    }

    suspend fun getCountdownMessage(): Int = withContext(dispatchers.io) {
        val startEvent = database.eventQueries.getById("hackStart").executeAsOneOrNull()
        val endEvent = database.eventQueries.getById("hackEnd").executeAsOneOrNull()

        if (startEvent != null && OffsetDateTime.now() < startEvent.startTime.plusMinutes(30)) {
            return@withContext R.string.countdown_hack_start
        }

        if (endEvent != null && OffsetDateTime.now() < endEvent.startTime.plusDays(1)) {
            return@withContext R.string.countdown_hack_end
        }

        return@withContext R.string.countdown_hack_over
    }
}