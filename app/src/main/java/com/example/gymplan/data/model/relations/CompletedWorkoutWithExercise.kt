package com.example.gymplan.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymplan.data.model.entity.CompletedWorkoutModel
import com.example.gymplan.data.model.entity.Exercise

data class CompletedWorkoutWithExercise (
    @Embedded val completedWorkout: CompletedWorkoutModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val exerciseList: List<Exercise>
)