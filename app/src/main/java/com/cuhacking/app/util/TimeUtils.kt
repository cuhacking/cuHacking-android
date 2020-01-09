package com.cuhacking.app.util

import android.content.Context
import com.cuhacking.app.R
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

fun Context.formatTimeDuration(start: OffsetDateTime, end: OffsetDateTime): String {
    return if (start == end) {
        start.format(
            DateTimeFormatter.ofPattern("hh:mm a")
        )
    } else {
        getString(
            R.string.time_duration, start.format(
                DateTimeFormatter.ofPattern("hh:mm a")
            ),
            end.format(
                DateTimeFormatter.ofPattern("hh:mm a")
            )
        )
    }
}