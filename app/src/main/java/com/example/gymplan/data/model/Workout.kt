package com.example.gymplan.data.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Workout(
    val id: String? = null,
    val name: String? = null,
    val desc: String? = null,
    val workoutPlanId: String? = null
) : Serializable {

    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "id" to id,
            "name" to name,
            "desc" to desc,
            "workoutPlanId" to workoutPlanId
        )
    }
}
