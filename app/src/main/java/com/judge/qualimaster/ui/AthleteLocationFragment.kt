package com.judge.qualimaster.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.judge.core.data.Repository
import com.judge.qualimaster.R
import com.judge.qualimaster.adapter.QualificationRecyclerviewAdapter
import com.judge.qualimaster.databinding.FragmentAthleteLocationBinding
import com.judge.qualimaster.ui.viewmodels.AthleteLocationViewModel
import com.judge.qualimaster.ui.viewmodels.QualificationViewModelFactory
import com.judge.qualimaster.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AthleteLocationFragment : Fragment(R.layout.fragment_athlete_location) {

    private lateinit var binding: FragmentAthleteLocationBinding
    @Inject lateinit var repository: Repository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAthleteLocationBinding.bind(view)

        val competitionId = arguments?.getLong(Constants.COMPETITION_ID_BUNDLE) ?: -1
        val viewModel: AthleteLocationViewModel by viewModels {
            QualificationViewModelFactory(repository, competitionId)
        }

        val adapter = QualificationRecyclerviewAdapter(emptyList())

        binding.rvAthleteLocation.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }

        viewModel.athleteLocation.observe(viewLifecycleOwner, Observer { list ->
            adapter.athleteList = list
            adapter.notifyDataSetChanged()
        })
    }
}