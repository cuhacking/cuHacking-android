package com.cuhacking.app.profile.domain

import com.cuhacking.app.data.Result
import com.cuhacking.app.profile.data.ProfileRepository
import javax.inject.Inject

class LoadProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String, isPrimary: Boolean): Result<Unit> =
        repository.loadUser(userId, isPrimary)
}