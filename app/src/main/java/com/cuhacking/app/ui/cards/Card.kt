package com.cuhacking.app.ui.cards

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil

sealed class Card {
    abstract infix fun sameAs(other: Card): Boolean

    companion object {
        val DiffCalback = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem sameAs newItem

            // All subclasses should in theory be data classes which will properly generate .equals()
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem == newItem
        }
    }
}

data class Header(@StringRes val title: Int) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? Header)?.title == title
    companion object {
        const val viewType = 1
    }
}

class Title : Card() {
    override fun sameAs(other: Card): Boolean = true
    companion object {
        const val viewType = 8
    }
}

data class CountdownCard(private val timeString: String) : Card() {
    override fun sameAs(other: Card): Boolean = false
}

data class UpdateCard(
    val id: String,
    val title: String,
    val description: String,
    val publishTime: Long
) : Card() {
    override fun sameAs(other: Card): Boolean = (other as? UpdateCard)?.id == id
    companion object {
        const val viewType = 2
    }
}

data class WiFiCard(val ssid: String, val password: String): Card() {
    override fun sameAs(other: Card): Boolean = (other as? WiFiCard)?.ssid == ssid

    companion object {
        const val viewType = 3
    }
}

