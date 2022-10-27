package com.example.gymplan.data.local

import androidx.room.*
import com.example.gymplan.data.model.ExerciseModel
import com.example.gymplan.data.model.WorkoutModel
import com.example.gymplan.data.model.WorkoutPlanModel
import com.example.gymplan.data.model.relations.WorkoutPlanWithWorkout

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPlan(workoutPlan: WorkoutPlanModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutModel)

    @Transaction
    @Query("SELECT * FROM WorkoutPlanModel")
    suspend fun getWorkoutPlanWithWorkout(): List<WorkoutPlanWithWorkout>
}