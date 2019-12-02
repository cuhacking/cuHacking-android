package com.cuhacking.app.info.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class InfoCardAdapter :
    ListAdapter<InfoCard, CardViewHolder<*>>(InfoCard.DiffCalback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder<*> {
        return when (viewType) {
            Header.viewType -> HeaderHolder(parent)
            UpdateCard.viewType -> UpdateCardHolder(parent)
            else -> throw IllegalArgumentException("Unknown card type")
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Header -> Header.viewType
        is UpdateCard -> UpdateCard.viewType
        else -> 0
    }

    override fun onBindViewHolder(holder: CardViewHolder<*>, position: Int) {
        when (holder) {
            is HeaderHolder -> holder.bind(getItem(position) as Header)
            is UpdateCardHolder -> holder.bind(getItem(position) as UpdateCard)
        }
    }
}
