package com.example.gymplan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gymplan.R
import com.example.gymplan.data.model.ExerciseModel
import com.example.gymplan.databinding.FragmentHomeBinding
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.show
import com.example.gymplan.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUser()
        collectObserver()
        setupOptionBtn()
    }

    private fun collectObserver() = with(binding) {
        viewModel.workoutPlanList.observe(viewLifecycleOwner){
            val workoutPlanNameList: ArrayList<String> = arrayListOf()
            for (item in it){
                workoutPlanNameList.add(item.workoutPlan.name)
            }
            val equipmentAdapter = ArrayAdapter(requireContext(), R.layout.menu_filter_item, workoutPlanNameList)
            homeSelectDropdownText.setAdapter(equipmentAdapter)
        }
    }

    private fun setupOptionBtn() = with(binding) {
        optionBtn.setOnClickListener {
            showMenu(it, R.menu.popup_home_page_menu)
//            showBottomSheet()
        }

    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.option_1 -> {
                    Toast.makeText(context, R.string.create_workout_plan, Toast.LENGTH_SHORT).show()
                    viewModel.createWorkoutPlan("Test")
                    true
                }
                R.id.option_2 -> {
                    Toast.makeText(context, R.string.create_exercise_list, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.option_3 -> {
                    Toast.makeText(context, R.string.rename_workout, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.option_4 -> {
                    Toast.makeText(context, R.string.share_workout, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.option_5 -> {
                    Toast.makeText(context, R.string.delete_workout, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
    }

//    private fun showBottomSheet() = with(binding) {
//        val dialog = BottomSheetDialog(requireContext())
//        dialog.setContentView(R.layout.workout_bottom_sheet)
//        dialog.show()
//    }

    private fun checkUser(){
        viewModel.user.observe(viewLifecycleOwner){ user ->
            val navController = findNavController()
            val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomAppBar)
            if (user != null) {
                navBar?.show()
            } else {
                navBar?.gone()
                navController.navigate(R.id.signInFragment2)
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
}