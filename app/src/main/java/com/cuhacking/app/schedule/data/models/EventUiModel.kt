package com.cuhacking.app.schedule.data.models

import androidx.recyclerview.widget.DiffUtil
import org.threeten.bp.OffsetDateTime

data class EventUiModel(
    val title: String,
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime,
    val locationName: String,
    val id: String
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventUiModel>() {
            override fun areItemsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean =
                oldItem == newItem
        }
    }
}