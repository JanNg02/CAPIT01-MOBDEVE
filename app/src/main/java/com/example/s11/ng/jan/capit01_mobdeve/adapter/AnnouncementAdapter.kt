package com.example.s11.ng.jan.capit01_mobdeve.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt

class AnnouncementAdapter(private var data: MutableList<modelPost>, homeactivityRt: homeActivity_rt) : RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementAdapter.AnnouncementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.posthome_rt, parent, false)

        return AnnouncementAdapter.AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {

        val modelPost = data.getOrNull(position)
        modelPost?.let { holder.bindData(it) }
    }

    class AnnouncementViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val announcementAuthor: TextView = itemView.findViewById(R.id.username_RT)
        val announcementContent: TextView = itemView.findViewById(R.id.caption_post)
        val announcementTitle: TextView = itemView.findViewById(R.id.title_RT)
        val announcementUploadDate: TextView = itemView.findViewById(R.id.date_RT)

        fun bindData(data: modelPost) {
            announcementAuthor.text = data.announcementAuthor
            announcementContent.text = data.announcementContent
            announcementTitle.text = data.announcementTitle
            announcementUploadDate.text = data.announcementUploadDate
        }

    }

    fun updateData(newData: List<modelPost>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
