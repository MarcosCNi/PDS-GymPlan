package com.example.gymplan.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gymplan.data.model.entity.Exercise
import com.example.gymplan.databinding.ItemExerciseInfoBinding
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.loadImg

class CalendarListAdapter : RecyclerView.Adapter<CalendarListAdapter.ExerciseInfoViewHolder>()  {

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
            exerciseCheckBox.gone()
            tvNameExerciseInfo.text = exercise.name
            tvDescExerciseInfo.text = exercise.target!!.uppercase()
            loadImg(
                imgExercise,
                exercise.gifUrl.toString(),
            )
            textInputEditTextWeight.setText(exercise.weight)
            textInputEditTextReps.setText(exercise.reps)
            textInputEditTextSets.setText(exercise.sets)
        }
    }

    override fun getItemCount() = exercises.size
}