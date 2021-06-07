package com.judge.qualimaster.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.judge.core.data.BaseRepository
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.logging.SimpleFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.atan

class BaseRepositoryImpl(private val athleteDao: AthleteDao) : BaseRepository {

    companion object Constants {
        const val ATHLETE_COLLECTION = "athletes"
        const val ATHLETE_NUMBER = "number"
        const val ATHLETE_START_ORDER = "startOrder"
        const val ATHLETE_FIRST_NAME = "firstName"
        const val ATHLETE_LAST_NAME = "lastName"
        const val ATHLETE_LICENCE = "licence"
        const val ATHLETE_CATEGORY = "category"
        const val ATHLETE_COMPETITION_ID = "competitionId"

        const val COMPETITION_COLLECTION = "competitions"
        const val COMP_NAME = "name"
        const val COMP_START_TIME = "startTime"
        const val COMP_LOCATION = "location"
        const val COMP_MIN_PER_BOULDER = "minPerBoulder"
        const val COMP_NUM_OF_ATHLETES_CLIMBING = "numOfAthletesClimbing"
        const val COMP_NUM_OF_ATHLETES_IN_BUFFER = "numOfAthletesInBuffer"
        const val COMP_CATEGORIES = "categories"
    }

    private val db = Firebase.firestore

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override suspend fun sync(): Response {
        return Response.Error("Not yet implemented.")
    }

    override suspend fun getAllCompetitions(): Result<List<Competition>> {
        val competitionsDeferred = CompletableDeferred<Result<List<Competition>>>()

        db.collection(COMPETITION_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val comps = result.map { document ->
                    val categories = dataToCategories(document.id, document.data)
                    dataToCompetition(document.id, categories, document.data)
                }
                competitionsDeferred.complete(Result.Success(comps))
            }
            .addOnFailureListener { exception ->
                competitionsDeferred.complete(Result.Error("Failed loading comps.", exception))
            }

        return competitionsDeferred.await()
    }

    override suspend fun uploadNewCompetition(
        competition: Competition,
        athletes: List<Athlete>
    ): Response {
        val ref = db.collection(COMPETITION_COLLECTION).document()
        val competitionUpload = CompletableDeferred<Response>()
        ref.set(mapOf(
            Pair(COMP_NAME, competition.name),
            Pair(COMP_LOCATION, competition.location),
            Pair(COMP_START_TIME, formatter.format(competition.startTime)),
            Pair(COMP_MIN_PER_BOULDER, competition.minPerBoulder),
            Pair(COMP_NUM_OF_ATHLETES_CLIMBING, competition.numOfAthletesClimbing),
            Pair(COMP_NUM_OF_ATHLETES_IN_BUFFER, competition.numOfAthletesInBuffer),
            Pair(COMP_CATEGORIES, competition.categories.map { it.name })
        ))
            .addOnSuccessListener {
                competitionUpload.complete(Response.Success("Successfully uploaded competition."))
            }
            .addOnFailureListener {
                competitionUpload.complete(Response.Error("Competition upload failed."))
            }

        val competitionId = ref.id
        for (athlete in athletes) {
            ref.collection(ATHLETE_COLLECTION).add(mapOf(
                    Pair(ATHLETE_NUMBER, athlete.number),
                    Pair(ATHLETE_START_ORDER, athlete.startOrder),
                    Pair(ATHLETE_FIRST_NAME, athlete.firstName),
                    Pair(ATHLETE_LAST_NAME, athlete.lastName),
                    Pair(ATHLETE_LICENCE, athlete.licence),
                    Pair(ATHLETE_CATEGORY, athlete.categoryName),
                ))
        }

        return competitionUpload.await()
    }

    override suspend fun insertAthletes(athletes: List<Athlete>): Response {
        return Response.Error("Not yet implemented")
    }

    override suspend fun insertCategories(categories: List<Category>): Response {
        return Response.Error("Not yet implemented")
    }

    override suspend fun insertCompetitions(competitions: List<Competition>): Response {
        return Response.Error("Not yet implemented")
    }

    override suspend fun refreshCompetition(competitionId: String): Response = suspendCoroutine { continuation ->
        db.collection(COMPETITION_COLLECTION)
            .document(competitionId)
            .collection(ATHLETE_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val athletes = result.map { document -> dataToAthletes(competitionId, document.data) }
                val categories = athletesToCategories(competitionId, athletes)

                runBlocking{
                    athleteDao.insertAthletes(athletes.map { it.toEntity() })
                    athleteDao.insertCategories(categories.map { it.toEntity() })
                }
                continuation.resume(Response.Success("Successfully loaded athletes."))
            }
            .addOnFailureListener { exception ->
                continuation.resume(Response.Error("Failed loading athletes.", exception))
            }
    }

