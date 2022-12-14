package com.example.gymplan.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gymplan.data.model.Workout
import com.example.gymplan.databinding.ItemWorkoutBinding

class WorkoutListAdapter : RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(val binding: ItemWorkoutBinding):
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Workout>(){
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.name == newItem.name && oldItem.desc == newItem.desc
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var workoutList: List<Workout>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            ItemWorkoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = workoutList.size

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.binding.apply {
            tvNameWorkout.text = workout.name
            tvDescWorkout.text = workout.desc
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(workout)
            }
        }
    }

    private var onItemClickListener: ((Workout) -> Unit)? = null

    fun setOnclickListener(listener: (Workout) -> Unit){
        onItemClickListener = listener
    }

    fun getWorkoutPosition(position: Int): Workout {
        return workoutList[position]
    }
}