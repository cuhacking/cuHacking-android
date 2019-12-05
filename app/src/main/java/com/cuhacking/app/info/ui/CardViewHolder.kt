package com.cuhacking.app.info.ui

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import com.cuhacking.app.info.data.WifiInfo
import com.google.android.material.button.MaterialButton

sealed class CardViewHolder<T : InfoCard>(item: View) : RecyclerView.ViewHolder(item) {
    abstract fun bind(value: T)
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
            DateUtils.getRelativeTimeSpanString(value.publishTime)
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
