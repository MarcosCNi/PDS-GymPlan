package com.example.gymplan.data.model.relations

import androidx.room.*
import com.example.gymplan.data.model.WorkoutModel
import com.example.gymplan.data.model.WorkoutPlanModel

data class WorkoutPlanWithWorkout(
    @Embedded val workoutPlan: WorkoutPlanModel,
    @Relation(
        parentColumn = "name",
        entityColumn = "workoutPlanName"
    )
    val workoutList: List<WorkoutModel>
)
