package com.cuhacking.app.info.domain

import com.cuhacking.app.R
import com.cuhacking.app.info.data.InfoRepository
import com.cuhacking.app.ui.cards.Card
import com.cuhacking.app.ui.cards.Header
import com.cuhacking.app.ui.cards.WiFiCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInfoCardsUseCase @Inject constructor(private val repository: InfoRepository) {
    suspend operator fun invoke(): Flow<List<Card>> {
        repository.getInfo()
        return repository.infoFlow.map { info ->
            listOf(
                Header(R.string.header_info),
                WiFiCard(info.wifi.network, info.wifi.password)
            )
        }
    }
}