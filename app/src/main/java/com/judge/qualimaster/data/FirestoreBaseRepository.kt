package com.judge.qualimaster.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.judge.core.data.BaseRepository
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FirestoreBaseRepository() : BaseRepository {

    companion object Constants {
        const val COMPETITION_COLLECTION = "competitions"
        const val COMP_NAME = "name"
        const val COMP_LOCATION = "location"
        const val COMP_MIN_PER_BOULDER = "minPerBoulder"
        const val COMP_NUM_OF_ATHLETES_CLIMBING = "numOfAthletesClimbing"
        const val COMP_NUM_OF_ATHLETES_IN_BUFFER = "numOfAthletesInBuffer"
        const val COMP_CATEGORIES = "categories"
    }

    private val db = Firebase.firestore

    override suspend fun sync(): Response {
        return Response.Error("Not yet implemented")
    }

    override suspend fun getAllCompetitions(): Result<List<Competition>> {
        val competitionsDeferred = CompletableDeferred<Result<List<Competition>>>()

        db.collection(COMPETITION_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val comps = result.map { document ->
                    val competitionId = 1L // document.id
                    val name = document.data[COMP_NAME] as String
                    val location = document.data[COMP_LOCATION] as String
                    val startTime = 1622698602644L // document.data[COMP_START_TIME] as String
                    val minPerBoulder = (document.data[COMP_MIN_PER_BOULDER] as Long)
                    val numOfAthletesClimbing = (document.data[COMP_NUM_OF_ATHLETES_CLIMBING] as Long)
                    val numOfAthletesInBuffer = (document.data[COMP_NUM_OF_ATHLETES_IN_BUFFER] as Long)
                    val categories = (document.data[COMP_CATEGORIES] as List<*>).mapIndexed { index, any ->
                        Category(index.toLong(), any as String, 1, competitionId)
                    }
                    Competition(
                        competitionId, name, location, startTime, minPerBoulder.toInt(),
                        numOfAthletesClimbing.toInt(), numOfAthletesInBuffer.toInt(), categories
                    )
                }
                competitionsDeferred.complete(Result.Success(comps))
            }
            .addOnFailureListener {
                competitionsDeferred.complete(Result.Error(it.message ?: "Error loading competitions", it))
            }

        return competitionsDeferred.await()
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

    override suspend fun refreshCompetition(competitionId: Long): Response {
        return Response.Error("Not yet implemented")
    }

    override suspend fun setStartTime(competition: Competition, time: Long): Response {
        return Response.Error("Not yet implemented")
    }

    override suspend fun subscribeCompetition(
        competitionId: Long,
        externalScope: CoroutineScope
    ): StateFlow<Result<Competition>> {
        return MutableStateFlow(Result.Error("Not yet implemented")).asStateFlow()
    }

    override suspend fun getAthletesByStartOrder(
        competitionId: Long,
        order: List<Int>
    ): Result<List<Athlete>> {
        return Result.Error("Not yet implemented")
    }
}