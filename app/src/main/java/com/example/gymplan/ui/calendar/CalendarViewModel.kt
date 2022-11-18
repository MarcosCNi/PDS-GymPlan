package com.example.gymplan.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymplan.data.local.ExerciseDao
import com.example.gymplan.data.model.relations.CompletedWorkoutWithExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dao: ExerciseDao
) : ViewModel() {

    private val _workout = MutableLiveData<CompletedWorkoutWithExercise>()
    val workout: LiveData<CompletedWorkoutWithExercise> get() = _workout

    fun fetch(id: String) = viewModelScope.launch {
        _workout.value = dao.getCompletedWorkoutWithExercise(id)
    }
}