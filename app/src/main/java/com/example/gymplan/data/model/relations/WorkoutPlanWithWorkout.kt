package com.example.gymplan.data.model.relations

import androidx.room.*
import com.example.gymplan.data.model.entity.WorkoutModel
import com.example.gymplan.data.model.entity.WorkoutPlanModel

data class WorkoutPlanWithWorkout(
    @Embedded val workoutPlan: WorkoutPlanModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutPlanId"
    )
    val workoutList: List<WorkoutModel>
)
