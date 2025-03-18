package com.example.nhltestapp

class Standings(gp: Int, w: Int, l: Int, ot: Int, rw: Int, row: Int){
    var gamesPlayed: Int
    var wins: Int
    var losses: Int
    var overtimeLosses: Int
    var points: Int = 0
    var pointsPercentage: Double = 0.0
    var regulationWins: Int
    var regulationOvertimeWins: Int


    init {
        gamesPlayed = gp
        wins = w
        losses = l
        overtimeLosses = ot

        calculatePoints()

        regulationWins = rw
        regulationOvertimeWins = row
    }

    fun calculatePoints() {
        points = (wins * 2) + overtimeLosses

        val totalPossiblePoints = gamesPlayed * 2
        pointsPercentage = (points / totalPossiblePoints).toDouble()
    }

}
