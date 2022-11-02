package com.example.gymplan.data.model.entity
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Exercise (
    val bodyPart: String? = null,
    val equipment: String? = null,
    val gifUrl: String? = null,
    @NonNull
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String? = null,
    val workoutName: String? = null,
    val target: String? = null
){
    constructor():this(
        "",
        "",
        "",
        0,
        "",
        "",
        ""
    )
}