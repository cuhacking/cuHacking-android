package com.cuhacking.app.profile.domain

import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.profile.data.ProfileRepository
import com.cuhacking.app.profile.ui.ProfileItem
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProfileItemsUseCase @Inject constructor(
    private val repository: ProfileRepository,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(userId: String): Flow<List<ProfileItem>> =
        withContext(dispatchers.io) {
            return@withContext repository.getUser(userId)
                .map { user ->
                    listOf(
                        ProfileItem.Header(repository.getUserQRCode(userId), user.name),
                        ProfileItem.FoodGroup("Cyan", "Cyan"),
                        ProfileItem.School(user.school),
                        ProfileItem.Email(user.email)
                    )
                }
        }
}