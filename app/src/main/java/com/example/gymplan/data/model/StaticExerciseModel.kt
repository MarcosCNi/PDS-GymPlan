package com.example.gymplan.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


data class StaticExerciseModel (
    val bodyPart: String? = null,
    val equipment: String? = null,
    val gifUrl: String? = null,
    val id: String? =null,
    val name: String? = null,
    val workoutName: String? = null,
    val target: String? = null
)
