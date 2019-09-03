package com.cuhacking.app.profile.domain

import com.cuhacking.app.profile.data.ProfileRepository
import com.cuhacking.app.profile.ui.ProfileItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProfileItemsUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String, imageSize: Int): Flow<List<ProfileItem>> {
        val bitmap = repository.getUserQRCode(userId, imageSize)

        return repository.getUser(userId)
            .map { user ->
                listOf(
                    ProfileItem.Header(bitmap, user.name),
                    ProfileItem.FoodGroup("Cyan", "Cyan")
                )
            }
    }
}