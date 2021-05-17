package com.judge.qualimaster.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.judge.qualimaster.R
import com.judge.qualimaster.adapter.QualificationRecyclerviewAdapter
import com.judge.qualimaster.databinding.FragmentRotationHistoryBinding
import com.judge.qualimaster.ui.viewmodels.RotationHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RotationHistoryFragment : Fragment(R.layout.fragment_rotation_history) {

    private lateinit var binding: FragmentRotationHistoryBinding
    private val viewModel: RotationHistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FragmentRotationHistoryBinding.bind(view)

        val adapter = QualificationRecyclerviewAdapter(emptyList())

        binding.rvRotationHistory.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }

        viewModel.rotationHistory.observe(viewLifecycleOwner, Observer { list ->
            adapter.athleteList = list
            adapter.notifyDataSetChanged()
        })
    }
}