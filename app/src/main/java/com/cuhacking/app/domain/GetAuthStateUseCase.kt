package com.cuhacking.app.domain

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.auth.AuthState
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    operator fun invoke(): Flow<AuthState> {
        return database.userQueries.getPrimary().asFlow().mapToOneOrNull(dispatchers.io)
            .map {
                when (it) {
                    null -> AuthState.Unauthenticated
                    else -> AuthState.Authenticated(it.id, it.role)
                }
            }
    }
}