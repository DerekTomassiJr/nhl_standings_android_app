package com.example.nhltestapp

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round

class Standings(rk: Int, gp: Int, w: Int, l: Int, ot: Int, rw: Int, row: Int){
    var rank: Int
    var gamesPlayed: Int
    var wins: Int
    var losses: Int
    var overtimeLosses: Int
    var points: Int = 0
    var pointsPercentage: Double = 0.0
    var regulationWins: Int
    var regulationOvertimeWins: Int


    init {
        rank = rk
        wins = w
        losses = l
        overtimeLosses = ot

        gamesPlayed = wins + losses + overtimeLosses

        calculatePoints()

        regulationWins = rw
        regulationOvertimeWins = row
    }

    private fun calculatePoints() {
        points = (wins * 2) + overtimeLosses

        val totalPossiblePoints = gamesPlayed * 2
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        val percentage = (points.toDouble() / totalPossiblePoints.toDouble())
        pointsPercentage = df.format(percentage).toDouble()
    }

}
