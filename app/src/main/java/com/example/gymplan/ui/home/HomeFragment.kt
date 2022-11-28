package com.example.gymplan.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymplan.R
import com.example.gymplan.data.model.Workout
import com.example.gymplan.databinding.FragmentHomeBinding
import com.example.gymplan.ui.adapters.WorkoutListAdapter
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.show
import com.example.gymplan.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(){

    override val viewModel: HomeViewModel by viewModels()
    private val workoutAdapter by lazy { WorkoutListAdapter() }
    private var workoutPlanId = ""

    override fun onResume() {
        super.onResume()
        realtimeDatabaseCollectObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUser()
        setupRecyclerView()
        realtimeDatabaseCollectObservers()
        clickAdapter()
        setupOptionBtn()
        setupFAB()
    }

    private fun loadWorkoutPlan() {
        viewModel.addSharedWorkoutPlan(workoutPlanId)
    }

    private fun realtimeDatabaseCollectObservers() = with(binding) {
        viewModel.getWorkoutPlanList()
        viewModel.workoutPlanList.observe(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                emptyWokoutPlan.show()
//                toast(getString(R.string.create_workout_plan))
            }else{
                emptyWokoutPlan.gone()
                val workoutPlanNameList: ArrayList<String> = arrayListOf()
                for (item in it){
                    if (item.name == homeSelectDropdownText.text.toString()){
                        viewModel.getWorkoutList(item)
                    }
                    item.name?.let { itemName -> workoutPlanNameList.add(itemName) }
                }
                val workoutPlanNameAdapter = ArrayAdapter(requireContext(), R.layout.menu_filter_item, workoutPlanNameList)
                homeSelectDropdownText.setAdapter(workoutPlanNameAdapter)
                if (workoutPlanNameList.isNotEmpty() && homeSelectDropdownText.text.toString() == getString(R.string.empty_workout_plan_list)) {
                    homeSelectDropdownText.setText(it[0].name)
                    viewModel.getWorkoutList(it[0])
                }
                viewModel.workoutList.observe(viewLifecycleOwner){ workoutList ->
                    if(workoutList.isNullOrEmpty()){
                        workoutAdapter.workoutList = emptyList()
                        leftBottomAddFab.gone()
                        centerAddFab.show()
                        emptyList.show()
                    }else{
                        workoutAdapter.workoutList = workoutList.toList()
                        leftBottomAddFab.show()
                        centerAddFab.gone()
                        emptyList.gone()
                    }
                }
            }
        }
    }

    private fun setupFAB() = with(binding) {
        centerAddFab.setOnClickListener {
            setupWorkoutDialog()
        }
        leftBottomAddFab.setOnClickListener {
            setupWorkoutDialog()
        }
        val equipmentAdapter = ArrayAdapter(requireContext(), R.layout.menu_filter_item, arrayListOf<String>())
        homeSelectDropdownText.setAdapter(equipmentAdapter)
        homeSelectDropdownText.setOnItemClickListener { _, _, position, _ ->
            viewModel.getWorkoutList(viewModel.workoutPlanList.value!![position])
            realtimeDatabaseCollectObservers()
        }
    }


    private fun clickAdapter() {
        workoutAdapter.setOnclickListener { workoutModel ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToCustomExerciseListFragment(workoutModel)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvPlanList.apply {
            adapter = workoutAdapter
            layoutManager = LinearLayoutManager(context)
        }
        rvPlanList.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {
                val workout = workoutAdapter.getWorkoutPosition(position)
                setupRenameWorkoutDialog(workout)
                rvPlanList.adapter?.notifyItemChanged(position)
            }
            override fun onSwipedRight(position: Int) {
                viewModel.workoutPlanList.value!!.map {
                    if (it.name == homeSelectDropdownText.text.toString()){
                        val workout = workoutAdapter.getWorkoutPosition(position)
                        viewModel.deleteWorkout(workout, it)
                        realtimeDatabaseCollectObservers()
                    }
                }
            }
        })
    }

    private fun setupOptionBtn() = with(binding) {
        optionBtn.setOnClickListener {
            showMenu(it, R.menu.popup_home_page_menu)
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
                    setupRenameWorkoutPlanDialog()
                    true
                }
                R.id.option_4 -> {
                    shareWorkoutPlan()
                    true
                }
                R.id.option_5 -> {
                    deleteWorkoutPlan()
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
    }

    private fun shareWorkoutPlan() = with(binding) {
        viewModel.workoutPlanList.value!!.map {
            if (it.name == homeSelectDropdownText.text.toString()){
                val workoutPlanId = it.id.toString()
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, workoutPlanId)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    private fun deleteWorkoutPlan() = with(binding) {
        viewModel.workoutPlanList.value!!.map {
            if (it.name == homeSelectDropdownText.text.toString()){
                viewModel.deleteWorkoutPlan(it)
                findNavController().navigate(R.id.homeFragment)
                realtimeDatabaseCollectObservers()
            }
        }
    }

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

    //DIALOGS
    private fun setupWorkoutPlanDialog(){
        val builder = MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.nameInputLayoutText)
        with(builder){
            setTitle(R.string.create_workout_plan)
            setPositiveButton("Ok"){dialog, which ->
                if (editText.text.toString().isEmpty()){
                    toast(getString(R.string.empty_text))
                }else{
                    viewModel.addWorkoutPlan(editText.text.toString())
                    realtimeDatabaseCollectObservers()
                    binding.homeSelectDropdownText.text = editText.text
                }
            }
            setNegativeButton("Cancel"){_, _ -> }
            setView(dialogLayout)
            show()
        }
    }

    private fun setupWorkoutDialog() = with(binding) {
        val builder = MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.nameInputLayoutText)
        with(builder){
            setTitle(R.string.create_exercise_list)
            setPositiveButton("Ok"){_, _ ->
                if (editText.text.toString().isEmpty()){
                    toast(getString(R.string.empty_text))
                }else if(homeSelectDropdownText.text.isEmpty()){
                    toast(getString(R.string.empty_workout_plan_list))
                } else{
                    viewModel.workoutPlanList.value!!.map{
                        if(it.name == homeSelectDropdownText.text.toString()){
                            viewModel.addWorkout(editText.text.toString(), it)
                            realtimeDatabaseCollectObservers()
                        }
                    }
                }
            }
            setNegativeButton("Cancel"){_, _ -> }
            setView(dialogLayout)
            show()
        }
    }

    private fun setupRenameWorkoutDialog(workout: Workout) = with(binding) {
        viewModel.getExerciseList(workout)
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.nameInputLayoutText)
        with(builder){
            setTitle(R.string.edit_workout)
            setPositiveButton("Ok"){_, _ ->
                if (editText.text.isNullOrEmpty()){
                    toast(getString(R.string.empty_text))
                }else{
                    viewModel.workoutPlanList.value!!.map {
                        if(it.name == homeSelectDropdownText.text.toString()){
                            viewModel.editWorkout(editText.text.toString(), workout, it)
                            realtimeDatabaseCollectObservers()
                        }
                    }
                }
            }
            setNegativeButton("Cancel"){_, _ -> }
            setView(dialogLayout)
            show()
        }
    }

    private fun setupRenameWorkoutPlanDialog() = with(binding) {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.nameInputLayoutText)
        with(builder){
            setTitle(R.string.edit_workout)
            setPositiveButton("Ok"){_, _ ->
                if (editText.text!!.isEmpty()){
                    toast(getString(R.string.empty_text))
                }else{
                    viewModel.workoutPlanList.value!!.map {
                        if (it.name == homeSelectDropdownText.text.toString()){
                            viewModel.editWorkoutPlan(it, editText.text.toString())
                            homeSelectDropdownText.text = editText.text
                        }
                    }
                    realtimeDatabaseCollectObservers()
                }
            }
            setNegativeButton("Cancel"){_, _ -> }
            setView(dialogLayout)
            show()
        }
    }
}
