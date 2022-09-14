package com.example.gymplan.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.gymplan.databinding.FragmentExerciseListBinding
import com.example.gymplan.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseListFragment : BaseFragment<FragmentExerciseListBinding, ExerciseListViewModel>() {

    override val viewModel: ExerciseListViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExerciseListBinding = FragmentExerciseListBinding.inflate(inflater, container, false)

}