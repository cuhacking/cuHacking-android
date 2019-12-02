package com.cuhacking.app.info.domain

import com.cuhacking.app.data.Result
import com.cuhacking.app.info.data.InfoRepository
import javax.inject.Inject

class RefreshInfoCardsUseCase @Inject constructor(private val repository: InfoRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshUpdates()
}