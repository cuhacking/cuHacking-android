package com.cuhacking.app.info.domain

import com.cuhacking.app.R
import com.cuhacking.app.info.data.InfoRepository
import com.cuhacking.app.info.ui.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class GetInfoCardsUseCase @Inject constructor(private val repository: InfoRepository) {
    suspend operator fun invoke(): Flow<List<InfoCard>> {
        return repository.getUpdates()
            .map {
                it.filter {
                    update ->
                    update.deliveryTime < Date().time
                }.map { update ->
                    UpdateCard(
                        update.id,
                        update.title,
                        update.description,
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
                val (ssid, password) = repository.getWifiInfo()
                listOf(Header(R.string.header_info), WiFiCard(ssid, password)) + it
            }.map {
                listOf(Title()) + it
            }
    }

}