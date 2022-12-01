package com.example.gymplan.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymplan.databinding.FragmentCalendarBinding
import com.example.gymplan.ui.adapters.CalendarListAdapter
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.ui.home.HomeViewModel
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.show
import com.example.gymplan.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    private val calendarListAdapter: CalendarListAdapter by lazy { CalendarListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
        setupRecyclerView()
        collectObservers()
    }

    private fun collectObservers() = with(binding) {
        if (calendarListAdapter.exercises.isNullOrEmpty()){
            val calendar = Calendar.getInstance()
            val completedDate = calendar.get(Calendar.MONTH).toString() + calendar.get(Calendar.DAY_OF_MONTH).toString() + calendar.get(Calendar.YEAR).toString()
            viewModel.getCompletedExerciseList(completedDate)
        }
        viewModel.completedExerciseList.observe(viewLifecycleOwner){
            if (it == null){
                calendarListAdapter.exercises = listOf()
                emptyCalendarList.show()
            }else{
                if (it.isEmpty()){
                    calendarListAdapter.exercises = listOf()
                    emptyCalendarList.show()
                }else{
                    calendarListAdapter.exercises = it.toList()
                    emptyCalendarList.gone()
                }
            }
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvCalendarExerciseList.apply {
            adapter = calendarListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupCalendar() = with(binding) {
        val calendar = calendarDatePicker
        val actualDate = Calendar.getInstance()
        calendar.init(actualDate.get(Calendar.YEAR), actualDate.get(Calendar.MONTH), actualDate.get(Calendar.DAY_OF_MONTH)){ _, year, month, day ->
            val id = month.toString()+day.toString()+year.toString()
            viewModel.getCompletedExerciseList(id)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, container, false)

}