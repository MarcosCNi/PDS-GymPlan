package com.example.gymplan.data.local

import androidx.room.*
import com.example.gymplan.data.model.ExerciseModel
import com.example.gymplan.data.model.entity.WorkoutModel
import com.example.gymplan.data.model.entity.WorkoutPlanModel

@Database(entities = [WorkoutPlanModel::class, WorkoutModel::class, ExerciseModel::class], version = 1, exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase(){
    abstract fun exerciseDao() : ExerciseDao
}