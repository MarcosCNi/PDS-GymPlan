package com.example.gymplan.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymplan.data.model.Exercise
import com.example.gymplan.data.model.User
import com.example.gymplan.data.model.Workout
import com.example.gymplan.data.model.WorkoutPlan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> get() = _user

    private val _workoutPlanList = MutableLiveData<List<WorkoutPlan>>()
    val workoutPlanList: LiveData<List<WorkoutPlan>> get() = _workoutPlanList

    private val _workoutList = MutableLiveData<List<Workout>>()
    val workoutList: LiveData<List<Workout>> get() = _workoutList

    private val _exerciseList = MutableLiveData<List<Exercise>>()
    val exerciseList: LiveData<List<Exercise>> get() = _exerciseList

    init {
        verifyUser()
        getWorkoutPlanList()
    }

    private fun verifyUser() = viewModelScope.launch{
        _user.value = firebaseAuth.currentUser
        if (_user.value != null){
            val user = User(
                _user.value!!.uid
            )
            firebaseDatabase.child("users").child(user.uid.toString()).setValue(user)
        }
    }

    fun editWorkoutPlan(workoutPlan: WorkoutPlan, newName: String) = viewModelScope.launch {
        val newWorkoutPlan = WorkoutPlan(
            workoutPlan.uid,
            workoutPlan.id,
            newName,
            workoutPlan.athleteUid,
        )
        val workoutPlanValues = newWorkoutPlan.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workoutPlan/${workoutPlan.id}" to workoutPlanValues,
            "/user-workout/${_user.value!!.uid}/${workoutPlan.id}" to workoutPlanValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
        if (_workoutList.value != null) {
            for (item in _workoutList.value!!.toList()){
                val workoutValues = item.toMap()
                val childUpdates = hashMapOf<String, Any>(
                    "/workout/${item.id}" to workoutValues,
                    "/workoutPlan/${workoutPlan.id}/workoutList/${item.id}" to workoutValues,
                )
                firebaseDatabase.child("data").updateChildren(childUpdates)
            }
        }
    }

    fun editWorkout(newName: String, workout: Workout, workoutPlan: WorkoutPlan) {
        val newWorkout = Workout(
            workout.id,
            newName,
            workout.desc,
        )
        val workoutValues = newWorkout.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workout/${workout.id}" to workoutValues,
            "/workoutPlan/${workoutPlan.id}/workoutList/${workout.id}" to workoutValues,
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
        if(_exerciseList.value != null){
            for (item in _exerciseList.value!!.toList()){
                val exerciseValues = item.toMap()
                val childUpdates = hashMapOf<String, Any>(
                    "/exercise/${item.id}" to exerciseValues,
                    "/workout/${workout.id}/exerciseList/${item.id}" to exerciseValues,
                )
                firebaseDatabase.child("data").updateChildren(childUpdates)
            }
        }
    }

    fun editExercise(exercise: Exercise, workout: Workout) {
        val newExercise = Exercise(
            bodyPart = exercise.bodyPart,
            equipment = exercise.equipment,
            gifUrl = exercise.gifUrl,
            id = exercise.id,
            name = exercise.name,
            target = exercise.target,
            weight = exercise.weight,
            sets = exercise.sets,
            reps = exercise.reps
        )
        val exerciseValues = newExercise.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/exercise/${exercise.id}" to exerciseValues,
            "/workout/${workout.id}/exerciseList/${exercise.id}" to exerciseValues,
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun getWorkoutPlanList() = viewModelScope.launch {
        firebaseDatabase.child("data").child("user-workout").child(_user.value!!.uid).get()
            .addOnSuccessListener {
                val workoutPlans = arrayListOf<WorkoutPlan>()
                if (it.exists()){
                    for(item in it.children){
                        val workoutPlan = item.getValue(WorkoutPlan::class.java)
                        if(workoutPlan != null){
                            workoutPlans.add(workoutPlan)
                        }
                    }
                    _workoutPlanList.value = workoutPlans
                }else{
                    _workoutList.value = arrayListOf()
                }
            }
    }

    fun getWorkoutList(workoutPlan: WorkoutPlan) {
        firebaseDatabase.child("data").child("workoutPlan").child(workoutPlan.id.toString()).child("workoutList").get()
            .addOnSuccessListener {
                val workoutList = arrayListOf<Workout>()
                if (it.exists()){
                    for(item in it.children){
                        val workout = item.getValue(Workout::class.java)
                        if(workout != null){
                            workoutList.add(workout)
                        }
                    }
                    _workoutList.value = workoutList
                }else{
                    _workoutList.value = arrayListOf()
                }
            }
    }

    fun getExerciseList(workout: Workout) {
        firebaseDatabase.child("data").child("workout").child(workout.id.toString()).child("exerciseList").get()
            .addOnSuccessListener {
                val exerciseList = arrayListOf<Exercise>()
                if (it.exists()){
                    for(item in it.children){
                        val exercise = item.getValue(Exercise::class.java)
                        if(exercise != null){
                            exerciseList.add(exercise)
                        }
                    }
                    _exerciseList.value = exerciseList
                }else{
                    _exerciseList.value = arrayListOf()
                }
            }
    }

    fun addWorkoutPlan(name: String){
        val key = firebaseDatabase.push().key
        if(key == null){
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }
        val workoutPlan = WorkoutPlan(
            uid = _user.value!!.uid,
            id = key,
            name = name
        )
        val workoutPlanValues = workoutPlan.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workoutPlan/$key" to workoutPlanValues,
            "/user-workout/${_user.value!!.uid}/$key" to workoutPlanValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun addWorkout(name: String, workoutPlan: WorkoutPlan){
        val key = firebaseDatabase.push().key
        if(key == null){
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }
        val workout = Workout(
            id = key,
            name = name,
            desc = null
        )
        val workoutValues = workout.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workout/$key" to workoutValues,
            "/workoutPlan/${workoutPlan.id}/workoutList/$key" to workoutValues,
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun addExercise(exercise: Exercise, workout: Workout) {
        val key = firebaseDatabase.push().key
        if(key == null){
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }
        val newExercise = Exercise(
            bodyPart = exercise.bodyPart,
            equipment = exercise.equipment,
            gifUrl = exercise.gifUrl,
            id = key,
            name = exercise.name,
            target = exercise.target,
            weight = "0",
            sets = "0",
            reps = "0"
        )
        val exerciseValues = newExercise.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/exercise/$key" to exerciseValues,
            "/workout/${workout.id}/exerciseList/$key" to exerciseValues,
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun deleteWorkoutPlan(workoutPlan: WorkoutPlan) = viewModelScope.launch {
        val newWorkoutPlan = WorkoutPlan()
        val workoutPlanValues = newWorkoutPlan.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workoutPlan/${workoutPlan.id}" to workoutPlanValues,
            "/user-workout/${_user.value!!.uid}/${workoutPlan.id}" to workoutPlanValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
        if (_workoutList.value != null) {
            for (item in _workoutList.value!!.toList()){
                val workout = Workout()
                val workoutValues = workout.toMap()
                val childUpdates = hashMapOf<String, Any>(
                    "/workout/${item.id}" to workoutValues,
                    "/workoutPlan/${workoutPlan.id}/workoutList/${item.id}" to workoutValues,
                )
                firebaseDatabase.child("data").updateChildren(childUpdates)
            }
        }
    }

    fun deleteWorkout(workout: Workout, workoutPlan: WorkoutPlan){
        val newWorkout = Workout()
        val workoutValues = newWorkout.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workout/${workout.id}" to workoutValues,
            "/workoutPlan/${workoutPlan.id}/workoutList/${workout.id}" to workoutValues,
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
        if(_exerciseList.value != null){
            for (item in _exerciseList.value!!.toList()){
                val exercise = Exercise()
                val exerciseValues = exercise.toMap()
                val childUpdates = hashMapOf<String, Any>(
                    "/exercise/${item.id}" to exerciseValues,
                    "/workout/${workout.id}/exerciseList/${item.id}" to exerciseValues,
                )
                firebaseDatabase.child("data").updateChildren(childUpdates)
            }
        }
    }

    fun deleteExercise(exercise: Exercise, workout: Workout){
        val newExercise = Exercise()
        val exerciseValues = newExercise.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/exercise/${exercise.id}" to exerciseValues,
            "/workout/${workout.id}/exerciseList/${exercise.id}" to exerciseValues,
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }
}