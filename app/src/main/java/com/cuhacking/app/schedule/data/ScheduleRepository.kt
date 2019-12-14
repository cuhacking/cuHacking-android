package com.cuhacking.app.schedule.data

import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.schedule.ScheduleSource
import javax.inject.Inject

class ScheduleRepository @Inject constructor(private val dispatchers: CoroutinesDispatcherProvider,
                                             private val scheduleSource: ScheduleSource) {
    fun getSchedule() = scheduleSource.getSchedule()

    suspend fun updateSchedule() = scheduleSource.checkAndUpdateSchedule()
}