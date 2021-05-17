package com.judge.qualimaster.ui.viewmodels

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.judge.qualimaster.adapter.AthleteQualificationListItem
//import com.judge.qualimaster.adapter.BoulderQualificationListItem
//import com.judge.qualimaster.adapter.HeaderQualificationListItem
//import com.judge.qualimaster.adapter.QualificationListItem
//import com.judge.qualimaster.data.Athlete
//import com.judge.qualimaster.data.Boulder
//import com.judge.qualimaster.data.Repository
//import com.judge.qualimaster.util.Constants.MINUTE_PER_BOULDER
//import com.judge.qualimaster.util.Constants.NUMBER_OF_BOULDERS
//import com.judge.qualimaster.util.Constants.PEOPLE_IN_BUFFER
//import com.judge.qualimaster.util.IntervalTimer
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//import java.util.*
//import javax.inject.Inject
//
//@HiltViewModel
//class QualificationViewModel @Inject constructor(
//    private val repository: Repository,
//    intervalTimer: IntervalTimer
//) : ViewModel() {
//
//    private val athletes = repository.getAllAthletes()
//
//    private val _startTime = MutableLiveData<Long>()
//    private val _currentTime = MutableLiveData<Long>()
//    private val _currentRotation = MutableLiveData<Int>()
//    private val _crucialInformation = MutableLiveData<List<QualificationListItem>>()
//    private val _rotationHistory = MutableLiveData<List<QualificationListItem>>()
//    private val _athleteLocation = MutableLiveData<List<QualificationListItem>>()
//
//    val startTime: LiveData<Long> = _startTime
//    val currentTime: LiveData<Long> = _currentTime
//    val currentRotation: LiveData<Int> = _currentRotation
//    val crucialInformation: LiveData<List<QualificationListItem>> = _crucialInformation
//    val rotationHistory: LiveData<List<QualificationListItem>> = _rotationHistory
//    val athleteLocation: LiveData<List<QualificationListItem>> = _athleteLocation
//
//    init {
//        viewModelScope.launch {
//            athletes.collect {
//                onAthletesChanged()
//            }
//        }
//
//        intervalTimer.setIntervalTimerListener { currentTimeInMillis ->
//            _currentTime.postValue(currentTimeInMillis)
//
//            val rotation = getRotation(currentTimeInMillis)
//            if (_currentRotation.value != rotation) {
//                onRotationChanged(rotation)
//            }
//        }
//
//        val calendar = Calendar.getInstance().apply{
//            set(Calendar.HOUR_OF_DAY, 9)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }
//
//        setStartTime(calendar.timeInMillis)
//    }
//
//    fun setStartTime(time: Long) {
//        _startTime.value = time
//    }
//
//    private fun getRotation(time: Long) : Int {
//        var rotation = 0
//        val difference = time - (_startTime.value ?: time)
//        if (difference > 0) {
//            val temp = (difference / (1000 * 60 * MINUTE_PER_BOULDER) + 1).toInt()
//            val totalRotations = repository.countAthletes() + 2 * NUMBER_OF_BOULDERS
//            if (temp <= totalRotations) {
//                rotation = temp
//            }
//        }
//
//        return rotation
//    }
//
//    private fun onAthletesChanged() {
//        _rotationHistory.value = computeRotationHistoryList()
//    }
//
//    private fun onRotationChanged(rotation: Int) {
//        if ((rotation == 0) and (_currentRotation.value != 0)) {
//            _currentRotation.postValue(0)
//            _crucialInformation.postValue(emptyList())
//            _athleteLocation.postValue(emptyList())
//        } else {
//            _currentRotation.postValue(rotation)
//            _crucialInformation.postValue(computeCrucialInformationList(rotation))
//            _athleteLocation.postValue(computeAthleteLocationList(rotation))
//        }
//    }
//
//    private fun computeCrucialInformationList(rotation: Int): List<QualificationListItem> {
//        val crucialInformationList = mutableListOf<QualificationListItem>()
//
//        crucialInformationList.addAll(getBoulderQualificationList(rotation, "CURRENT"))
//        crucialInformationList.addAll(getBoulderQualificationList(rotation + 1, "NEXT"))
//
//        getAthleteInTransit(rotation)?.let { athlete ->
//            crucialInformationList.add(HeaderQualificationListItem("TRANSIT"))
//            crucialInformationList.add(BoulderQualificationListItem(Boulder("", athlete)))
//        }
//
//        return crucialInformationList.toList()
//    }
//
//    private fun computeRotationHistoryList() : List<QualificationListItem> {
//        val rotationHistoryList = mutableListOf<QualificationListItem>()
//
//        for (rotation in 1..(repository.countAthletes() + 2 * NUMBER_OF_BOULDERS)) {
//            rotationHistoryList.addAll(getBoulderQualificationList(rotation, "ROTATION $rotation"))
//        }
//
//        return rotationHistoryList.toList()
//    }
//
//    private fun computeAthleteLocationList(rotation: Int) : List<QualificationListItem> {
//        val athleteLocationList = mutableListOf<QualificationListItem>()
//
//        val isolation = getAthletesInIsolation(rotation)
//        if (isolation.isNotEmpty()) {
//            athleteLocationList.add(HeaderQualificationListItem("ISOLATION"))
//            athleteLocationList.addAll(isolation.map { athlete -> AthleteQualificationListItem(athlete) })
//        }
//
//        val transit = getAthleteInTransit(rotation)
//        transit?.let { athlete ->
//            athleteLocationList.add(HeaderQualificationListItem("TRANSIT"))
//            athleteLocationList.add(AthleteQualificationListItem(athlete))
//        }
//
//        val buffer = getAthletesInBuffer(rotation)
//        val intermediate = getAthletesInIntermediateIsolation(rotation)
//        if (buffer.isNotEmpty() or intermediate.isNotEmpty()) {
//            athleteLocationList.add(HeaderQualificationListItem("INTERMEDIATE ISOLATION"))
//            athleteLocationList.addAll(buffer.map { athlete -> AthleteQualificationListItem(athlete) })
//            athleteLocationList.addAll(intermediate.map { athlete -> AthleteQualificationListItem(athlete) })
//        }
//
//        val onTheWall = getAthletesOnTheWall(rotation)
//        if (onTheWall.isNotEmpty()) {
//            athleteLocationList.add(HeaderQualificationListItem("ON THE WALL"))
//            athleteLocationList.addAll(onTheWall.map { athlete -> AthleteQualificationListItem(athlete) })
//        }
//
//        val finishedCompeting = getAthletesFinishedCompeting(rotation)
//        if (finishedCompeting.isNotEmpty()) {
//            athleteLocationList.add(HeaderQualificationListItem("FINISHED COMPETING"))
//            athleteLocationList.addAll(finishedCompeting.map { athlete -> AthleteQualificationListItem(athlete) })
//        }
//
//        return athleteLocationList.toList()
//    }
//
//    /**
//     * Returns a list of [QualificationListItem] containing all [Boulder] of a given [rotation].
//     * If [title] is not an empty string a [HeaderQualificationListItem] with the [title] is added
//     * as the first element of the list.
//     */
//    private fun getBoulderQualificationList(rotation: Int, title: String = "") : List<QualificationListItem> {
//        val boulders = mutableListOf<QualificationListItem>()
//
//        val athletes = getAthletesOnTheWall(rotation)
//        if (athletes.isNotEmpty()) {
//            if (title.isNotEmpty()) boulders.add(HeaderQualificationListItem(title))
//            boulders.addAll(athletes.mapIndexed { i, athlete ->
//                BoulderQualificationListItem(Boulder("B${i+1}", athlete))
//            })
//        }
//
//        return boulders.toList()
//    }
//
//    private fun getAthletesInIsolation(rotation: Int) : List<Athlete> {
//        val startNumbers = mutableListOf<Int>()
//        for (i in (PEOPLE_IN_BUFFER + 2)..(repository.countAthletes())) startNumbers.add(rotation + i)
//        return repository.getAthletesByStartNumber(startNumbers)
//    }
//
//    private fun getAthleteInTransit(rotation: Int) : Athlete? {
//        val startNumber = rotation + PEOPLE_IN_BUFFER + 1
//        if (startNumber > repository.countAthletes()) return null
//        return repository.getAthleteByStartNumber(startNumber)
//    }
//
//    private fun getAthletesInBuffer(rotation: Int) : List<Athlete> {
//        val startNumbers = mutableListOf<Int>()
//        for (i in 1..PEOPLE_IN_BUFFER) startNumbers.add(rotation + i)
//        return repository.getAthletesByStartNumber(startNumbers)
//    }
//
//    private fun getAthletesInIntermediateIsolation(rotation: Int) : List<Athlete> {
//        val startNumbers = mutableListOf<Int>()
//        for (i in 0 until (NUMBER_OF_BOULDERS - 1)) startNumbers.add(rotation - 2*i - 1)
//        return repository.getAthletesByStartNumber(startNumbers)
//    }
//
//    /**
//     * Returns all [Athlete] on the wall during a given [rotation]
//     * ordered from first to last boulder.
//     */
//    private fun getAthletesOnTheWall(rotation: Int) : List<Athlete> {
//        val startNumbers = mutableListOf<Int>()
//        for (i in 0 until NUMBER_OF_BOULDERS) startNumbers.add(rotation - 2*i)
//        return repository.getAthletesByStartNumber(startNumbers, true)
//    }
//
//    private fun getAthletesFinishedCompeting(rotation: Int) : List<Athlete> {
//        val startNumbers = mutableListOf<Int>()
//        for (i in 1..(rotation - 2*NUMBER_OF_BOULDERS + 1)) startNumbers.add(i)
//        return repository.getAthletesByStartNumber(startNumbers)
//    }
//
//}