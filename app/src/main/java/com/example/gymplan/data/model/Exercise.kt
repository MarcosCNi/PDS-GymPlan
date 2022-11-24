package com.example.gymplan.data.model

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import java.io.Serializable

data class Exercise(
    val bodyPart: String? = null,
    val equipment: String? = null,
    val gifUrl: String? = null,
    val id: String? = null,
    val name: String? = null,
    val target: String? = null,
    val weight: String? = null,
    val sets: String? = null,
    val reps: String? = null,
    val workoutId: String? = null
) : Serializable {

    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "bodyPart" to bodyPart,
            "equipment" to equipment,
            "gifUrl" to gifUrl,
            "id" to id,
            "name" to name,
            "target" to target,
            "weight" to weight,
            "sets" to sets,
            "reps" to reps,
            "workoutId" to workoutId
        )
    }
}
