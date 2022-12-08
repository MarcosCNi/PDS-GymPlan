package com.example.gymplan.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gymplan.data.model.Exercise
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
        var weight = exercise.weight
        var sets = exercise.sets
        var reps = exercise.reps
        holder.binding.textInputEditTextWeight.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                weight = holder.binding.textInputEditTextWeight.text.toString()
                doOnFocusChanged?.let {
                    it(
                        Exercise(
                            exercise.bodyPart,
                            exercise.equipment,
                            exercise.gifUrl,
                            exercise.id,
                            exercise.name,
                            exercise.target,
                            weight,
                            sets,
                            reps,
                            exercise.workoutId
                        )
                    )
                }
            }
        }
        holder.binding.textInputEditTextSets.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                sets = holder.binding.textInputEditTextSets.text.toString()
                doOnFocusChanged?.let {
                    it(
                        Exercise(
                            exercise.bodyPart,
                            exercise.equipment,
                            exercise.gifUrl,
                            exercise.id,
                            exercise.name,
                            exercise.target,
                            weight,
                            sets,
                            reps,
                            exercise.workoutId
                        )
                    )
                }
            }
        }
        holder.binding.textInputEditTextReps.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                reps = holder.binding.textInputEditTextReps.text.toString()
                doOnFocusChanged?.let {
                    it(
                        Exercise(
                            exercise.bodyPart,
                            exercise.equipment,
                            exercise.gifUrl,
                            exercise.id,
                            exercise.name,
                            exercise.target,
                            weight,
                            sets,
                            reps,
                            exercise.workoutId
                        )
                    )
                }
            }
        }
    }

    override fun getItemCount() = exercises.size

    private var doOnFocusChanged: ((Exercise) -> Unit)? = null

    private var onCheckBoxClickListener: ((Exercise) -> Unit)? = null

    fun setDoOnFocusChanged(listener: (Exercise) -> Unit) {
        doOnFocusChanged = listener
    }

    fun setOnCheckBoxClickListener(listener: (Exercise) -> Unit) {
        onCheckBoxClickListener = listener
    }

    fun getExercisePosition(position: Int): Exercise {
        return exercises[position]
    }
}