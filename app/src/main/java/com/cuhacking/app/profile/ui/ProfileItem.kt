package com.cuhacking.app.profile.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil

sealed class ProfileItem {

    abstract infix fun sameAs(other: ProfileItem): Boolean
    abstract val type: Int

    data class Header(val qrCode: Bitmap, val name: String) : ProfileItem() {
        override fun sameAs(other: ProfileItem): Boolean = other is Header && other.name == name
        override val type: Int = 0
    }

    data class FoodGroup(@StringRes val name: Int, @ColorRes val color: Int) : ProfileItem() {
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

    data class FoodRestrictions(
        val lactoseFree: Boolean,
        val nutFree: Boolean,
        val vegetarian: Boolean,
        val halal: Boolean,
        val glutenFree: Boolean,
        val other: String?
    ) : ProfileItem() {

        override fun sameAs(other: ProfileItem) = other is FoodRestrictions

        override val type: Int
            get() = 8
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProfileItem>() {
            override fun areItemsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean =
                oldItem sameAs newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ProfileItem,
                newItem: ProfileItem
            ): Boolean =
                oldItem == newItem
        }
    }
}