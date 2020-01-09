package com.cuhacking.app.map.domain

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.api.models.EventType
import com.cuhacking.app.data.auth.UserRole
import com.cuhacking.app.schedule.data.models.EventUiModel
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class GetEventsInRoomUseCase @Inject constructor(
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(roomId: String): List<EventUiModel> = withContext(dispatchers.io) {
        val isAdmin =
            database.userQueries.getPrimary().executeAsOneOrNull()?.role == UserRole.ADMIN

        database.eventQueries.getByRoom(roomId).executeAsList()
            .map {
                EventUiModel(it.title, it.startTime, it.endTime, it.location, it.id, it.type)
            }
            .sortedBy { it.startTime }
            .filter { event ->
                if (isAdmin) {
                    true
                } else {
                    event.type.toLowerCase(Locale.CANADA) != EventType.VOLUNTEER.typeString
                }
            }
    }
}