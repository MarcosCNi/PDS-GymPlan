package com.example.gymplan.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkoutModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val workoutPlanName: String,
    val desc: String? = null,
)
