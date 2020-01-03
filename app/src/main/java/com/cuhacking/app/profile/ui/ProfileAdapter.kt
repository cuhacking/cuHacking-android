package com.cuhacking.app.profile.ui

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import com.cuhacking.app.util.viewContext

class ProfileAdapter :
    ListAdapter<ProfileItem, RecyclerView.ViewHolder>(ProfileItem.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> HeaderHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.profile_header,
                    parent,
                    false
                )
            )
            1 -> FoodGroupHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.profile_food_group,
                    parent,
                    false
                )
            )
            2 -> SchoolHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.profile_school,
                    parent,
                    false
                )
            )
            3 -> EmailHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.profile_email,
                    parent,
                    false
                )
            )
            8 -> FoodRestrictionsHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.profile_food_restrictions,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun getItemViewType(position: Int): Int = getItem(position).type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ProfileItem.Header -> (holder as HeaderHolder).bind(item)
            is ProfileItem.FoodGroup -> (holder as FoodGroupHolder).bind(item)
            is ProfileItem.School -> (holder as SchoolHolder).bind(item)
            is ProfileItem.Email -> (holder as EmailHolder).bind(item)
            is ProfileItem.FoodRestrictions -> (holder as FoodRestrictionsHolder).bind(item)
        }
    }
}

class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(header: ProfileItem.Header) {
        itemView.findViewById<TextView>(R.id.name).text = header.name
        val drawable = BitmapDrawable(itemView.context.resources, header.qrCode)
        drawable.paint.isFilterBitmap = false
        itemView.findViewById<ImageView>(R.id.imageView).setImageDrawable(drawable)
    }
}

class FoodGroupHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(foodGroup: ProfileItem.FoodGroup) {
        val color = ContextCompat.getColor(viewContext, foodGroup.color)
        itemView.findViewById<ImageView>(R.id.group_indicator).setColorFilter(color)
        itemView.findViewById<TextView>(R.id.group_name).text =
            viewContext.getString(R.string.color_group, viewContext.getString(foodGroup.name))
    }
}

class EmailHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(email: ProfileItem.Email) {
        itemView.findViewById<TextView>(R.id.email_address).text = email.address
    }
}

class SchoolHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(school: ProfileItem.School) {
        itemView.findViewById<TextView>(R.id.school_name).text = school.name
    }
}

class FoodRestrictionsHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(restrictions: ProfileItem.FoodRestrictions) {
        itemView.findViewById<TextView>(R.id.lactose_free).isVisible = restrictions.lactoseFree
        itemView.findViewById<TextView>(R.id.nut_free).isVisible = restrictions.nutFree
        itemView.findViewById<TextView>(R.id.vegetarian).isVisible = restrictions.vegetarian
        itemView.findViewById<TextView>(R.id.halal).isVisible = restrictions.halal
        itemView.findViewById<TextView>(R.id.gluten_free).isVisible = restrictions.glutenFree
        itemView.findViewById<TextView>(R.id.other_restrictions).apply {
            isVisible = restrictions.other != null
            if (restrictions.other != null) {
                text = context.getString(R.string.food_restrictions_other, restrictions.other)
            }
        }

        itemView.findViewById<TextView>(R.id.none).isVisible =
            !(restrictions.lactoseFree || restrictions.nutFree || restrictions.vegetarian || restrictions.halal || restrictions.glutenFree || restrictions.other != null)
    }

    private var TextView.isVisible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = if (value) View.VISIBLE else View.GONE
        }
}