package com.example.gymplan.data.local

import androidx.room.*
import com.example.gymplan.data.model.entity.Exercise
import com.example.gymplan.data.model.entity.WorkoutModel
import com.example.gymplan.data.model.entity.WorkoutPlanModel
import com.example.gymplan.data.model.relations.WorkoutPlanWithWorkout
import com.example.gymplan.data.model.relations.WorkoutWithExercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPlan(workoutPlan: WorkoutPlanModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Update
    suspend fun updateWorkoutPlan(workoutPlan: WorkoutPlanModel)

    @Update
    suspend fun updateWorkout(workout: WorkoutModel)

    @Transaction
    @Query("SELECT * FROM WorkoutPlanModel")
    suspend fun getAllWorkoutPlanWithWorkout(): List<WorkoutPlanWithWorkout>

    @Transaction
    @Query("SELECT * FROM WorkoutModel WHERE workoutPlanName =:workoutPlanName")
    suspend fun getWorkoutModelList(workoutPlanName: String): List<WorkoutModel>

    @Transaction
    @Query("SELECT * FROM WorkoutPlanModel WHERE name =:workoutPlanName")
    suspend fun getWorkoutPlan(workoutPlanName: String): WorkoutPlanWithWorkout

    @Transaction
    @Query("SELECT * FROM WorkoutModel WHERE name=:workoutName")
    suspend fun getWorkoutWithExercise(workoutName: String) : WorkoutWithExercise

    //DELETE QUERY
    @Query("DELETE FROM WorkoutPlanModel WHERE name =:workoutName")
    suspend fun deleteByWorkoutPlanName(workoutName: String)

    @Query("DELETE FROM WorkoutModel WHERE name =:name")
    suspend fun deleteByWorkoutName(name: String)

}