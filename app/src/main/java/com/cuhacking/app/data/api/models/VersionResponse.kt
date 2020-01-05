package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json
import org.threeten.bp.OffsetDateTime

data class VersionResponse(@field:Json(name = "version") val version: OffsetDateTime)