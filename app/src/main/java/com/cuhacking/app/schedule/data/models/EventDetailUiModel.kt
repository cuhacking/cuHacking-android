package com.cuhacking.app.schedule.data.models

data class EventDetailUiModel(
    val id: String,
    val title: String,
    val timeDuration: String,
    val locationName: String,
    val category: String,
    val description: String
)