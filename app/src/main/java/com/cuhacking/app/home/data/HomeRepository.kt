package com.cuhacking.app.home.data

import com.cuhacking.app.Database
import com.cuhacking.app.R
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.Result
import com.cuhacking.app.data.api.ApiService
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class HomeRepository @Inject constructor() {
    fun getCountdownTime() =
        if (LocalDateTime.now().isBefore(LocalDateTime.of(2020, 1, 11, 10, 30))) {
            LocalDateTime.of(2020, 1, 11, 10, 0)
        } else {
            LocalDateTime.of(2020, 1, 12, 10, 0)
        }

    fun getCountdownMessage() =
        if (LocalDateTime.now().isBefore(LocalDateTime.of(2020, 1, 11, 10, 30))) {
            R.string.countdown_hack_start
        } else {
            R.string.countdown_hack_end
        }
}