package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.judge.core.data.Repository
import java.lang.IllegalArgumentException

class QualificationViewModelFactory(private val repository: Repository, private val competitionId: Long) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrucialInformationViewModel::class.java)) {
            return CrucialInformationViewModel(repository, competitionId) as T
        } else if (modelClass.isAssignableFrom(AthleteLocationViewModel::class.java)){
            return AthleteLocationViewModel(repository, competitionId) as T
        } else if(modelClass.isAssignableFrom(RotationHistoryViewModel::class.java)) {
            return RotationHistoryViewModel(repository, competitionId) as T
        } else {
            throw(IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}"))
        }
    }
}