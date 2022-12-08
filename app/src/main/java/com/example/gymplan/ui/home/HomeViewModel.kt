package com.example.gymplan.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymplan.data.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
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

    private val _completedExerciseList = MutableLiveData<List<Exercise>>()
    val completedExerciseList: LiveData<List<Exercise>> get() = _completedExerciseList

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
        firebaseDatabase.child("data").child("workoutPlan").child(workoutPlan.id.toString()).child("name").setValue(newName)
            .addOnSuccessListener {
                getWorkoutPlanList()
            }
    }

    fun editWorkout(newName: String, workout: Workout, workoutPlan: WorkoutPlan) {
        firebaseDatabase.child("data").child("workout").child(workout.id.toString()).child("name").setValue(newName)
            .addOnSuccessListener {
                getWorkoutList(workoutPlan)
            }
    }

    fun editExercise(exercise: Exercise, workout: Workout) {
        firebaseDatabase.child("data").child("exercise").child(exercise.id.toString()).setValue(exercise)
            .addOnSuccessListener {
                getExerciseList(workout)
            }
    }

    fun getWorkoutPlanList() = viewModelScope.launch {
        val uid = _user.value?.uid ?: ""
        firebaseDatabase.child("data")
            .child("workoutPlan")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()){
                    val workoutPlanList = arrayListOf<WorkoutPlan>()
                    for (item in snapshot.children){
                        val workoutPlan = item.getValue(WorkoutPlan::class.java)
                        if (workoutPlan!= null){
                            if(workoutPlan.uid.equals(uid) || workoutPlan.athleteUid.equals(uid)){
                                workoutPlanList.add(workoutPlan)
                            }
                        }
                    }
                    _workoutPlanList.value = workoutPlanList
                }else{
                    _workoutList.value = arrayListOf()
                }
            }
    }

    fun getWorkoutList(workoutPlan: WorkoutPlan) {
        firebaseDatabase.child("data")
            .child("workout")
            .orderByChild("workoutPlanId")
            .equalTo(workoutPlan.id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val workoutList = arrayListOf<Workout>()
                        for(item in snapshot.children){
                            val newWorkout = item.getValue(Workout::class.java)
                            if(newWorkout != null) {
                                workoutList.add(newWorkout)
                            }
                        }
                        _workoutList.value = workoutList
                    }else{
                        _workoutList.value = arrayListOf()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    _workoutList.value = arrayListOf()
                }
            })
    }

    fun getExerciseList(workout: Workout) {
        firebaseDatabase.child("data")
            .child("exercise")
            .orderByChild("workoutId")
            .equalTo(workout.id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val exerciseList = arrayListOf<Exercise>()
                        for(item in snapshot.children){
                            val newExercise = item.getValue(Exercise::class.java)
                            if(newExercise != null) {
                                exerciseList.add(newExercise)
                            }
                        }
                        _exerciseList.value = exerciseList
                    }else{
                        _exerciseList.value = arrayListOf()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    _exerciseList.value = arrayListOf()
                }
            })
    }

    fun getCompletedExerciseList(id: String) {
        firebaseDatabase.child("data")
            .child("user-workout")
            .child(_user.value!!.uid)
            .child("completedExerciseList")
            .child(id)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()){
                    val exerciseList = arrayListOf<Exercise>()
                    for (item in snapshot.children){
                        val newExercise = item.getValue(Exercise::class.java)
                        if (newExercise!= null){
                            exerciseList.add(newExercise)
                        }
                    }
                    _completedExerciseList.value = exerciseList
                }else{
                    _completedExerciseList.value = arrayListOf()
                }
            }
    }

    fun addSharedWorkoutPlan(workoutPlanId: String){
        val uid = _user.value?.uid ?: ""
        val childUpdates = hashMapOf<String, Any>(
            "/workoutPlan/$workoutPlanId/athleteUid" to uid,
            "/user-workout/$uid/workoutPlanList/$workoutPlanId" to true,
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun addWorkoutPlan(name: String){
        val key = firebaseDatabase.child("workoutPlan").push().key
        if(key == null){
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }
        val uid = _user.value!!.uid
        val workoutPlan = WorkoutPlan(
            uid = uid,
            id = key,
            name = name
        )
        val workoutPlanValues = workoutPlan.toMap()
        val childUpdates = hashMapOf(
            "/workoutPlan/$key" to workoutPlanValues,
            "/user-workout/$uid/workoutPlanList/$key" to true,
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun addWorkout(name: String, workoutPlan: WorkoutPlan){
        val key = firebaseDatabase.child("workout").push().key
        if(key == null){
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }
        val workout = Workout(
            id = key,
            name = name,
            desc = null,
            workoutPlan.id
        )
        val workoutValues = workout.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workout/$key" to workoutValues,
            "/workoutPlan/${workoutPlan.id}/workoutList/$key" to workoutValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun addCompletedWorkout(exercise: Exercise) {
        val calendar = Calendar.getInstance()
        val completedDate = calendar.get(Calendar.MONTH).toString() + calendar.get(Calendar.DAY_OF_MONTH).toString() + calendar.get(Calendar.YEAR).toString()
        val exerciseValues = exercise.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/completedExercise/$completedDate" to exerciseValues,
            "/user-workout/${_user.value!!.uid}/completedExerciseList/$completedDate/${exercise.id}" to exerciseValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun addExercise(exercise: Exercise, workout: Workout) {
        val key = firebaseDatabase.child("exercise").push().key
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
            weight = "",
            sets = "",
            reps = "",
            workout.id
        )
        val exerciseValues = newExercise.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/exercise/$key" to exerciseValues,
            "/workout/${workout.id}/exerciseList/$key" to exerciseValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
        getExerciseList(workout)
    }

    fun deleteWorkoutPlan(workoutPlan: WorkoutPlan) = viewModelScope.launch {
        val uid = _user.value!!.uid
        val newWorkoutPlan = WorkoutPlan()
        val workoutPlanValues = newWorkoutPlan.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workoutPlan/${workoutPlan.id}" to workoutPlanValues,
            "/user-workout/$uid/workoutPlanList/${workoutPlan.id}" to workoutPlanValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
        if(_workoutList.value != null){
            for(item in _workoutList.value!!){
                deleteWorkout(item, workoutPlan)
            }
        }
    }

    fun deleteWorkout(workout: Workout, workoutPlan: WorkoutPlan) = viewModelScope.launch{
        val newWorkout = Workout()
        val workoutValues = newWorkout.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/workout/${workout.id}" to workoutValues,
            "/workoutPlan/${workoutPlan.id}/workoutList/${workout.id}" to workoutValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
        if(_exerciseList.value != null){
            for(item in _exerciseList.value!!){
                deleteExercise(item)
            }
        }
    }

    fun deleteExercise(exercise: Exercise) = viewModelScope.launch{
        firebaseDatabase.child("data").child("exercise")
        val newExercise = Exercise()
        val exerciseValues = newExercise.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/exercise/${exercise.id}" to exerciseValues,
            "/workout/${exercise.workoutId}/exerciseList/${exercise.id}" to exerciseValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }

    fun deleteCompletedWorkout(exercise: Exercise) {
        val calendar = Calendar.getInstance()
        val completedDate = calendar.get(Calendar.MONTH).toString() + calendar.get(Calendar.DAY_OF_MONTH).toString() + calendar.get(Calendar.YEAR).toString()
        val newExercise = Exercise()
        val exerciseValues = newExercise.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/completedExercise/$completedDate" to exerciseValues,
            "/user-workout/${_user.value!!.uid}/completedExerciseList/$completedDate/${exercise.id}" to exerciseValues
        )
        firebaseDatabase.child("data").updateChildren(childUpdates)
    }
}