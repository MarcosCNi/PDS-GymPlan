package com.example.gymplan.ui.list

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymplan.R
import com.example.gymplan.data.model.Exercise
import com.example.gymplan.databinding.FragmentExerciseListBinding
import com.example.gymplan.ui.adapters.ExerciseListAdapter
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.utils.Constants.DEFAULT_QUERY
import com.example.gymplan.utils.Constants.LAST_SEARCH_QUERY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseListFragment : BaseFragment<FragmentExerciseListBinding, ExerciseListViewModel>() {

    private val args: ExerciseListFragmentArgs by navArgs()
    override val viewModel: ExerciseListViewModel by viewModels()
    private val exerciseAdapter by lazy { ExerciseListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        bodyPartFilter()
        equipmentFilter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        searchInit(query)
        collectObserver()
        clickAdapter()
    }

    private fun clickAdapter() {
        exerciseAdapter.setOnclickListener {
            if (args.workout != null){
                val exercise = Exercise(
                    it.bodyPart,
                    it.equipment,
                    it.gifUrl,
                    null,
                    it.name,
                    it.target,
                    "0",
                    "0",
                    "0"
                )
                val action = ExerciseListFragmentDirections
                    .actionExerciseListFragmentToCustomExerciseListFragment(args.workout!!, exercise)
                findNavController().navigate(action)
            }
        }
    }


    private fun bodyPartFilter() = with(binding){
        val bodyPartItems = resources.getStringArray(R.array.body_part_list)
        val bodyPartAdapter = ArrayAdapter(requireContext(), R.layout.menu_filter_item, bodyPartItems)
        bodyPartFilterDropdownText.setAdapter(bodyPartAdapter)
        bodyPartFilterDropdownText.setOnItemClickListener { _, _, id, _ ->
            if (id == 0) {
                viewModel.bodyPartFilter = ""
            } else {
                viewModel.bodyPartFilter = bodyPartFilterDropdownText.text.toString().lowercase()
            }
            viewModel.fetch()
        }
    }

    private fun equipmentFilter() = with(binding) {
        val equipmentItems = resources.getStringArray(R.array.equipment_list)
        val equipmentAdapter = ArrayAdapter(requireContext(), R.layout.menu_filter_item, equipmentItems)
        equipmentFilterDropdownText.setAdapter(equipmentAdapter)
        equipmentFilterDropdownText.setOnItemClickListener { _, _, id, _ ->
            if (id == 0) {
                viewModel.equipmentFilter = ""
            } else {
                viewModel.equipmentFilter = equipmentFilterDropdownText.text.toString().lowercase()
            }
            viewModel.fetch()
        }
    }

    private fun searchInit(query: String) = with(binding) {
        edSearchExercise.setText(query)
        edSearchExercise.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO){
                updateExerciseList()
                true
            }else{
                false
            }
        }
        edSearchExercise.setOnKeyListener{_, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                updateExerciseList()
                true
            }else{
                false
            }
        }
    }

    private fun updateExerciseList() = with(binding) {
        edSearchExercise.editableText.trim().let {
            if (it.isEmpty()){
                viewModel.query = ""
            }else{
                viewModel.query = it.toString().lowercase()
            }
            viewModel.fetch()
        }
    }

    private fun collectObserver(){
        viewModel.list.observe(viewLifecycleOwner){
            exerciseAdapter.exercises = it.toList()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.edSearchExercise.editableText.trim().toString())
    }

    private fun setupRecyclerView() = with(binding) {
        rvExerciseList.apply {
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExerciseListBinding = FragmentExerciseListBinding.inflate(inflater, container, false)
}