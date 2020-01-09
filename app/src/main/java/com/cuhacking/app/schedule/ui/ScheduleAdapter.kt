package com.cuhacking.app.schedule.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import com.cuhacking.app.data.api.models.EventType
import com.cuhacking.app.schedule.data.models.EventUiModel
import com.cuhacking.app.util.viewContext
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.card_event.view.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class ScheduleAdapter(private val nav: NavController) :
    ListAdapter<EventUiModel, ScheduleAdapter.ViewHolder>(EventUiModel.DIFF_CALLBACK) {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val directions = ScheduleFragmentDirections.actionScheduleToEventDetailActivity(
                    getItem(adapterPosition).id
                )
                nav.navigate(directions)
            }
        }

        fun bind(model: EventUiModel) {
            itemView.findViewById<TextView>(R.id.title).text = model.title
            itemView.findViewById<TextView>(R.id.location).text = model.locationName

            if (model.startTime != model.endTime) {
                itemView.findViewById<TextView>(R.id.time).text = viewContext.getString(
                    R.string.time_duration, model.startTime.format(
                        DateTimeFormatter.ofPattern("hh:mm a")
                    ), model.endTime.format(
                        DateTimeFormatter.ofPattern("hh:mm a")
                    )
                )
            } else {
                itemView.findViewById<TextView>(R.id.time).text = model.startTime.format(
                    DateTimeFormatter.ofPattern("hh:mm a")
                )
            }

            val colorId = when (model.type.toLowerCase(Locale.getDefault())) {
                EventType.WORKSHOP.typeString -> R.color.eventGreen
                EventType.KEY_EVENT.typeString -> R.color.eventPurple
                EventType.SPONSOR_EVENT.typeString -> R.color.eventYellow
                EventType.SOCIAL_ACTIVITY.typeString -> R.color.eventBlue
                EventType.FOOD.typeString -> R.color.eventRed
                EventType.VOLUNTEER.typeString -> R.color.eventOrange
                else -> R.color.eventPurple
            }

            val eventColor = ContextCompat.getColor(
                itemView.context,
                colorId
            )

            itemView.category_icon.setColorFilter(eventColor)
            (itemView as MaterialCardView).apply {
                setStrokeColor(ColorStateList.valueOf(eventColor))
                setRippleColorResource(colorId)
            }

            itemView.event_type_name.text = model.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_event, parent, false))

    override fun onBindViewHolder(holder: ScheduleAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}