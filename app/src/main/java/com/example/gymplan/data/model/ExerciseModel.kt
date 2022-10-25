package com.example.gymplan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExerciseModel (
    val bodyPart: String? = null,
    val equipment: String? = null,
    val gifUrl: String? = null,
    val id: String? = null,
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val workoutName: String,
    val target: String? = null
)

