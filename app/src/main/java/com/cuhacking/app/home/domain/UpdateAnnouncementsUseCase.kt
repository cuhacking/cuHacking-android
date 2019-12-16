package com.cuhacking.app.home.domain

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.Result
import com.cuhacking.app.data.api.ApiService
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateAnnouncementsUseCase @Inject constructor(
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider,
    private val api: ApiService
) {
    suspend operator fun invoke(): Result<Unit> = withContext(dispatchers.io) {
        val newUpdates = try {
            api.getUpdates()
        } catch (e: Exception) {
            return@withContext Result.Error<Unit>(e)
        }
        val oldUpdateIds = database.announcementQueries.getAll().executeAsList().map { it.id }

        // Insert updates that are not already contained in the database
        newUpdates.updates.filter { (id) -> !oldUpdateIds.contains(id) }.forEach { (id, update) ->
            database.announcementQueries.insert(
                id,
                update.title,
                update.description,
                update.locationId,
                update.deliveryTime,
                update.eventId
            )
        }

        // Remove updates from the database that are no longer live
        val newUpdateIds = newUpdates.updates.keys
        oldUpdateIds.filter { !newUpdateIds.contains(it) }.forEach(database.announcementQueries::delete)

        return@withContext Result.Success(Unit)
    }
}