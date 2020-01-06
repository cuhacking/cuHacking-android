package com.cuhacking.app.admin.domain

import com.cuhacking.app.Database
import com.cuhacking.app.admin.ui.EventSelectUiModel
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class GetScanEventsUseCase @Inject constructor(
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    suspend operator fun invoke(): Flow<List<EventSelectUiModel>> =
        database.eventQueries.getAll().asFlow().mapToList(dispatchers.io)
            .map { list -> list.filter { it.scan } }
            .map { list -> list.sortedBy { it.startTime } }
            .map { list ->
                list.map {
                    EventSelectUiModel(
                        it.id,
                        it.title,
                        "${formatter.format(it.startTime)} - ${formatter.format(it.endTime)}",
                        it.startTime,
                        it.endTime
                    )
                }
            }
}