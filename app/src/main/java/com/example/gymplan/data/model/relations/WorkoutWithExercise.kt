package com.example.gymplan.data.model.relations

import androidx.room.*
import com.example.gymplan.data.model.ExerciseModel
import com.example.gymplan.data.model.entity.Exercise
import com.example.gymplan.data.model.entity.WorkoutModel

data class WorkoutWithExercise(
    @Embedded val workout: WorkoutModel,
    @Relation(
        parentColumn = "workoutName",
        entityColumn = "name"
    )
    val exerciseList: List<Exercise>
)
