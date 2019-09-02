package com.cuhacking.app.profile.ui

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import kotlinx.android.synthetic.main.profile_header.view.*

class ProfileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int = 4

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
}

class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(name: String, bitmap: Bitmap) {
        itemView.findViewById<TextView>(R.id.name).text = name
        itemView.findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
    }
}