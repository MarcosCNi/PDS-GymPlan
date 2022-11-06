package com.example.gymplan.ui.customlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymplan.data.local.ExerciseDao
import com.example.gymplan.data.model.entity.WorkoutModel
import com.example.gymplan.data.model.relations.WorkoutWithExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomExerciseListViewModel @Inject constructor(
    private val dao: ExerciseDao
) : ViewModel() {

    private val _workout = MutableLiveData<WorkoutWithExercise>()
    val workout: LiveData<WorkoutWithExercise> get() = _workout

    fun getExerciseList(name: String) = viewModelScope.launch{
        _workout.value = dao.getWorkoutWithExercise(name)
    }
}