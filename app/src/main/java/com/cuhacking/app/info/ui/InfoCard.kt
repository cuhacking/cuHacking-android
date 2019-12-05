package com.cuhacking.app.info.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

sealed class InfoCard {
    abstract infix fun sameAs(other: InfoCard): Boolean

    companion object {
        val DiffCalback = object : DiffUtil.ItemCallback<InfoCard>() {
            override fun areItemsTheSame(oldItem: InfoCard, newItem: InfoCard): Boolean =
                oldItem sameAs newItem

            // All subclasses should in theory be data classes which will properly generate .equals()
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: InfoCard, newItem: InfoCard): Boolean =
                oldItem == newItem
        }
    }
}

data class Header(@StringRes val title: Int) : InfoCard() {
    override fun sameAs(other: InfoCard): Boolean = (other as? Header)?.title == title
    companion object {
        const val viewType = 1
    }
}

data class CountdownCard(private val timeString: String) : InfoCard() {
    override fun sameAs(other: InfoCard): Boolean = false
}

data class UpdateCard(
    val id: String,
    val title: String,
    val description: String,
    val publishTime: Long
) : InfoCard() {
    override fun sameAs(other: InfoCard): Boolean = (other as? UpdateCard)?.id == id
    companion object {
        const val viewType = 2
    }
}