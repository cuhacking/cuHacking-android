package com.cuhacking.app.schedule.domain

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.api.models.EventType
import com.cuhacking.app.data.auth.UserRole
import com.cuhacking.app.schedule.data.ScheduleRepository
import com.cuhacking.app.schedule.data.models.EventUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository,
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(): Flow<List<EventUiModel>> = withContext(dispatchers.io) {
        val isAdmin =
            database.userQueries.getPrimary().executeAsOneOrNull()?.role == UserRole.ADMIN ?: false

        return@withContext repository.getSchedule().map {
            it.sortedBy { event -> event.startTime }.map { event ->
                EventUiModel(
                    event.title,
                    event.startTime,
                    event.endTime,
                    event.location,
                    event.id,
                    event.type
                )
            }
                .filter { event ->
                    if (isAdmin) {
                        true
                    } else {
                        event.type.toLowerCase(Locale.CANADA) != EventType.VOLUNTEER.typeString
                    }
                }
        }
    }
}