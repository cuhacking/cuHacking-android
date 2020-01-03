package com.cuhacking.app.home.domain

import android.content.SharedPreferences
import javax.inject.Inject

class ShouldShowOnboardingUseCase @Inject constructor(private val sharedPreferences: SharedPreferences) {
    operator fun invoke(): Boolean = sharedPreferences.getBoolean("onboard_login", true)
}