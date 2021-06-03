package com.judge.qualimaster.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.judge.core.domain.model.Competition
import com.judge.qualimaster.R

class CompetitionsRecyclerviewAdapter(
    var competitions: List<Competition>,
    private val listener: (competitionId: String) -> Unit
) : RecyclerView.Adapter<CompetitionsRecyclerviewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_competition, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tvCompetitionTitle = holder.itemView.findViewById<TextView>(R.id.tvCompetitionTitle)
        val tvLocationName = holder.itemView.findViewById<TextView>(R.id.tvLocationName)
        tvCompetitionTitle.text = competitions[position].name
        tvLocationName.text = competitions[position].location
        holder.itemView.setOnClickListener {
            listener(competitions[position].competitionId)
        }
    }

    override fun getItemCount() = competitions.size

}

