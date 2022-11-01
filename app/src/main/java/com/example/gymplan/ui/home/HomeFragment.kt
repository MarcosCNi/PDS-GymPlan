package com.example.gymplan.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymplan.R
import com.example.gymplan.databinding.FragmentHomeBinding
import com.example.gymplan.ui.adapters.WorkoutListAdapter
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.show
import com.example.gymplan.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    private val workoutAdapter by lazy { WorkoutListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        clickAdapter()
        checkUser()
        collectObserver()
        setupOptionBtn()
    }

    private fun clickAdapter() {
        workoutAdapter.setOnclickListener { workoutModel ->
            Log.e("HomeFragment", workoutModel.name)
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvPlanList.apply {
            adapter = workoutAdapter
            layoutManager = LinearLayoutManager(context)
        }
        ItemTouchHelper(itemTouchHelperCallback()).attachToRecyclerView(rvPlanList)
    }

    private fun itemTouchHelperCallback(): ItemTouchHelper.Callback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val workout = workoutAdapter.getCharacterPosition(viewHolder.adapterPosition)
                viewModel.deleteWorkout(workout).also {
                    collectObserver()
                    toast(getString(R.string.message_delete_workout))
                }
            }
        }
    }

    private fun collectObserver() = with(binding) {
        emptyList.show()
        viewModel.getWorkoutPlan(homeSelectDropdownText.text.toString())
        viewModel.safeFetch()
        viewModel.workoutPlanList.observe(viewLifecycleOwner){
            val workoutPlanNameList: ArrayList<String> = arrayListOf()
            for (item in it){
                workoutPlanNameList.add(item.workoutPlan.name)
            }
            val equipmentAdapter = ArrayAdapter(requireContext(), R.layout.menu_filter_item, workoutPlanNameList)
            homeSelectDropdownText.setAdapter(equipmentAdapter)
            homeSelectDropdownText.setOnItemClickListener { _, _, _, _ ->
                viewModel.getWorkoutPlan(homeSelectDropdownText.text.toString())
                viewModel.workoutPlan.observe(viewLifecycleOwner){ workoutList->
                    if (workoutList.isEmpty()){
                        workoutAdapter.workoutList = emptyList()
                        emptyList.show()
                    }else{
                        workoutAdapter.workoutList = workoutList.toList()
                        emptyList.gone()
                    }
                }
                viewModel.currentWorkoutPlan = homeSelectDropdownText.text.toString()
            }
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
                    setupWorkoutPlanDialog()
                    true
                }
                R.id.option_2 -> {
                    setupWorkoutDialog()
                    true
                }
                R.id.option_3 -> {
                    true
                }
                R.id.option_4 -> {
                    true
                }
                R.id.option_5 -> {
                    viewModel.deleteWorkoutPlan()
                    collectObserver()
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
    }

    private fun setupWorkoutDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.nameInputLayoutText)
        with(builder){
            setTitle(R.string.create_exercise_list)
            setPositiveButton("Ok"){dialog, which ->
                if (editText.text.toString().isEmpty()){
                    toast(R.string.empty_text.toString())
                }else{
                    viewModel.createWorkout(editText.text.toString())
                }
                collectObserver()
            }
            setNegativeButton("Cancel"){dialog, which ->
            }
            setView(dialogLayout)
            show()
        }
    }

    private fun setupWorkoutPlanDialog(){
        val builder = MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.nameInputLayoutText)

        with(builder){
            setTitle(R.string.create_workout_plan)
            setPositiveButton("Ok"){dialog, which ->
                if (editText.text.toString().isEmpty()){
                    toast(R.string.empty_text.toString())
                }else{
                    viewModel.createWorkoutPlan(editText.text.toString())

                }
                collectObserver()
            }
            setNegativeButton("Cancel"){dialog, which ->

            }
            setView(dialogLayout)
            show()
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