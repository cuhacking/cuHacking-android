package com.cuhacking.app.schedule.domain

import com.cuhacking.app.schedule.data.ScheduleRepository
import com.cuhacking.app.schedule.data.models.EventUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(private val repository: ScheduleRepository) {
    suspend operator fun invoke(): Flow<List<EventUiModel>> {
        repository.updateSchedule()

        return repository.getSchedule().map {
            it.sortedBy { event -> event.startTime }.map { event ->
                EventUiModel(
                    event.title, ZonedDateTime.ofInstant(
                        Instant.ofEpochSecond(event.startTime / 1000),
                        ZoneOffset.UTC
                    ), ZonedDateTime.ofInstant(
                        Instant.ofEpochSecond(event.endTime / 1000),
                        ZoneOffset.UTC
                    ), event.locationName, event.id
                )
            }
        }
    }
}