package com.example.gymplan.ui.customList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymplan.data.local.ExerciseDao
import com.example.gymplan.data.model.entity.CompletedWorkoutModel
import com.example.gymplan.data.model.entity.Exercise
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

    fun getExerciseList(id: Int) = viewModelScope.launch{
        _workout.value = dao.getWorkoutWithExercise(id)
    }

    fun createCompletedWorkout(id: String) = viewModelScope.launch {
        dao.insertCompletedWorkout(CompletedWorkoutModel(id))
    }

    fun editExercise(exercise: Exercise) = viewModelScope.launch {
        dao.updateExercise(exercise)
    }

    fun deleteExercise(exercise: Exercise) = viewModelScope.launch {
        dao.deleteExercise(exercise.id)
    }

    fun insertCompletedExercise(exercise: Exercise) = viewModelScope.launch {
        dao.insertExercise(exercise)
    }
}