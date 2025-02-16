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
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class pregnantDropdownModal : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var selectionListener: PregnantSelectionListener? = null

    interface PregnantSelectionListener {
        fun onPregnantSelection(selectedItem: String)
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
        val radioGroup = view.findViewById<RadioGroup>(R.id.pregnant_radiogroup)
        val firstTrimester = view.findViewById<RadioButton>(R.id.first_trimester_radiobutton)
        val secondTrimester = view.findViewById<RadioButton>(R.id.second_trimester_radiobutton)
        val thirdTrimester = view.findViewById<RadioButton>(R.id.third_trimester_radiobutton)

        // Restore the selected radio button state
        val savedSelection = sharedPreferences.getString("selected_trimester", "")
        when (savedSelection) {
            "First Trimester" -> firstTrimester.isChecked = true
            "Second Trimester" -> secondTrimester.isChecked = true
            "Third Trimester" -> thirdTrimester.isChecked = true
        }

        closeButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val selectedTrimester = when (selectedId) {
                R.id.first_trimester_radiobutton -> "First Trimester"
                R.id.second_trimester_radiobutton -> "Second Trimester"
                R.id.third_trimester_radiobutton -> "Third Trimester"
                else -> ""
            }

            // Save selected trimester in SharedPreferences
            sharedPreferences.edit().apply {
                putString("selected_trimester", selectedTrimester)
                apply()
            }

            // Pass selected trimester to listener
            selectionListener?.onPregnantSelection(selectedTrimester)

            dismiss()
        }
    }
}
