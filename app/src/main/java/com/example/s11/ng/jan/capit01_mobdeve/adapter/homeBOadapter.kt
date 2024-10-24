package com.example.s11.ng.jan.capit01_mobdeve.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.s11.ng.jan.capit01_mobdeve.home.currentAssignment

class homeBOadapter(context: Context, private val items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        // You can customize the view here if needed
        return view
    }
}