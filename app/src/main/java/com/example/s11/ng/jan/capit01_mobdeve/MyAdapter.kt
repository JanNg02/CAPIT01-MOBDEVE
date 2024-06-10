package com.example.s11.ng.jan.capit01_mobdeve

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.bson.Document

class MyAdapter(private val data: List<Document>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document = data[position]
        holder.newsTitle.text = document.getString("announcementTitle")
        holder.newsContent.text = document.getString("announcementContent")
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle_TV)
        val newsContent: TextView = itemView.findViewById(R.id.newsContent_TV)
    }
}