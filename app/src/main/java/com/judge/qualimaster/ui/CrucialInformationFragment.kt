package com.judge.qualimaster.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.judge.core.domain.result.Response
import com.judge.core.presentation.state.AthleteListState
import com.judge.qualimaster.R
import com.judge.qualimaster.adapter.QualificationRecyclerviewAdapter
import com.judge.qualimaster.databinding.FragmentCrucialInformationBinding
import com.judge.qualimaster.ui.viewmodels.CrucialInformationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CrucialInformationFragment : Fragment(R.layout.fragment_crucial_information) {

    private lateinit var binding: FragmentCrucialInformationBinding
    private val viewModel: CrucialInformationViewModel by viewModels()

    private var formatter = SimpleDateFormat("HH:mm:ss")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCrucialInformationBinding.bind(view)

        val adapter = QualificationRecyclerviewAdapter(emptyList())

        binding.rvCurrentInformationList.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }

        binding.tvStartTime.setOnClickListener {
            val originalStartTime = viewModel.startTime.value ?: 0L
            val calendar = Calendar.getInstance().apply { timeInMillis = originalStartTime }

            val picker = createTimePicker(calendar)
            picker.show(parentFragmentManager, "tag")
            picker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
                calendar.set(Calendar.MINUTE, picker.minute)

                if (calendar.timeInMillis != originalStartTime) {
                    lifecycleScope.launchWhenStarted {
                        when (val response = viewModel.setStartTime(calendar.timeInMillis)) {
                            // is Response.Success -> Toast.makeText(context, response.info, Toast.LENGTH_SHORT).show()
                            is Response.Error -> Toast.makeText(context, response.msg, Toast.LENGTH_SHORT).show()
                        }
//                        Snackbar.make(view, "Changed competition starting time.", Snackbar.LENGTH_SHORT)
//                                .setAction("Undo") {
//                                    viewModel.setStartTime(originalStartTime)
//                                }
//                                .show()
                    }
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when(state) {
                is AthleteListState.Loading ->  Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                is AthleteListState.Active ->  Toast.makeText(context, "Active", Toast.LENGTH_SHORT).show()
                is AthleteListState.Error -> Toast.makeText(context, state.msg, Toast.LENGTH_LONG).show()
                else -> Toast.makeText(context, "UNEXPECTED", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.crucialInformation.observe(viewLifecycleOwner, Observer { list ->
            adapter.athleteList = list
            adapter.notifyDataSetChanged()
        })

        viewModel.startTime.observe(viewLifecycleOwner, Observer { time ->
            binding.tvStartTime.text = formatter.format(time)
        })

        viewModel.currentTime.observe(viewLifecycleOwner, Observer { time ->
            binding.tvCurrentTime.text = formatter.format(time)
        })

        viewModel.currentRotation.observe(viewLifecycleOwner, Observer { rotation ->
            binding.tvCurrentRotation.text = rotation.toString()
        })
    }

    private fun createTimePicker(calendar: Calendar) : MaterialTimePicker {
        return MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .build()
    }

}