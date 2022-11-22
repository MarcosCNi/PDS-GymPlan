package com.example.gymplan.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymplan.data.model.StaticExerciseModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val firebaseDatabase: DatabaseReference,
) : ViewModel() {

    private val _list = MutableLiveData<ArrayList<StaticExerciseModel>>()
    val list: LiveData<ArrayList<StaticExerciseModel>> get() = _list

    var query: String = ""
    var bodyPartFilter: String = ""
    var equipmentFilter: String = ""

    init {
        fetch()
    }

     fun fetch() = viewModelScope.launch {
         safeFetch()
    }

    private fun safeFetch() {
        firebaseDatabase.child("exercises").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                val exercises: ArrayList<StaticExerciseModel> = arrayListOf()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val exercise = data.getValue(StaticExerciseModel::class.java)
                            when(exercise != null){
                                (exercise?.name!!.contains(query)&&exercise.equipment!!.contains(equipmentFilter)&&exercise.bodyPart!!.contains(bodyPartFilter))
                                    -> exercises.add(exercise)
                                else -> {}
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
}