    override suspend fun setStartTime(competition: Competition, time: Long): Response = suspendCoroutine{ continuation ->
        db.collection(COMPETITION_COLLECTION)
            .document(competition.competitionId)
            .update(COMP_START_TIME, formatter.format(time))
            .addOnSuccessListener {
                continuation.resume(Response.Success("Successfully set start time."))
            }
            .addOnFailureListener { exception ->
                continuation.resume(Response.Error("Failed to set start time.", exception))
            }
    }

    override suspend fun subscribeCompetition(
        competitionId: String,
        externalScope: CoroutineScope
    ): StateFlow<Result<Competition>> { // = suspendCoroutine { continuation ->
        val competition = MutableStateFlow<Result<Competition>>(Result.Error("Not loaded yet"))
        val ref = db
            .collection(COMPETITION_COLLECTION)
            .document(competitionId)

        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                competition.value = Result.Error(error.message ?: "Listen failed.")
            } else {
                if (snapshot != null && snapshot.exists() && snapshot.data != null) {
                    val categories = dataToCategories(snapshot.id, snapshot.data!!)
                    val comp = dataToCompetition(snapshot.id, categories, snapshot.data!!)
                    competition.value = Result.Success(comp)
                    runBlocking{ athleteDao.insertCompetitions(listOf(comp.toEntity())) }
                } else {
                    Result.Error("No data.")
                }
            }
            // continuation.resume(competition.asStateFlow())
        }

        val subscription = athleteDao.subscribeCompetition(competitionId)
        return subscription
            .map { entity ->
                if (entity == null) {
                    Result.Error("No id $competitionId")
                } else {
                    val categories = athleteDao.getCategories(competitionId)
                        .filterNotNull()
                        .map { it.toCategory() }
                    if (categories.isEmpty()) {
                        Result.Error("No category for competition $competitionId")
                    }
                    Result.Success(entity.toCompetition(categories))
                }
            }
            .stateIn(externalScope.apply { coroutineContext + CoroutineName("Subscribe Competition Base Repository") })
    }

    override suspend fun getAthletesByStartOrder(
        competitionId: String,
        order: List<Int>
    ): Result<List<Athlete>> {
        val athletes = athleteDao.getAthletesByStartOrder(competitionId, order)
        return Result.Success(athletes.filterNotNull().map { it.toAthlete()})
    }

    private fun dataToCompetition(
        competitionId: String,
        categories: List<Category>,
        data: Map<String, Any>
    ): Competition {
        val name = (data[COMP_NAME] as String)
        val location = (data[COMP_LOCATION] as String)
        val startTime = formatter.parse(data[COMP_START_TIME] as String)?.time ?: 0L
        val minPerBoulder = (data[COMP_MIN_PER_BOULDER] as Long)
        val numOfAthletesClimbing = (data[COMP_NUM_OF_ATHLETES_CLIMBING] as Long)
        val numOfAthletesInBuffer = (data[COMP_NUM_OF_ATHLETES_IN_BUFFER] as Long)
        return Competition(
            competitionId, name, location, startTime, minPerBoulder.toInt(),
            numOfAthletesClimbing.toInt(), numOfAthletesInBuffer.toInt(), categories
        )
    }

    private fun dataToCategories(competitionId: String, data: Map<String, Any>): List<Category> {
        return (data[COMP_CATEGORIES] as List<*>).map {
            Category(it as String, 0, competitionId)
        }
    }

    private fun athletesToCategories(competitionId: String, athletes: List<Athlete>): List<Category> {
        val athletesByName = athletes.groupBy { athlete -> athlete.categoryName }
        return athletesByName.keys.map { name ->
            Category(name, athletesByName[name]!!.size, competitionId)
        }
    }

    private fun dataToAthletes(competitionId: String, data: Map<String, Any>): Athlete {
        return Athlete(
            competitionId,
            (data[ATHLETE_NUMBER] as Long).toInt(),
            (data[ATHLETE_START_ORDER] as Long).toInt(),
            (data[ATHLETE_FIRST_NAME] as String),
            (data[ATHLETE_LAST_NAME] as String),
            (data[ATHLETE_LICENCE] as Long).toInt(),
            (data[ATHLETE_CATEGORY] as String)
        )
    }
}