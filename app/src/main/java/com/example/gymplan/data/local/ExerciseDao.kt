package com.example.gymplan.data.local

import androidx.room.*
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
    suspend fun getAllWorkoutPlanWithWorkout(): List<WorkoutPlanWithWorkout>

    @Transaction
    @Query("SELECT * FROM WorkoutModel WHERE workoutPlanName =:workoutPlanName")
    suspend fun getWorkoutPlanWithWorkout(workoutPlanName: String): List<WorkoutModel>

    @Query("DELETE FROM WorkoutPlanModel WHERE name =:workoutName")
    suspend fun deleteByWorkoutPlanName(workoutName: String)
}