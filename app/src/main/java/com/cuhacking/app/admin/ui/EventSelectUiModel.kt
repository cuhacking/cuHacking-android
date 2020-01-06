package com.cuhacking.app.admin.ui

import org.threeten.bp.OffsetDateTime

data class EventSelectUiModel(val id: String,
                              val title: String,
                              val timeSpan: String,
                              val startTime: OffsetDateTime,
                              val endTime: OffsetDateTime)