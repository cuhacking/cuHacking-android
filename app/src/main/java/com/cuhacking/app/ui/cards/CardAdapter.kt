package com.cuhacking.app.ui.cards

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cuhacking.app.info.ui.*

class CardAdapter(private val viewModel: InfoViewModel? = null) :
    ListAdapter<Card, CardViewHolder<*>>(Card.DiffCalback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder<*> {
        return when (viewType) {
            Header.viewType -> HeaderHolder(parent)
            UpdateCard.viewType -> UpdateCardHolder(parent)
            WiFiCard.viewType -> WiFiCardHolder(parent, viewModel!!)
            Title.viewType -> TitleViewHolder(parent)
            CountdownCard.viewType -> CountdownViewHolder(parent)
            EmergencyContactCard.viewType -> EmergencyContactHolder(parent)
            HelpCard.viewType -> HelpHolder(parent)
            SocialCard.viewType -> SocialHolder(parent)
            else -> throw IllegalArgumentException("Unknown card type")
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Header -> Header.viewType
        is UpdateCard -> UpdateCard.viewType
        is WiFiCard -> WiFiCard.viewType
        is Title -> Title.viewType
        is CountdownCard -> CountdownCard.viewType
        is EmergencyContactCard -> EmergencyContactCard.viewType
        is HelpCard -> HelpCard.viewType
        is SocialCard -> SocialCard.viewType
        else -> 0
    }

    override fun onBindViewHolder(holder: CardViewHolder<*>, position: Int) {
        when (holder) {
            is HeaderHolder -> holder.bind(getItem(position) as Header)
            is UpdateCardHolder -> holder.bind(getItem(position) as UpdateCard)
            is WiFiCardHolder -> holder.bind(getItem(position) as WiFiCard)
            is TitleViewHolder -> holder.bind(getItem(position) as Title)
            is CountdownViewHolder -> holder.bind(getItem(position) as CountdownCard)
            is EmergencyContactHolder -> holder.bind(getItem(position) as EmergencyContactCard)
            is HelpHolder -> holder.bind(getItem(position) as HelpCard)
            is SocialHolder -> holder.bind(getItem(position) as SocialCard)
        }
    }

    override fun onViewRecycled(holder: CardViewHolder<*>) {
        super.onViewRecycled(holder)
        if (holder is CountdownViewHolder) {
            holder.recycle()
        }
    }
}
