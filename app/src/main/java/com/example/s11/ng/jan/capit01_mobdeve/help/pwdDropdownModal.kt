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
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class pwdDropdownModal : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var selectionListener: PwdSelectionListener? = null

    interface PwdSelectionListener {
        fun onPwdSelection(selectedItems: List<String>)
    }

    fun setSelectionListener(listener: PwdSelectionListener) {
        selectionListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireActivity(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pwdmodaldrop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("PwdModalPrefs", Context.MODE_PRIVATE)

        val closeButton = view.findViewById<Button>(R.id.close_button)
        val hearingImpairment = view.findViewById<CheckBox>(R.id.hearing_impairment_checkbox)
        val visionImpairment = view.findViewById<CheckBox>(R.id.vision_impairment_checkbox)
        val mentalDisability = view.findViewById<CheckBox>(R.id.mental_disability_checkbox)
        val mobilityDisability = view.findViewById<CheckBox>(R.id.mobility_disability_checkbox)

        // Restore checkbox states
        hearingImpairment.isChecked = sharedPreferences.getBoolean("hearing_impairment", false)
        visionImpairment.isChecked = sharedPreferences.getBoolean("vision_impairment", false)
        mentalDisability.isChecked = sharedPreferences.getBoolean("mental_disability", false)
        mobilityDisability.isChecked = sharedPreferences.getBoolean("mobility_disability", false)

        closeButton.setOnClickListener {
            // Collect selected checkboxes
            val selectedItems = mutableListOf<String>()
            if (hearingImpairment.isChecked) selectedItems.add("Hearing Impairment")
            if (visionImpairment.isChecked) selectedItems.add("Vision Impairment")
            if (mentalDisability.isChecked) selectedItems.add("Mental Disability")
            if (mobilityDisability.isChecked) selectedItems.add("Mobility Disability")

            // Save states to SharedPreferences
            sharedPreferences.edit().apply {
                putBoolean("hearing_impairment", hearingImpairment.isChecked)
                putBoolean("vision_impairment", visionImpairment.isChecked)
                putBoolean("mental_disability", mentalDisability.isChecked)
                putBoolean("mobility_disability", mobilityDisability.isChecked)
                apply()
            }

            // Pass selected items to listener
            selectionListener?.onPwdSelection(selectedItems)

            dismiss()
        }
    }
}
