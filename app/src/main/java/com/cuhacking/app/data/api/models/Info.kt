package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json
import org.threeten.bp.OffsetDateTime

data class InfoResponse(
    @field:Json(name = "version") val version: OffsetDateTime,
    @field:Json(name = "info") val info: Info
)

data class Info(
    @field:Json(name = "wifi") val wifi: WifiData,
    @field:Json(name = "social") val social: SocialLinks,
    @field:Json(name = "emergency") val emergency: String,
    @field:Json(name = "help") val help: String
)

data class WifiData(
    @field:Json(name = "network") val network: String,
    @field:Json(name = "password") val password: String
)

data class SocialLinks(
    @field:Json(name = "twitter") val twitter: String,
    @field:Json(name = "facebook") val facebook: String,
    @field:Json(name = "instagram") val instagram: String,
    @field:Json(name = "slack") val slack: String,
    @field:Json(name = "github") val github: String?,
    @field:Json(name = "linkedin") val linkedin: String?
)