package com.judge.core.presentation.state

sealed class AthleteListState {
    object Loading: AthleteListState()
    object Active: AthleteListState()
    data class Error(val msg: String): AthleteListState()
}