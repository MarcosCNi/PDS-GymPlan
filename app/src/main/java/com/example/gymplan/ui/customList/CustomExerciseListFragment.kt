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
import com.example.gymplan.data.model.Workout
import com.example.gymplan.data.model.Exercise
import com.example.gymplan.databinding.FragmentCustomExerciseListBinding
import com.example.gymplan.ui.adapters.CustomExerciseListAdapter
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.ui.home.HomeViewModel
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.show
import com.example.gymplan.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomExerciseListFragment : BaseFragment<FragmentCustomExerciseListBinding, HomeViewModel>(){

    override val viewModel: HomeViewModel by viewModels()

    private val args: CustomExerciseListFragmentArgs by navArgs()
    private val customExerciseAdapter: CustomExerciseListAdapter by lazy { CustomExerciseListAdapter() }
    private lateinit var workoutModel: Workout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutModel = args.workout
        setupRecyclerView()
        setData()
        collectObserver()
        setupAdapterFunctions()
        if (args.exercise != null) {
            addExercise(args.exercise!!)
        }
    }
    private fun addExercise(exercise: Exercise) = with(binding) {
        viewModel.addExercise(exercise, workoutModel)
        collectObserver()
    }

    private fun setupAdapterFunctions() {
        customExerciseAdapter.setOnCheckBoxClickListener {
            if (customExerciseAdapter.exercisesChecked.contains(it)){
                customExerciseAdapter.exercisesChecked.remove(it)
                viewModel.deleteCompletedWorkout(it)
            }else{
                customExerciseAdapter.exercisesChecked.add(it)
                viewModel.addCompletedWorkout(it)
            }
        }
        customExerciseAdapter.setDoOnFocusChanged {
            viewModel.editExercise(it, workoutModel)
        }
    }

    private fun collectObserver() = with(binding) {
        viewModel.getExerciseList(workoutModel)
        viewModel.exerciseList.observe(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                customExerciseAdapter.exercises = listOf()
                emptyList.show()
                centerAddFab.show()
                leftBottomAddFab.gone()
            }else{
                customExerciseAdapter.exercises = it.toList()
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
                    true
                }
                else -> false
            }
        }
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
                val exercise = customExerciseAdapter.getExercisePosition(position)
                setupRenameExerciseDialog(exercise)
                rvCustomExerciseList.adapter?.notifyDataSetChanged()
            }
            override fun onSwipedRight(position: Int) {
                val exercise = customExerciseAdapter.getExercisePosition(position)
                viewModel.deleteExercise(exercise, workoutModel)
                collectObserver()
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
                            exercise.target,
                            exercise.weight,
                            exercise.sets,
                            exercise.reps,
                            exercise.workoutId
                        ),
                        workoutModel
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
