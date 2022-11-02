package com.example.gymplan.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.IDN

@Entity
data class WorkoutPlanModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
)
