package com.cuhacking.app.info.ui

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R

sealed class CardViewHolder<T : InfoCard>(item: View) : RecyclerView.ViewHolder(item) {
    abstract fun bind(value: T)
}

class HeaderHolder(parent: ViewGroup) :
    CardViewHolder<Header>(LayoutInflater.from(parent.context).inflate(R.layout.header_info, parent, false)) {
    override fun bind(value: Header) {
        itemView.findViewById<TextView>(R.id.title).setText(value.title)
    }
}

class UpdateCardHolder(parent: ViewGroup) :
    CardViewHolder<UpdateCard>(LayoutInflater.from(parent.context).inflate(R.layout.card_update, parent, false)) {
    override fun bind(value: UpdateCard) {
        itemView.findViewById<TextView>(R.id.time).text = DateUtils.getRelativeTimeSpanString(value.publishTime)
        itemView.findViewById<TextView>(R.id.title).text = value.title
        itemView.findViewById<TextView>(R.id.description).text = value.description
    }
}