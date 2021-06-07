package com.judge.core.io

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.util.TimeFormatter

class QualificationFileParser() {

    fun parse(lineReader: LineReader, formatter: TimeFormatter): Result<Pair<Competition, List<Athlete>>> {
        val competitionMap = mutableMapOf<String, String>()
        val athletes = mutableListOf<Athlete>()
        var line = lineReader.read()
        while (!(line == null || line == ";;;;;")){
            val split = line.split(";", limit = 6)
            competitionMap[split[0]] = split[1]
            line = lineReader.read()
        }
        val competitionKeys = setOf(
            "name", "location", "startTime", "minPerBoulder",
            "numOfAthletesClimbing", "numOfAthletesInBuffer"
        )
        if (!competitionMap.keys.containsAll(competitionKeys)) {
            return Result.Error("Missing ${competitionKeys.minus(competitionMap.keys)}")
        }
        val startTime = formatter.parse(competitionMap["startTime"]!!)
            ?: return Result.Error("Wrong startTime format. ${formatter.formatString}")
        val competition = Competition(
            "",
            competitionMap["name"]!!,
            competitionMap["location"]!!,
            startTime,
            competitionMap["minPerBoulder"]!!.toInt(),
            competitionMap["numOfAthletesClimbing"]!!.toInt(),
            competitionMap["numOfAthletesInBuffer"]!!.toInt(),
            emptyList()
        )
        line = lineReader.read()
        if (line == null || line != ";;;;;") {
            return Result.Error("Keep two empty row between the competition and athletes. $line")
        }
        val athleteKeys = setOf(
            "number", "startOrder", "firstName", "lastName", "licence", "categoryName"
        )
        line = lineReader.read()
        if (line == null) {
            return Result.Error("Missing athlete header.")
        }
        val header = line.split(";", limit = 6)
        if (!header.containsAll(athleteKeys)) {
            return Result.Error("Missing ${athleteKeys.minus(header)}")
        }
        line = lineReader.read()
        while (line != null) {
            val split = line.split(";", limit = 6)
            athletes.add(Athlete(
                "",
                split[header.indexOf("number")].toInt(),
                split[header.indexOf("startOrder")].toInt(),
                split[header.indexOf("firstName")],
                split[header.indexOf("lastName")],
                split[header.indexOf("licence")].toInt(),
                split[header.indexOf("categoryName")]
            ))
            line = lineReader.read()
        }
        val athletesByName = athletes.groupBy { it.categoryName }
        val categories = athletesByName.keys.map { name ->
            Category(name, athletesByName[name]!!.size, "")
        }
        return Result.Success(Pair(competition.copy(categories = categories), athletes))
    }

}