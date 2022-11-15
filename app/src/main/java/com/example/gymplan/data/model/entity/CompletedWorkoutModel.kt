package com.example.gymplan.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompletedWorkoutModel(
    @PrimaryKey(autoGenerate = false)
    val id: String,
)