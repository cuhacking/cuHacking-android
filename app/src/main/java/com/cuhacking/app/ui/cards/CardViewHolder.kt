package com.cuhacking.app.ui.cards

import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.provider.Settings
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import com.cuhacking.app.info.data.WifiInfo
import com.cuhacking.app.info.ui.InfoViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.card_emergency.view.*
import kotlinx.android.synthetic.main.card_help.view.*
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalUnit
import kotlin.math.floor

sealed class CardViewHolder<T : Card>(item: View) : RecyclerView.ViewHolder(item) {
    abstract fun bind(value: T)
}

class EmergencyContactHolder(parent: ViewGroup) : CardViewHolder<EmergencyContactCard>(
    LayoutInflater.from(parent.context).inflate(
        R.layout.card_emergency,
        parent,
        false
    )
) {
    override fun bind(value: EmergencyContactCard) {
        itemView.phone_number.text = value.number
    }
}

class HelpHolder(parent: ViewGroup) : CardViewHolder<HelpCard>(
    LayoutInflater.from(parent.context).inflate(
        R.layout.card_help,
        parent,
        false
    )
) {
    override fun bind(value: HelpCard) {
        itemView.message.text = value.message
    }
}

class HeaderHolder(parent: ViewGroup) :
    CardViewHolder<Header>(
        LayoutInflater.from(parent.context).inflate(
            R.layout.header_info,
            parent,
            false
        )
    ) {
    override fun bind(value: Header) {
        itemView.findViewById<TextView>(R.id.title).setText(value.title)
    }
}

class UpdateCardHolder(parent: ViewGroup) :
    CardViewHolder<UpdateCard>(
        LayoutInflater.from(parent.context).inflate(
            R.layout.card_update,
            parent,
            false
        )
    ) {
    override fun bind(value: UpdateCard) {
        itemView.findViewById<TextView>(R.id.time).text =
            DateUtils.getRelativeTimeSpanString(value.publishTime.toEpochSecond() * 1000)
        itemView.findViewById<TextView>(R.id.title).text = value.title
        itemView.findViewById<TextView>(R.id.description).text = value.description
    }
}

class WiFiCardHolder(parent: ViewGroup, private val viewModel: InfoViewModel) :
    CardViewHolder<WiFiCard>(
        LayoutInflater.from(parent.context).inflate(
            R.layout.card_wifi,
            parent,
            false
        )
    ) {
    private var wifiInfo: WifiInfo? = null

    init {
        itemView.findViewById<MaterialButton>(R.id.connect_button).setOnClickListener {
            wifiInfo?.let { (ssid, password) ->
                viewModel.setupWifi(WifiInfo(ssid, password))
                if (Build.VERSION.SDK_INT >= 29) {
                    itemView.context.startActivity(Intent(Settings.Panel.ACTION_WIFI))
                }
            }
        }
    }

    override fun bind(value: WiFiCard) {
        wifiInfo = WifiInfo(value.ssid, value.password)
        val context = itemView.context
        itemView.findViewById<TextView>(R.id.network).text =
            context.getString(R.string.wifi_network, value.ssid)
        itemView.findViewById<TextView>(R.id.password).text =
            context.getString(R.string.wifi_password, value.password)
    }
}

class TitleViewHolder(parent: ViewGroup) : CardViewHolder<Title>(
    LayoutInflater.from(parent.context).inflate(
        R.layout.header_announcement,
        parent,
        false
    )
) {
    override fun bind(value: Title) {
    }
}

class CountdownViewHolder(parent: ViewGroup) : CardViewHolder<CountdownCard>(
    LayoutInflater.from(parent.context).inflate(
        R.layout.header_announcement,
        parent,
        false
    )
) {

    private lateinit var timer: CountDownTimer

    override fun bind(value: CountdownCard) {
        timer = object :
            CountDownTimer(Duration.between(LocalDateTime.now(), value.time).toMillis(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = floor(millisUntilFinished / 60 / 60 / 1000f).toInt()
                val minutes =
                    floor((millisUntilFinished - (hours * 60 * 60 * 1000L)) / 60 / 1000f).toInt()
                val seconds = (floor(millisUntilFinished / 1000f)).toInt() % 60

                val hourString = "$hours".padStart(2, '0')
                val minuteString = "$minutes".padStart(2, '0')
                val secondsString = "$seconds".padStart(2, '0')

                itemView.findViewById<TextView>(R.id.title_text).text =
                    itemView.context.getString(
                        value.message,
                        "${hourString}:${minuteString}:${secondsString}"
                    )
            }

            override fun onFinish() {
                itemView.findViewById<TextView>(R.id.title_text).text =
                    itemView.context.getString(
                        value.message,
                        "00:00:00"
                    )
            }
        }
        timer.start()

    }

    fun recycle() {
        timer.cancel()
    }
}