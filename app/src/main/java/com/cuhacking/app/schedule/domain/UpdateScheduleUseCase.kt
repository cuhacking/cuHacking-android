package com.cuhacking.app.schedule.domain

import com.cuhacking.app.schedule.data.ScheduleRepository
import javax.inject.Inject

class UpdateScheduleUseCase @Inject constructor(private val repository: ScheduleRepository) {
    suspend operator fun invoke() {
        repository.updateSchedule()
    }
}