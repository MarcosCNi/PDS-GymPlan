package com.example.gymplan.data.model.entity
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Exercise (
    val bodyPart: String? = null,
    val equipment: String? = null,
    val gifUrl: String? = null,
    @NonNull
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String? = null,
    val workoutId: Int? = null,
    val completedWorkoutId: String? = null,
    val target: String? = null,
    val weight: String? = null,
    val sets: String? = null,
    val reps: String? = null
) : Serializable {
    constructor():this(
        "",
        "",
        "",
        0,
        "",
        0,
        ""
    )
}