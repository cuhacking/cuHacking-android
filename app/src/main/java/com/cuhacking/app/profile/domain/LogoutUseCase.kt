package com.cuhacking.app.profile.domain

import com.cuhacking.app.signin.data.AuthenticationManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val manager: AuthenticationManager) {
    suspend operator fun invoke() {
        manager.signOut()
    }
}