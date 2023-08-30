package com.example.gymplan.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gymplan.data.model.StaticExerciseModel
import com.example.gymplan.databinding.ItemExerciseBinding
import com.example.gymplan.utils.limitCharacters
import com.example.gymplan.utils.loadImg
import com.example.gymplan.utils.show

class ExerciseListAdapter : RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemExerciseBinding):
            RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<StaticExerciseModel>(){
        override fun areItemsTheSame(oldItem: StaticExerciseModel, newItem: StaticExerciseModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
        override fun areContentsTheSame(oldItem: StaticExerciseModel, newItem: StaticExerciseModel): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name &&
                    oldItem.bodyPart == newItem.bodyPart && oldItem.gifUrl == newItem.gifUrl &&
                    oldItem.equipment == newItem.equipment && oldItem.target == newItem.target
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var exercises: List<StaticExerciseModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            ItemExerciseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.binding.apply {
            tvNameExercise.text = exercise.name?.limitCharacters(20)
            tvDescExercise.text = exercise.bodyPart!!.uppercase()
            loadImg(
                imgExercise,
                exercise.gifUrl.toString(),
            )
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(exercise)
            }
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.let{
                it(exercise)
            }
            true
        }
    }

    override fun getItemCount() = exercises.size

    private var onItemClickListener: ((StaticExerciseModel) -> Unit)? = null

    private var onLongClickListener: ((StaticExerciseModel) -> Unit)? = null

    fun setOnclickListener(listener: (StaticExerciseModel) -> Unit){
        onItemClickListener = listener
    }

    fun setOnLongClickListener(listener: (StaticExerciseModel) -> Unit){
        onLongClickListener = listener
    }
}