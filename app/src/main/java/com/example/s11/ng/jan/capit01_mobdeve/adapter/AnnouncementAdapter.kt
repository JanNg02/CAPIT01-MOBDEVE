package com.example.s11.ng.jan.capit01_mobdeve.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.R

class AnnouncementAdapter(private val data: ArrayList<modelPost>?, private val viewPostLauncher: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<AnnouncementViewHolder>() {

    companion object {
        const val imageKey: String = "imageKey"
        const val usernameKey: String = "nameKey"
        const val captionKey: String = "captionKey"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.posthome_rt, parent, false)

        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bindData(data!!.get(position))

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.getContext(), postFull::class.java)
            intent.putExtra(imageKey, data[position].imageID)
            intent.putExtra(usernameKey, data[position].username)
            intent.putExtra(captionKey, data[position].captionID)

            viewPostLauncher.launch(intent)
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }
}