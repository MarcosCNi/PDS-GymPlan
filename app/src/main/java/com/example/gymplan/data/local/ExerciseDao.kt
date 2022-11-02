package com.example.gymplan.data.local

import androidx.room.*
import com.example.gymplan.data.model.entity.WorkoutModel
import com.example.gymplan.data.model.entity.WorkoutPlanModel
import com.example.gymplan.data.model.relations.WorkoutPlanWithWorkout

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPlan(workoutPlan: WorkoutPlanModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutModel)

    @Update
    suspend fun updateWorkoutPlan(workoutPlan: WorkoutPlanModel)

    @Update
    suspend fun updateWorkout(workout: WorkoutModel)

    @Transaction
    @Query("SELECT * FROM WorkoutPlanModel")
    suspend fun getAllWorkoutPlanWithWorkout(): List<WorkoutPlanWithWorkout>

    @Transaction
    @Query("SELECT * FROM WorkoutModel WHERE workoutPlanName =:workoutPlanName")
    suspend fun getWorkoutPlanWithWorkout(workoutPlanName: String): List<WorkoutModel>

    //DELETE QUERY
    @Query("DELETE FROM WorkoutPlanModel WHERE name =:workoutName")
    suspend fun deleteByWorkoutPlanName(workoutName: String)

    @Query("DELETE FROM WorkoutModel WHERE name =:name")
    suspend fun deleteByWorkoutName(name: String)

}