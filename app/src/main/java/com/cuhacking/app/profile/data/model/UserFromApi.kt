package com.cuhacking.app.profile.data.model

import com.squareup.moshi.Json

data class UserResponse(
    @field:Json(name = "data") val data: UserFromApi,
    @field:Json(name = "operation") val operation: String,
    @field:Json(name = "status") val status: String
)

data class UserFromApi(
    @field:Json(name = "uid") val id: String,
    @field:Json(name = "email") val email: String,
    @field:Json(name = "color") val color: String?,
    @field:Json(name = "role") val role: String,
    @field:Json(name = "application") val applicationInfo: ApplicationInfo
)

data class ApplicationInfo(
    @field:Json(name = "basicInfo") val basicInfo: UserBasicInfo,
    @field:Json(name = "personalInfo") val personalInfo: UserPersonalInfo
)

data class UserBasicInfo(
    @field:Json(name = "lastName") val lastName: String,
    @field:Json(name = "firstName") val firstName: String
)

data class UserPersonalInfo(
    @field:Json(name = "school") val school: String,
    @field:Json(name = "dietaryRestrictions") val dietaryRestrictions: DietaryRestrictions
)

data class DietaryRestrictions(
    @field:Json(name = "lactoseFree") val lactoseFree: Boolean,
    @field:Json(name = "nutFree") val nutFree: Boolean,
    @field:Json(name = "vegetarian") val vegetarian: Boolean,
    @field:Json(name = "halal") val halal: Boolean,
    @field:Json(name = "glutenFree") val glutenFree: Boolean,
    @field:Json(name = "other") val other: String?
)