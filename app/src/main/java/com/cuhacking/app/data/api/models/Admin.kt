package com.cuhacking.app.data.api.models

import com.squareup.moshi.Json

data class ScanRequest(@field:Json(name = "uid") val userId: String,
                       @field:Json(name = "eventId") val eventId: String)