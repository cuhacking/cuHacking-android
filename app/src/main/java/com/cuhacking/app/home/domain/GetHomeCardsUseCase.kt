package com.cuhacking.app.home.domain

import com.cuhacking.app.Database
import com.cuhacking.app.R
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.home.data.HomeRepository
import com.cuhacking.app.ui.cards.CountdownCard
import com.cuhacking.app.ui.cards.Header
import com.cuhacking.app.ui.cards.UpdateCard
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHomeCardsUseCase @Inject constructor(
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider,
    private val repository: HomeRepository
) {
    operator fun invoke() =
        database.announcementQueries.getAll().asFlow().mapToList(dispatchers.io)
            .map {
                it.map { update ->
                    UpdateCard(
                        update.id,
                        update.name,
                        update.description,
                        update.location,
                        update.deliveryTime
                    )
                }
            }
            .map {
                if (it.isNotEmpty()) {
                    listOf(Header(R.string.header_announcements)) + it
                } else {
                    it
                }
            }.map {
                listOf(
                    CountdownCard(
                        repository.getCountdownMessage(),
                        repository.getCountdownTime()
                    )
                ) + it
            }
}
