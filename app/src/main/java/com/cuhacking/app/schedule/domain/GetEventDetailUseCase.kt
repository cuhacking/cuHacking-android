package com.cuhacking.app.schedule.domain

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.schedule.data.models.EventDetailUiModel
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
                return@map EventDetailUiModel(
                    event.id,
                    event.title,
                    "${timeFormatter.format(event.startTime)} - ${timeFormatter.format(event.endTime)}",
                    event.location,
                    event.type,
                    event.description
                )
            }
}