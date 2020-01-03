package com.cuhacking.app.home.domain

import android.content.SharedPreferences
import javax.inject.Inject

class UpdateOnboardingFlagUseCase @Inject constructor(private val sharedPreferences: SharedPreferences) {
    operator fun invoke() {
        sharedPreferences.edit().putBoolean("onboard_login", false).apply()
    }
}