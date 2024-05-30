package com.example.s11.ng.jan.capit01_mobdeve.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.R

    class AnnouncementViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image_post: ImageView = itemView.findViewById(R.id.image_post)
        private val username_RT: TextView = itemView.findViewById(R.id.username_RT)
        private val caption_post: TextView = itemView.findViewById(R.id.caption_post)

        fun bindData(data: modelPost) {
            image_post.setImageResource(data.imageID)
            username_RT.text = data.username
            caption_post.text = data.captionID
        }
    }