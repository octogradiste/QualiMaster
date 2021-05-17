package com.judge.qualimaster.fakes

import com.judge.qualimaster.data.Athlete

object FakeAthletes {

    val megos = Athlete(1, 4, "Alexander", "Megos", 1993, 1111)
    val mitbo = Athlete(2, 2, "Magnus", "Mitbo", 1988, 1234)
    val ondra = Athlete(3, 1, "Adam", "Ondra", 1993, 2323)
    val sharma = Athlete(4, 3, "Chris", "Sharma", 1981, 4321)
    val woods = Athlete(5, 5, "Daniel", "Woods", 1989, 3333)
    val honnold = Athlete(5, 5, "Alex", "Honnold", 1985, 3443)


    val fakeAthletesForCompetition = listOf(
        Athlete(13,1,"Lehmann","Sascha",1998,8963),
        Athlete(20,2,"Chuat","Dylan",2000,8435),
        Athlete(10,3,"Geisenhoff","Philipp",2000,7767),
        Athlete(21,4,"Clémence","Julien",2001,9635),
        Athlete(12,5,"Grnenfelder","Nino",2001,2445),
        Athlete(7,6,"Vogt","Dimitri",1997,4412),
        Athlete(17,7,"Favre","Nils",1991,5047),
        Athlete(9,8,"Schwob","Sascha",2002,6491),
        Athlete(19,9,"Ometz","Baptiste",1998,5838),
        Athlete(1,10,"Waltensphl","Raoul",1993,6976)
    )

}