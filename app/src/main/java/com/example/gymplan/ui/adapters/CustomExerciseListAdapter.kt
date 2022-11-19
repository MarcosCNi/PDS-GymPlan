package com.example.gymplan.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gymplan.data.model.entity.Exercise
import com.example.gymplan.databinding.ItemExerciseInfoBinding
import com.example.gymplan.utils.loadImg

class CustomExerciseListAdapter : RecyclerView.Adapter<CustomExerciseListAdapter.ExerciseInfoViewHolder>() {

    inner class ExerciseInfoViewHolder(val binding: ItemExerciseInfoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return newItem.hashCode() == oldItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name &&
                    oldItem.bodyPart == newItem.bodyPart && oldItem.gifUrl == newItem.gifUrl &&
                    oldItem.equipment == newItem.equipment && oldItem.target == newItem.target
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var exercises: List<Exercise>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    val exercisesChecked: MutableList<Exercise> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseInfoViewHolder {
        return ExerciseInfoViewHolder(
            ItemExerciseInfoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseInfoViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.binding.apply {
            tvNameExerciseInfo.text = exercise.name
            tvDescExerciseInfo.text = exercise.bodyPart!!.uppercase()
            loadImg(
                imgExercise,
                exercise.gifUrl.toString(),
            )
            textInputEditTextWeight.setText(exercise.weight)
            textInputEditTextReps.setText(exercise.reps)
            textInputEditTextSets.setText(exercise.sets)
        }
        holder.binding.exerciseCheckBox.setOnClickListener {
            onCheckBoxClickListener?.let {
                it(exercise)
            }
        }
        var weight = exercise.weight ?: "0"
        var sets = exercise.sets ?: "0"
        var reps = exercise.reps ?: "0"
        holder.binding.textInputEditTextWeight.doOnTextChanged { inputText, _, _, _ ->
            weight = inputText.toString()
            doOnTextChanged?.let {
                it(
                    Exercise(
                        exercise.bodyPart,
                        exercise.equipment,
                        exercise.gifUrl,
                        exercise.id,
                        exercise.name,
                        exercise.workoutId,
                        exercise.completedWorkoutId,
                        exercise.target,
                        weight,
                        sets,
                        reps
                    )
                )
            }
        }
        holder.binding.textInputEditTextSets.doOnTextChanged { inputText, _, _, _ ->
            sets = inputText.toString()
            doOnTextChanged?.let {
                it(
                    Exercise(
                        exercise.bodyPart,
                        exercise.equipment,
                        exercise.gifUrl,
                        exercise.id,
                        exercise.name,
                        exercise.workoutId,
                        exercise.completedWorkoutId,
                        exercise.target,
                        weight,
                        sets,
                        reps
                    )
                )
            }
        }
        holder.binding.textInputEditTextReps.doOnTextChanged { inputText, _, _, _ ->
            reps = inputText.toString()
            doOnTextChanged?.let {
                it(
                    Exercise(
                        exercise.bodyPart,
                        exercise.equipment,
                        exercise.gifUrl,
                        exercise.id,
                        exercise.name,
                        exercise.workoutId,
                        exercise.completedWorkoutId,
                        exercise.target,
                        weight,
                        sets,
                        reps
                    )
                )
            }
        }
    }

    override fun getItemCount() = exercises.size

    private var doOnTextChanged: ((Exercise) -> Unit)? = null

    private var onCheckBoxClickListener: ((Exercise) -> Unit)? = null

    fun setDoOnTextChanged(listener: (Exercise) -> Unit) {
        doOnTextChanged = listener
    }

    fun setOnCheckBoxClickListener(listener: (Exercise) -> Unit) {
        onCheckBoxClickListener = listener
    }

    fun getWorkoutPosition(position: Int): Exercise {
        return exercises[position]
    }
}