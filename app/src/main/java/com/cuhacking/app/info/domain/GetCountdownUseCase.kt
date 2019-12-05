package com.cuhacking.app.info.domain

import android.os.CountDownTimer
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.info.data.InfoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class GetCountdownUseCase @Inject constructor(
    private val repository: InfoRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(): Flow<Long> {
        val difference =
            repository.getCuHackingDate().timeInMillis - Calendar.getInstance().timeInMillis

        return flow {
            for (i in difference..0 step -1) {
                delay(1000)
                emit(i)
            }
        }.flowOn(dispatcher.computation)
    }
}