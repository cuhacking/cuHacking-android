package com.cuhacking.app.info.domain

import com.cuhacking.app.R
import com.cuhacking.app.info.data.InfoRepository
import com.cuhacking.app.ui.cards.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInfoCardsUseCase @Inject constructor(private val repository: InfoRepository) {
    suspend operator fun invoke(): Flow<List<Card>> {
        repository.getInfo()
        return repository.infoFlow.map { info ->
            listOf(
                WiFiCard(info.wifi.network, info.wifi.password),
                Header(R.string.header_emergency),
                EmergencyContactCard(info.emergency),
                Header(R.string.header_help),
                HelpCard(info.help),
                Header(R.string.header_social),
                SocialCard(info.social)
            )
        }
    }
}