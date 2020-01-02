package com.cuhacking.app.profile.data.model

import com.squareup.moshi.Json

data class UserResponse(
    @field:Json(name = "data") val data: UserFromApi,
    @field:Json(name = "operation") val operation: String,
    @field:Json(name = "status") val status: String
)

data class UserFromApi(
    @field:Json(name = "uid") val id: String,
    @field:Json(name = "username") val name: String,
    @field:Json(name = "email") val email: String,
    @field:Json(name = "color") val color: String,
    @field:Json(name = "school") val school: String,
    @field:Json(name = "role") val role: String
)