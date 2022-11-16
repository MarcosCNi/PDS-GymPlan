package com.example.gymplan.ui.customList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymplan.R
import com.example.gymplan.data.model.entity.Exercise
import com.example.gymplan.data.model.entity.WorkoutModel
import com.example.gymplan.databinding.FragmentCustomExerciseListBinding
import com.example.gymplan.ui.adapters.CustomExerciseListAdapter
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.show
import com.example.gymplan.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class CustomExerciseListFragment : BaseFragment<FragmentCustomExerciseListBinding, CustomExerciseListViewModel>(){

    override val viewModel: CustomExerciseListViewModel by viewModels()

    private val args: CustomExerciseListFragmentArgs by navArgs()
    private val customExerciseAdapter: CustomExerciseListAdapter by lazy { CustomExerciseListAdapter() }
    private lateinit var workoutModel: WorkoutModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutModel = args.workoutModel
        setupRecyclerView()
        setData()
        collectObserver()
        setupAdapterFunctions()
    }
    
    private fun setupAdapterFunctions() {
        customExerciseAdapter.setOnCheckBoxClickListener {
            if (customExerciseAdapter.exercisesChecked.contains(it)){
                customExerciseAdapter.exercisesChecked.remove(it)
            }else{
                customExerciseAdapter.exercisesChecked.add(it)
            }
            if (customExerciseAdapter.exercises == customExerciseAdapter.exercisesChecked){
                viewModel.createCompletedWorkout(LocalDate.now().toString() ,customExerciseAdapter.exercisesChecked)
                toast(getString(R.string.completed_workout))
            }
        }
        customExerciseAdapter.setDoOnTextChanged {
            viewModel.editExercise(it)
        }
    }

    private fun collectObserver() = with(binding) {
        viewModel.getExerciseList(workoutModel.id)
        viewModel.workout.observe(viewLifecycleOwner){
            if (it.exerciseList.isEmpty()){
                customExerciseAdapter.exercises = listOf()
                emptyList.show()
                centerAddFab.show()
                leftBottomAddFab.gone()
            }else{
                customExerciseAdapter.exercises = it.exerciseList.toList()
                emptyList.gone()
                centerAddFab.gone()
                leftBottomAddFab.show()
            }
        }
    }

    private fun setData() = with(binding) {
        leftBottomAddFab.setOnClickListener {
            navigateToExerciseList()
        }
        centerAddFab.setOnClickListener {
            navigateToExerciseList()
        }
        topNavigationBar.title = workoutModel.name
        topNavigationBar.setNavigationOnClickListener {
            val action = CustomExerciseListFragmentDirections
                .actionCustomExerciseListFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        topNavigationBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.optionDeleteExerciseList -> {
                    toast(menuItem.title.toString())
                    true
                }
                R.id.optionAddExercise -> {
                    navigateToExerciseList()
                    toast(menuItem.title.toString())
                    true
                }
                else -> false
            }
        }
        viewModel.getExerciseList(workoutModel.id)
    }

    private fun navigateToExerciseList() {
        val action = CustomExerciseListFragmentDirections
            .actionCustomExerciseListFragmentToExerciseListFragment(workoutModel)
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() = with(binding) {
        rvCustomExerciseList.apply {
            adapter = customExerciseAdapter
            layoutManager = LinearLayoutManager(context)
        }
        rvCustomExerciseList.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {
                val exercise = customExerciseAdapter.getWorkoutPosition(position)
                setupRenameExerciseDialog(exercise)
                rvCustomExerciseList.adapter?.notifyDataSetChanged()
                toast("Edit")
            }
            override fun onSwipedRight(position: Int) {
                val exercise = customExerciseAdapter.getWorkoutPosition(position)
                viewModel.deleteExercise(exercise).also {
                    collectObserver()
                    toast(getString(R.string.message_delete_workout))
                }
                rvCustomExerciseList.adapter?.notifyDataSetChanged()
            }
        })
    }

    private fun setupRenameExerciseDialog(exercise: Exercise) {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.nameInputLayoutText)
        with(builder){
            setTitle(R.string.edit_exercise)
            editText.setText(exercise.name)
            setPositiveButton("Ok"){_, _ ->
                if(editText.text!!.isEmpty()){
                    toast(getString(R.string.empty_text))
                }else{
                    viewModel.editExercise(
                        Exercise(
                            exercise.bodyPart,
                            exercise.equipment,
                            exercise.gifUrl,
                            exercise.id,
                            editText.text.toString(),
                            workoutModel.id,
                            null,
                            exercise.target
                        )
                    )
                }
                customExerciseAdapter.notifyDataSetChanged()
                collectObserver()
            }
            setNegativeButton("Cancel"){_, _ -> }
            setView(dialogLayout)
            show()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCustomExerciseListBinding = FragmentCustomExerciseListBinding.inflate(inflater, container, false)
}