package com.cuhacking.app.schedule.ui

import android.os.Bundle
import android.view.Menu

import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.cuhacking.app.R
import com.cuhacking.app.di.injector
import com.cuhacking.app.schedule.data.models.EventDetailUiModel
import com.cuhacking.app.util.formatTimeDuration
import kotlinx.android.synthetic.main.activity_schedule_detail.*

class EventDetailActivity : AppCompatActivity(R.layout.activity_schedule_detail) {
    private val viewModel: EventDetailViewModel by viewModels { injector.eventDetailViewModelFactory() }
    private val args by navArgs<EventDetailActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.init(args.eventId)
        viewModel.details.observe(this, Observer {
            when (it) {
                null -> {
                    Toast.makeText(
                        this,
                        getString(R.string.error_event_details),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                }
                else -> updateUi(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun updateUi(model: EventDetailUiModel) {
        event_title.text = model.title
        event_time.text = formatTimeDuration(model.start, model.end)
        event_location_name.text = model.locationName
        event_type_name.text = model.category
        event_summary.text = model.description

        category_icon.setColorFilter(ContextCompat.getColor(this, model.typeColor))
    }
}