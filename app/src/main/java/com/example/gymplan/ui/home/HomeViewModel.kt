package com.example.gymplan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymplan.data.local.ExerciseDao
import com.example.gymplan.data.model.WorkoutModel
import com.example.gymplan.data.model.WorkoutPlanModel
import com.example.gymplan.data.model.relations.WorkoutPlanWithWorkout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dao: ExerciseDao
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> get() = _user

    private val _workoutPlanList = MutableLiveData<List<WorkoutPlanWithWorkout>>()
    val workoutPlanList: LiveData<List<WorkoutPlanWithWorkout>> get() = _workoutPlanList

    private val _workoutPlan = MutableLiveData<List<WorkoutModel>>()
    val workoutPlan: LiveData<List<WorkoutModel>> get() = _workoutPlan

    var currentWorkoutPlan: String = ""

    init {
        verifyUser()
        safeFetch()
    }

    fun safeFetch() = viewModelScope.launch {
        fetch()
    }

    private suspend fun fetch() {
        _workoutPlanList.value = dao.getAllWorkoutPlanWithWorkout()
    }

    private fun verifyUser() = viewModelScope.launch{
        _user.value = firebaseAuth.currentUser
    }

    fun createWorkoutPlan(name: String) = viewModelScope.launch{
        dao.insertWorkoutPlan(WorkoutPlanModel(name))
    }

    fun createWorkout(name: String) = viewModelScope.launch {
        dao.insertWorkout(WorkoutModel(name, currentWorkoutPlan))
    }

    fun getWorkoutPlan(name: String) = viewModelScope.launch {
        _workoutPlan.value = dao.getWorkoutPlanWithWorkout(name)
    }

    fun deleteWorkoutPlan() = viewModelScope.launch {
        dao.deleteByWorkoutPlanName(currentWorkoutPlan)
    }

    fun deleteWorkout(workout: WorkoutModel)= viewModelScope.launch {
        dao.deleteByWorkoutName(workout.name)
    }
}