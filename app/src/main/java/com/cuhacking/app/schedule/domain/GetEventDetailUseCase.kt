package com.cuhacking.app.schedule.domain

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.schedule.data.models.EventDetailUiModel
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class GetEventDetailUseCase @Inject constructor(
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    operator fun invoke(id: String): Flow<EventDetailUiModel?> =
        database.eventQueries.getById(id).asFlow().mapToOneOrNull(dispatchers.io)
            .map { event ->
                event ?: return@map null

                val startTime = ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(event.startTime / 1000),
                    ZoneOffset.UTC
                )
                val endTime = ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(event.endTime / 1000),
                    ZoneOffset.UTC
                )

                return@map EventDetailUiModel(
                    event.id,
                    event.title,
                    "${timeFormatter.format(startTime)} - ${timeFormatter.format(endTime)}",
                    event.locationName,
                    event.type,
                    event.description
                )
            }
}