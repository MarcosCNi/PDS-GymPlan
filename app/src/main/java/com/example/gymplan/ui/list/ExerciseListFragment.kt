package com.example.gymplan.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymplan.R
import com.example.gymplan.databinding.FragmentExerciseListBinding
import com.example.gymplan.ui.adapters.ExerciseListAdapter
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.ui.state.ResourceState
import com.example.gymplan.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ExerciseListFragment : BaseFragment<FragmentExerciseListBinding, ExerciseListViewModel>() {

    override val viewModel: ExerciseListViewModel by viewModels()
    private val exerciseAdapter by lazy { ExerciseListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        collectObserver()
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.list.observe(viewLifecycleOwner){
            exerciseAdapter.exercises = it.toList()
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvExerciseList.apply {
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExerciseListBinding = FragmentExerciseListBinding.inflate(inflater, container, false)
}