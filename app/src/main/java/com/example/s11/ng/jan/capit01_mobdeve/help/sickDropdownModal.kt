package com.example.s11.ng.jan.capit01_mobdeve.help

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class sickDropdownModal : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var selectionListener: SickSelectionListener? = null

    interface SickSelectionListener {
        fun onSickSelection(selectedItems: List<String>)
    }

    fun setSelectionListener(listener: SickSelectionListener) {
        selectionListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireActivity(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sickmodaldrop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences =
            requireActivity().getSharedPreferences("SickModalPrefs", Context.MODE_PRIVATE)

        val closeButton = view.findViewById<Button>(R.id.close_button)
        val flu = view.findViewById<CheckBox>(R.id.flu_checkbox)
        val diabetes = view.findViewById<CheckBox>(R.id.diabetes_checkbox)
        val heartProblems = view.findViewById<CheckBox>(R.id.heart_problems_checkbox)
        val stroke = view.findViewById<CheckBox>(R.id.stroke_checkbox)
        val otherSickness = view.findViewById<EditText>(R.id.other_sick_edittext)

        // Restore checkbox states
        flu.isChecked = sharedPreferences.getBoolean("flu", false)
        diabetes.isChecked = sharedPreferences.getBoolean("diabetes", false)
        heartProblems.isChecked = sharedPreferences.getBoolean("heart_problems", false)
        stroke.isChecked = sharedPreferences.getBoolean("stroke", false)

        // Restore text input
        otherSickness.setText(sharedPreferences.getString("other_sickness", ""))

        closeButton.setOnClickListener {
            val selectedItems = mutableListOf<String>()
            if (flu.isChecked) selectedItems.add("Flu")
            if (diabetes.isChecked) selectedItems.add("Diabetes")
            if (heartProblems.isChecked) selectedItems.add("Heart Problems")
            if (stroke.isChecked) selectedItems.add("Stroke")

            val otherText = otherSickness.text.toString().trim()
            if (otherText.isNotEmpty()) {
                selectedItems.add(otherText)
            }
            sharedPreferences.edit().apply {
                putBoolean("Flu", flu.isChecked)
                putBoolean("Diabetes", diabetes.isChecked)
                putBoolean("Heart Problems", heartProblems.isChecked)
                putBoolean("Stroke", stroke.isChecked)
                putString("other_sickness", otherText)
                apply()
            }
            selectionListener?.onSickSelection(selectedItems)
            dismiss()
        }
    }
}
