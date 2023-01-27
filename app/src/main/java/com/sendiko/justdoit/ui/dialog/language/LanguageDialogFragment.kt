package com.sendiko.justdoit.ui.dialog.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentLanguageDialogBinding

class LanguageDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentLanguageDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.languagesCheckbox.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.language_english -> Toast.makeText(context, "English", Toast.LENGTH_SHORT)
                    .show()
                R.id.language_indonesian -> Toast.makeText(
                    context,
                    "Indonesian",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}