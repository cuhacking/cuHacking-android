package com.cuhacking.app.profile.ui

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R

class ProfileAdapter :
    ListAdapter<ProfileItem, RecyclerView.ViewHolder>(ProfileItem.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> HeaderHolder(View.inflate(parent.context, R.layout.profile_header, parent))
            1 -> FoodGroupHolder(View.inflate(parent.context, R.layout.profile_food_group, parent))
            else -> TODO()
        }

    override fun getItemViewType(position: Int): Int = getItem(position).type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ProfileItem.Header -> (holder as HeaderHolder).bind(item)
            is ProfileItem.FoodGroup -> (holder as FoodGroupHolder).bind(item)
            is ProfileItem.School -> TODO()
            is ProfileItem.Email -> TODO()
        }
    }
}

class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(header: ProfileItem.Header) {
        itemView.findViewById<TextView>(R.id.name).text = header.name
        itemView.findViewById<ImageView>(R.id.imageView).setImageBitmap(header.qrCode)
    }
}

class FoodGroupHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(foodGroup: ProfileItem.FoodGroup) {
        itemView.findViewById<ImageView>(R.id.group_indicator).setColorFilter(Color.CYAN)
        itemView.findViewById<TextView>(R.id.group_name).text = "Cyan Group"
    }
}