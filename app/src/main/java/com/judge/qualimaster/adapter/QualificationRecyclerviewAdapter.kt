package com.judge.qualimaster.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.judge.core.presentation.AthleteListItem
import com.judge.qualimaster.R
import com.judge.qualimaster.databinding.ViewholderAthleteBinding
import com.judge.qualimaster.databinding.ViewholderBoulderBinding
import com.judge.qualimaster.databinding.ViewholderCategoryBinding
import com.judge.qualimaster.databinding.ViewholderHeaderBinding

class QualificationRecyclerviewAdapter(
    var athleteList: List<AthleteListItem>
) : RecyclerView.Adapter<QualificationRecyclerviewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(val item = athleteList[position]) {
            is AthleteListItem.HeaderItem -> {
                val binding = ViewholderHeaderBinding.bind(holder.itemView)
                binding.tvHeaderText.text = item.title
            }
            is AthleteListItem.CategoryItem -> {
                val binding = ViewholderCategoryBinding.bind(holder.itemView)
                binding.tvCategoryName.text = item.category.name
            }
            is AthleteListItem.AthleteItem -> {
                val binding = ViewholderAthleteBinding.bind(holder.itemView)
                binding.tvaAthleteFirstName.text = item.athlete.firstName
                binding.tvaAthleteLastName.text = item.athlete.lastName
                binding.tvaAthleteNumber.text = item.athlete.number.toString()
            }
            is AthleteListItem.BoulderItem -> {
                val binding = ViewholderBoulderBinding.bind(holder.itemView)
                binding.tvbBoulderName.text = item.boulder.name
                binding.tvbAthleteFirstName.text = item.athlete.firstName
                binding.tvbAthleteLastName.text = item.athlete.lastName
                binding.tvbAthleteNumber.text = item.athlete.number.toString()
            }
        }
    }

    override fun getItemCount() = athleteList.size

    override fun getItemViewType(position: Int): Int {
        return when(athleteList[position]) {
            is AthleteListItem.AthleteItem -> R.layout.viewholder_athlete
            is AthleteListItem.BoulderItem -> R.layout.viewholder_boulder
            is AthleteListItem.CategoryItem -> R.layout.viewholder_category
            is AthleteListItem.HeaderItem -> R.layout.viewholder_header
            else -> 0 // TODO some error view ??
        }
    }
}