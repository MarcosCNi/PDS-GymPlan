package com.example.gymplan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gymplan.R
import com.example.gymplan.databinding.HomeBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: HomeBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeBottomSheetBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.home_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMenu()
    }

    private fun showMenu() {
        binding.btnCreateWorkoutPlan.setOnClickListener {
        }
        binding.btnCreateExerciseList.setOnClickListener {
        }
        binding.btnRenameWorkoutPlan.setOnClickListener {
        }
        binding.btnShareWorkoutPlan.setOnClickListener {

        }
        binding.btnDeleteWorkoutPlan.setOnClickListener {

        }
    }
}