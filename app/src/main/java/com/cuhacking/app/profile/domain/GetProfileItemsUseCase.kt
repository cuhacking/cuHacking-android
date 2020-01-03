package com.cuhacking.app.profile.domain

import com.cuhacking.app.R
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.db.User
import com.cuhacking.app.profile.data.ProfileRepository
import com.cuhacking.app.profile.ui.ProfileItem
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class GetProfileItemsUseCase @Inject constructor(
    private val repository: ProfileRepository,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(userId: String): Flow<List<ProfileItem>> =
        withContext(dispatchers.io) {
            return@withContext repository.getUser(userId)
                .map { user ->
                    listOfNotNull(
                        ProfileItem.Header(repository.getUserQRCode(userId), user.name),
                        when (user.color.toLowerCase(Locale.getDefault())) {
                            "green" -> ProfileItem.FoodGroup(
                                R.string.color_green,
                                R.color.foodGreen
                            )
                            "blue" -> ProfileItem.FoodGroup(R.string.color_blue, R.color.foodBlue)
                            "yellow" -> ProfileItem.FoodGroup(
                                R.string.color_yellow,
                                R.color.foodYellow
                            )
                            else -> ProfileItem.FoodGroup(R.string.color_red, R.color.foodRed)
                        },
                        ProfileItem.FoodRestrictions(
                            user.lactoseFree,
                            user.nutFree,
                            user.vegetarian,
                            user.halal,
                            user.glutenFree,
                            user.otherRestrictions
                        ),
                        ProfileItem.School(user.school),
                        ProfileItem.Email(user.email)
                    )
                }
        }
}