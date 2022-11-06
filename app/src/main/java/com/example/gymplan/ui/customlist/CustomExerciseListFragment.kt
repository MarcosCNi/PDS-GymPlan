package com.example.gymplan.ui.customlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymplan.data.model.entity.WorkoutModel
import com.example.gymplan.databinding.FragmentCustomExerciseListBinding
import com.example.gymplan.ui.adapters.CustomExerciseListAdapter
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.ui.home.HomeFragment
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.show
import dagger.hilt.android.AndroidEntryPoint

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
    }

    private fun collectObserver() = with(binding) {
        emptyList.gone()
        viewModel.workout.observe(viewLifecycleOwner){
            if (it.exerciseList.isEmpty()){
                customExerciseAdapter.exercises = listOf()
                emptyList.show()
            }else{
                customExerciseAdapter.exercises = it.exerciseList.toList()
                emptyList.gone()
            }
        }
    }

    private fun setData() = with(binding) {
        topNavigationBar.title = workoutModel.name
        topNavigationBar.setNavigationOnClickListener {
            val action = CustomExerciseListFragmentDirections
                .actionCustomExerciseListFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        viewModel.getExerciseList(workoutModel.name)
    }

    private fun setupRecyclerView() = with(binding) {
        rvCustomExerciseList.apply {
            adapter = customExerciseAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCustomExerciseListBinding = FragmentCustomExerciseListBinding.inflate(inflater, container, false)
}