package com.example.gymplan.data.local

import androidx.room.*
import com.example.gymplan.data.model.ExerciseModel
import com.example.gymplan.data.model.entity.CompletedWorkoutModel
import com.example.gymplan.data.model.entity.Exercise
import com.example.gymplan.data.model.entity.WorkoutModel
import com.example.gymplan.data.model.entity.WorkoutPlanModel

@Database(entities = [WorkoutPlanModel::class, WorkoutModel::class, Exercise::class, CompletedWorkoutModel::class], version = 1, exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase(){
    abstract fun exerciseDao() : ExerciseDao
}