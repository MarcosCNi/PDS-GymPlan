package com.example.gymplan.data.model

import com.google.firebase.database.Exclude

data class WorkoutPlan (
    val uid: String? = null,
    val id: String? = null,
    val name: String? = null,
    val athleteUid: String? = null,
){
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "uid" to uid,
            "id" to id,
            "name" to name,
            "athleteUid" to athleteUid,
        )
    }
}