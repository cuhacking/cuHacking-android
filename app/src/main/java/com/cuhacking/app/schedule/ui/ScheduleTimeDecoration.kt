package com.cuhacking.app.schedule.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Typeface
import android.text.Layout.Alignment.ALIGN_CENTER
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.graphics.withTranslation
import androidx.core.text.inSpans
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import com.cuhacking.app.schedule.data.models.EventUiModel
import com.cuhacking.app.util.newStaticLayout
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class ScheduleTimeDecoration(
    context: Context,
    schedule: List<EventUiModel>
) : RecyclerView.ItemDecoration() {
    private val paint: TextPaint
    private val width: Int
    private val padding: Int
    private val timeTextSize: Int
    private val meridiemTextSize: Int
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm")
    private val meridiemFormatter = DateTimeFormatter.ofPattern("a")

    private val timeTextSizeSpan: AbsoluteSizeSpan
    private val meridiemTextSizeSpan: AbsoluteSizeSpan
    private val boldSpan = StyleSpan(Typeface.BOLD)

    init {
        val attrs = context.obtainStyledAttributes(
            R.style.Widget_CuHacking_TimeHeaders,
            R.styleable.TimeHeader
        )

        paint = TextPaint(ANTI_ALIAS_FLAG).apply {
            color = attrs.getColorOrThrow(R.styleable.TimeHeader_android_textColor)
        }
        width = attrs.getDimensionPixelSizeOrThrow(R.styleable.TimeHeader_android_width)
        padding = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_padding, 0)
        timeTextSize = attrs.getDimensionPixelSizeOrThrow(R.styleable.TimeHeader_timeTextSize)
        meridiemTextSize =
            attrs.getDimensionPixelSizeOrThrow(R.styleable.TimeHeader_meridiemTextSize)
        attrs.recycle()

        timeTextSizeSpan = AbsoluteSizeSpan(timeTextSize)
        meridiemTextSizeSpan = AbsoluteSizeSpan(meridiemTextSize)
    }

    private val timeSlots: Map<Int, StaticLayout> = schedule
        .mapIndexed { index, event ->
            index to event.startTime
        }
        .distinctBy { (_, time) -> time.truncatedTo(ChronoUnit.MINUTES) }
        .map { (index, time) ->
            index to createHeader(time)
        }.toMap()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (timeSlots.isEmpty() || parent.isEmpty()) return

        val parentPadding = parent.paddingTop

        var earliestPosition = Int.MAX_VALUE
        var previousHeaderPosition = -1
        var previousHasHeader = false
        var earliestChild: View? = null
        for (i in parent.childCount - 1 downTo 0) {
            val child = parent.getChildAt(i) ?: continue

            if (child.y > parent.height || (child.y + child.height) < 0) {
                // Can't see this child
                continue
            }

            val position = parent.getChildAdapterPosition(child)
            if (position < 0) {
                continue
            }
            if (position < earliestPosition) {
                earliestPosition = position
                earliestChild = child
            }

            val header = timeSlots[position]
            if (header != null) {
                drawHeader(c, child, parentPadding, header, child.alpha, previousHasHeader)
                previousHeaderPosition = position
                previousHasHeader = true
            } else {
                previousHasHeader = false
            }
        }

        if (earliestChild != null && earliestPosition != previousHeaderPosition) {
            // This child needs a sicky header
            findHeaderBeforePosition(earliestPosition)?.let { stickyHeader ->
                previousHasHeader = previousHeaderPosition - earliestPosition == 1
                drawHeader(c, earliestChild, parentPadding, stickyHeader, 0f, previousHasHeader)
            }
        }
    }

    private fun findHeaderBeforePosition(position: Int): StaticLayout? {
        for (headerPos in timeSlots.keys.reversed()) {
            if (headerPos < position) {
                return timeSlots[headerPos]
            }
        }
        return null
    }

    private fun drawHeader(
        canvas: Canvas,
        child: View,
        parentPadding: Int,
        header: StaticLayout,
        headerAlpha: Float,
        previousHasHeader: Boolean
    ) {
        val childTop = child.y.toInt()
        val childBottom = childTop + child.height
        var top = (childTop + padding).coerceAtLeast(parentPadding)
        if (previousHasHeader) {
            top = top.coerceAtMost(childBottom - header.height - padding)
        }
        paint.alpha = (headerAlpha * 255).toInt()
        canvas.withTranslation(y = top.toFloat()) {
            header.draw(canvas)
        }
    }

    /**
     * Create a header layout for the given [startTime].
     */
    private fun createHeader(startTime: ZonedDateTime): StaticLayout {
        val text = SpannableStringBuilder().apply {
            inSpans(timeTextSizeSpan) {
                append(timeFormatter.format(startTime))
            }
            append(System.lineSeparator())
            inSpans(meridiemTextSizeSpan, boldSpan) {
                append(meridiemFormatter.format(startTime).toUpperCase())
            }
        }
        return newStaticLayout(text, paint, width, ALIGN_CENTER, 1f, 0f, false)
    }
}