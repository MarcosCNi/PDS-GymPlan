package com.example.gymplan.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymplan.data.model.ExerciseModel
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel() {

    private val _list = MutableLiveData<ArrayList<ExerciseModel>>()
    val list: LiveData<ArrayList<ExerciseModel>> get() = _list

    init {
        fetch()
    }

     fun fetch() = viewModelScope.launch {
        safeFetch()
    }

    private fun safeFetch() {
        firebaseDatabase.getReference("exercises").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                val exercises: ArrayList<ExerciseModel> = arrayListOf()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val exercise = data.getValue(ExerciseModel::class.java)
                        if (exercise != null) {
                            exercises.add(exercise)
                        }
                    }
                }
                _list.value = exercises
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun searchFetch(query: String) = viewModelScope.launch{
        _list.value!!.clear()
        firebaseDatabase.getReference("exercises").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val exercises: ArrayList<ExerciseModel> = arrayListOf()
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        val exercise = data.getValue(ExerciseModel::class.java)
                        if (exercise!!.name!!.contains(query)){
                            exercises.add(exercise)
                        }
                    }
                    _list.value = exercises
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun filterByEquipment(equipment: String) = viewModelScope.launch {
        _list.value!!.clear()
        firebaseDatabase.getReference("exercises").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val exercises: ArrayList<ExerciseModel> = arrayListOf()
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        val exercise = data.getValue(ExerciseModel::class.java)
                        if (exercise!!.equipment!!.contains(equipment.lowercase(Locale.getDefault()))){
                            exercises.add(exercise)
                        }
                    }
                    _list.value = exercises
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun filterByMuscle(muscle: String) {
        _list.value!!.clear()
        firebaseDatabase.getReference("exercises").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val exercises: ArrayList<ExerciseModel> = arrayListOf()
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        val exercise = data.getValue(ExerciseModel::class.java)
                        if (exercise!!.target!!.contains(muscle.lowercase(Locale.getDefault()))){
                            exercises.add(exercise)
                        }
                    }
                    _list.value = exercises
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}