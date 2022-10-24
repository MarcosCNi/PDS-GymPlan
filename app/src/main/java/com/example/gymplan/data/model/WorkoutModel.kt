package com.example.gymplan.data.model

data class WorkoutModel(
    var name: String,
    var desc: String? = null,
    var exerciseList: List<ExerciseModel>? = null
)
