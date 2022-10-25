package com.example.gymplan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkoutPlanModel(
    @PrimaryKey(autoGenerate = false)
    val name: String,
)
