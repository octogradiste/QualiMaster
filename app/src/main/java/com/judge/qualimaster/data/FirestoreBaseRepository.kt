package com.judge.qualimaster.data
/*
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
                val comps = result.map { document -> dataToCompetition(document.id, document.data)}
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

    override suspend fun refreshCompetition(competitionId: String): Response {
        return Response.Error("Not yet implemented")
    }

    override suspend fun setStartTime(competition: Competition, time: Long): Response {
        return Response.Error("Not yet implemented")
    }

    override suspend fun subscribeCompetition(
        competitionId: String,
        externalScope: CoroutineScope
    ): StateFlow<Result<Competition>> {
        val competition = MutableStateFlow<Result<Competition>>(Result.Error("Not loaded yet"))
        return suspendCoroutine { continuation ->
            val ref = db.collection(COMPETITION_COLLECTION).document(competitionId)
            ref.addSnapshotListener { value, error ->
                if (error != null) {
                    competition.value = Result.Error(error.message ?: "Listen failed.")
                } else {
                    if (value != null && value.exists() && value.data != null) {
                        competition.value = Result.Success(dataToCompetition(value.id, value.data!!))
                    } else {
                        Result.Error("Value is null.")
                    }
                }
                continuation.resume(competition.asStateFlow())
            }
        }
    }

    override suspend fun getAthletesByStartOrder(
        competitionId: String,
        order: List<Int>
    ): Result<List<Athlete>> {
        return Result.Error("Not yet implemented")
    }

    private fun dataToCompetition(competitionId: String, data: Map<String, Any>): Competition {
        val name = data[COMP_NAME] as String
        val location = data[COMP_LOCATION] as String
        val startTime = 1622698602644L // TODO : document.data[COMP_START_TIME] as String
        val minPerBoulder = (data[COMP_MIN_PER_BOULDER] as Long)
        val numOfAthletesClimbing = (data[COMP_NUM_OF_ATHLETES_CLIMBING] as Long)
        val numOfAthletesInBuffer = (data[COMP_NUM_OF_ATHLETES_IN_BUFFER] as Long)
        val categories = (data[COMP_CATEGORIES] as List<*>).mapIndexed { index, any ->
            Category(any as String, 1, competitionId)
        }
        return Competition(
            competitionId, name, location, startTime, minPerBoulder.toInt(),
            numOfAthletesClimbing.toInt(), numOfAthletesInBuffer.toInt(), categories
        )
    }
}
*/