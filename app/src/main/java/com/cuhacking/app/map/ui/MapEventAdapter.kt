package com.cuhacking.app.map.ui

import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import com.cuhacking.app.data.api.models.EventType
import com.cuhacking.app.schedule.data.models.EventUiModel
import com.cuhacking.app.util.formatTimeDuration
import com.cuhacking.app.util.viewContext
import kotlinx.android.synthetic.main.event_layout.view.*
import kotlinx.android.synthetic.main.event_layout.view.category_icon
import kotlinx.android.synthetic.main.event_layout.view.event_type_name
import java.util.*

class MapEventAdapter(private val fragment: MapFragment) :
    ListAdapter<EventUiModel, MapEventAdapter.ViewHolder>(EventUiModel.DIFF_CALLBACK) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                fragment.findNavController().navigate(MapFragmentDirections.actionMapToEventDetailActivity(getItem(adapterPosition).id))
            }
        }

        fun bind(event: EventUiModel) {
            itemView.title.text = event.title
            itemView.time.text = viewContext.formatTimeDuration(event.startTime, event.endTime)
            itemView.event_type_name.text = event.type

            val colorId = when (event.type.toLowerCase(Locale.getDefault())) {
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.event_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}