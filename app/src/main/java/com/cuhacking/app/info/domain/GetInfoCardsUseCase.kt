package com.cuhacking.app.info.domain

import com.cuhacking.app.R
import com.cuhacking.app.info.data.InfoRepository
import com.cuhacking.app.ui.cards.Header
import com.cuhacking.app.ui.cards.Card
import com.cuhacking.app.ui.cards.UpdateCard
import com.cuhacking.app.ui.cards.WiFiCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInfoCardsUseCase @Inject constructor(private val repository: InfoRepository) {
    operator fun invoke(): List<Card> {
        val (ssid, password) = repository.getWifiInfo()
        return listOf(
            Header(R.string.header_info),
            WiFiCard(ssid, password)
        )
    }

}