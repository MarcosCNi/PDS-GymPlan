package com.example.gymplan.ui.calendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
) : ViewModel() {

//    private val _workout = MutableLiveData<CompletedWorkoutWithExercise>()
//    val workout: LiveData<CompletedWorkoutWithExercise> get() = _workout
//
//    fun fetch(id: String) = viewModelScope.launch {
//        _workout.value = dao.getCompletedWorkoutWithExercise(id)
//    }
}