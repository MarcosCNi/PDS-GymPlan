package com.example.gymplan.ui.list

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.gymplan.utils.toast
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
            }else{
                toast(it.name!!)
            }
        }
    }

    private fun bodyPartFilter() = with(binding){
        val bodyPartItems = resources.getStringArray(R.array.body_part_list)
        val bodyPartAdapter = ArrayAdapter(requireContext(), R.layout.menu_filter_item, bodyPartItems)
        bodyPartFilterDropdownText.setAdapter(bodyPartAdapter)
        bodyPartFilterDropdownText.setOnItemClickListener { _, _, id, _ ->
            when(id){
                1 ->{
                    viewModel.bodyPartFilter = "back"
                }
                2 ->{
                    viewModel.bodyPartFilter = "cardio"
                }
                3 ->{
                    viewModel.bodyPartFilter = "chest"
                }
                4 ->{
                    viewModel.bodyPartFilter = "lower arms"
                }
                5 ->{
                    viewModel.bodyPartFilter = "lower legs"
                }
                6 ->{
                    viewModel.bodyPartFilter = "neck"
                }
                7 ->{
                    viewModel.bodyPartFilter = "shoulders"
                }
                8 ->{
                    viewModel.bodyPartFilter = "upper arms"
                }
                9 ->{
                    viewModel.bodyPartFilter = "upper legs"
                }
                10 ->{
                    viewModel.bodyPartFilter = "waist"
                }
                else ->{
                    viewModel.bodyPartFilter = ""
                }
            }
//            if (id == 0) {
//                viewModel.bodyPartFilter = ""
//            } else {
//                viewModel.bodyPartFilter = bodyPartFilterDropdownText.text.toString()
//            }
            viewModel.fetch()
        }
    }

    private fun equipmentFilter() = with(binding) {
        val equipmentItems = resources.getStringArray(R.array.equipment_list)
        val equipmentAdapter = ArrayAdapter(requireContext(), R.layout.menu_filter_item, equipmentItems)
        equipmentFilterDropdownText.setAdapter(equipmentAdapter)
        equipmentFilterDropdownText.setOnItemClickListener { _, _, id, _ ->
            when(id){
                1 ->{
                    viewModel.equipmentFilter = "assisted"
                }
                2 ->{
                    viewModel.equipmentFilter = "band"
                }
                3 ->{
                    viewModel.equipmentFilter = "barbel"
                }
                4 ->{
                    viewModel.equipmentFilter = "body weight"
                }
                5 ->{
                    viewModel.equipmentFilter = "bosu ball"
                }
                6 ->{
                    viewModel.equipmentFilter = "dumbbell"
                }
                7 ->{
                    viewModel.equipmentFilter = "elliptical machine"
                }
                8 ->{
                    viewModel.equipmentFilter = "ez barbell"
                }
                9 ->{
                    viewModel.equipmentFilter = "hammer"
                }
                10 ->{
                    viewModel.equipmentFilter = "kettlebell"
                }
                11 ->{
                    viewModel.equipmentFilter = "leverage machine"
                }
                12 ->{
                    viewModel.equipmentFilter = "olympic barbell"
                }
                13 ->{
                    viewModel.equipmentFilter = "resistance band"
                }
                14 ->{
                    viewModel.equipmentFilter = "roller"
                }
                15 ->{
                    viewModel.equipmentFilter = "rope"
                }
                16 ->{
                    viewModel.equipmentFilter = "skierg machine"
                }
                17 ->{
                    viewModel.equipmentFilter = "sled machine"
                }
                18 ->{
                    viewModel.equipmentFilter = "smith machine"
                }
                19 ->{
                    viewModel.equipmentFilter = "stability ball"
                }
                20 ->{
                    viewModel.equipmentFilter = "stationary bike"
                }
                21 ->{
                    viewModel.equipmentFilter = "stepmill machine"
                }
                22 ->{
                    viewModel.equipmentFilter = "tire"
                }
                23 ->{
                    viewModel.equipmentFilter = "trap bar"
                }
                24 ->{
                    viewModel.equipmentFilter = "upper body ergometer"
                }
                25 ->{
                    viewModel.equipmentFilter = "weighted"
                }
                26 ->{
                    viewModel.equipmentFilter = "wheel roller"
                }
                else ->{
                    viewModel.equipmentFilter = ""
                }
            }
//            if (id == 0) {
//                viewModel.equipmentFilter = ""
//            } else {
//                viewModel.equipmentFilter = equipmentFilterDropdownText.text.toString().lowercase()
//            }
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