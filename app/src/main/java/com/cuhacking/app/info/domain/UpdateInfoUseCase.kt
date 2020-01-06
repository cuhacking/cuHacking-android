package com.cuhacking.app.info.domain

import com.cuhacking.app.info.data.InfoRepository
import javax.inject.Inject

class UpdateInfoUseCase @Inject constructor(private val repository: InfoRepository) {
    suspend operator fun invoke() {
        repository.updateInfo()
    }
}