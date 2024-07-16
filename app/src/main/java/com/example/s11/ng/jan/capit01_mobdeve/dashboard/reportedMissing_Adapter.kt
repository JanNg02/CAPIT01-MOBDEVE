package com.example.s11.ng.jan.capit01_mobdeve.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.missing.missing_descp

class MissingPersonReportedAdapter(private val missingPersonData: List<missingPersonDataReported>) :
    RecyclerView.Adapter<MissingPersonReportedAdapter.MissingPersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissingPersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.missingperson_layout_bo, parent, false)
        return MissingPersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: MissingPersonViewHolder, position: Int) {
        val missingPerson = missingPersonData[position]
        holder.bind(missingPerson, position)

        // Add an OnClickListener to each item
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, missingPersonReported_desc_rt::class.java)
            intent.putExtra("miaID", missingPerson.miaID)
            intent.putExtra("title", "Case#" + (position + 1) + ": "+missingPerson.missingFullName)
            intent.putExtra("missingFullName", missingPerson.missingFullName)
            intent.putExtra("age", missingPerson.age)
            intent.putExtra("gender", missingPerson.sex)
            intent.putExtra("areaLastSeen", missingPerson.areaLastSeen)
            intent.putExtra("description", missingPerson.description)
            intent.putExtra("filedBy", missingPerson.filedBy)
            intent.putExtra("dateSubmitted", missingPerson.dateSubmitted)
            intent.putExtra("contactNum", missingPerson.contactNum)
            intent.putExtra("teamID", missingPerson.teamID)
            intent.putExtra("isFound", missingPerson.isFound)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = missingPersonData.size

    inner class MissingPersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var titleTV: TextView = itemView.findViewById(R.id.title_TV)
        private var nameTV: TextView = itemView.findViewById(R.id.name_TV)
        private var descriptionTV: TextView = itemView.findViewById(R.id.description_TV)
        private var lastSeenTV: TextView = itemView.findViewById(R.id.lastSeen_TV)
        private var contactTV: TextView = itemView.findViewById(R.id.contact_TV)

        fun bind(missingPerson: missingPersonDataReported, position: Int) {
            var positionProper = position + 1;
            // Bind the data to the views
            titleTV.text = "Case#" + positionProper + ": " + missingPerson.missingFullName
            nameTV.text = "Name: "+missingPerson.missingFullName
            descriptionTV.text = "Description: "+missingPerson.description
            lastSeenTV.text ="Area Last Seen: " +missingPerson.areaLastSeen
            contactTV.text = "Family Contact Number: "+missingPerson.contactNum
        }
    }
}