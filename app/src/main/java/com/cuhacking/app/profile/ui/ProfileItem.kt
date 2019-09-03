package com.cuhacking.app.profile.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.recyclerview.widget.DiffUtil

sealed class ProfileItem {

    abstract infix fun sameAs(other: ProfileItem): Boolean
    abstract val type: Int

    data class Header(val qrCode: Bitmap, val name: String) : ProfileItem() {
        override fun sameAs(other: ProfileItem): Boolean = other is Header
        override val type: Int = 0
    }

    data class FoodGroup(val name: String, val color: String) : ProfileItem() {
        override fun sameAs(other: ProfileItem): Boolean = other is FoodGroup
        override val type: Int = 1
    }

    data class School(val name: String) : ProfileItem() {
        override fun sameAs(other: ProfileItem): Boolean = other is School
        override val type: Int = 2
    }

    data class Email(val address: String) : ProfileItem() {
        override fun sameAs(other: ProfileItem): Boolean = other is Email
        override val type: Int = 3
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProfileItem>() {
            override fun areItemsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean =
                oldItem sameAs newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean =
                oldItem == newItem
        }
    }
}