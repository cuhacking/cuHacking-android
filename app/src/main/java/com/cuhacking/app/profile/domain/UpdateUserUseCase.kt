package com.cuhacking.app.profile.domain

import com.cuhacking.app.profile.data.ProfileRepository
import com.cuhacking.app.signin.data.AuthenticationManager
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(uid: String) = repository.loadUser(uid)
}