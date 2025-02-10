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

class pregnantDropdownModal : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var selectionListener: PregnantSelectionListener? = null

    interface PregnantSelectionListener {
        fun onPregnantSelection(selectedItems: List<String>)
    }

    fun setSelectionListener(listener: PregnantSelectionListener) {
        selectionListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireActivity(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pregnantmodaldrop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("PregnantModalPrefs", Context.MODE_PRIVATE)

        val closeButton = view.findViewById<Button>(R.id.close_button)
        val firstTrimester = view.findViewById<CheckBox>(R.id.first_trimester_checkbox)
        val secondTrimester = view.findViewById<CheckBox>(R.id.second_trimester_checkbox)
        val thirdTrimester = view.findViewById<CheckBox>(R.id.third_trimester_checkbox)

        // Restore checkbox states
        firstTrimester.isChecked = sharedPreferences.getBoolean("first_trimester", false)
        secondTrimester.isChecked = sharedPreferences.getBoolean("second_trimester", false)
        thirdTrimester.isChecked = sharedPreferences.getBoolean("third_trimester", false)

        val checkboxes = listOf(firstTrimester, secondTrimester, thirdTrimester)

        checkboxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkboxes.forEach { otherCheckbox ->
                        if (otherCheckbox != checkbox) {
                            otherCheckbox.isChecked = false // Uncheck other checkboxes
                        }
                    }
                }
            }
        }

        closeButton.setOnClickListener {
            val selectedItems = mutableListOf<String>()
            if (firstTrimester.isChecked) selectedItems.add("First Trimester")
            if (secondTrimester.isChecked) selectedItems.add("Second Trimester")
            if (thirdTrimester.isChecked) selectedItems.add("Third Trimester")

            sharedPreferences.edit().apply {
                putBoolean("First Trimester", firstTrimester.isChecked)
                putBoolean("Second Trimester", secondTrimester.isChecked)
                putBoolean("Third Trimester", thirdTrimester.isChecked)
                apply()
            }
            selectionListener?.onPregnantSelection(selectedItems)

            dismiss()
        }
    }
}
