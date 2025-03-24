package com.example.nhltestapp

import android.content.Context
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import com.example.nhltestapp.R.string
import com.example.nhltestapp.R.string.*
import com.example.nhltestapp.ui.theme.NHLTestAppTheme
import org.json.JSONObject
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val teams = createTeams()

        setContent {
            // Standings Header
            Column (
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize()
            ) {
                Row {
                    Text(
                        text = getString(standings_header),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp).padding(0.dp, 8.dp, 0.dp, 8.dp)
                    )
                }
                Row {
                    // Standings Table
                    StandingsTableShell(teams)
                    StandingsTableStats(teams)
                }
            }
        }
    }

    @Composable
    fun RowScope.TableCell(
        imageID: Int,
        logoName: String,
        weight: Float,
    ) {
        Image(
            painter = painterResource(id = imageID),
            contentDescription = "${logoName} Logo",
            modifier = Modifier
                .border(0.dp, Color.Black)
                .weight(weight)
                .padding(1.dp, 8.dp, 1.dp, 8.dp)
        )
    }

    @Composable
    fun RowScope.TableCell(
        text: String,
        weight: Float,
        isBold: Boolean = false,
        isTextCenter: Boolean = true
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp,
            textAlign = if (isTextCenter) TextAlign.Center else TextAlign.Left,
            modifier = Modifier
                .border(0.dp, Color.Black)
                .weight(weight)
                .padding(1.dp, 8.dp, 1.dp, 8.dp)
        )
    }

    @Composable
    fun StandingsTableShell(teams: List<Pair<Int, Team>>) {
        val column1Weight = .35f
        var columnCounter = 1

        LazyColumn(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(.35f)
        ) {
            // Table Shell Header
            item {
               Row(
                   Modifier.background(Color.Black)
               ) {
                   TableCell(text = getString(league_header), weight = column1Weight, isBold = true, isTextCenter = false)
               }
            }

           // Table Shell Content: Includes rank, image, and abbreviation
           items(teams) {
               val (rank, teamData) = it
               Row(
                   Modifier
                       .fillMaxWidth()
                       .background(if (columnCounter % 2 == 0) Color.Black else Color.DarkGray)
                       .fillMaxHeight()
               ) {
                   // Rank
                   TableCell(text = rank.toString(), weight = .1f, isTextCenter = false)

                   // Team Logo
                   //TableCell(imageID = teamData.logoID, logoName = teamData.name, weight = .1f)

                   // Team Abbreviation
                   TableCell(text = teamData.abbreviation, weight = .15f, isTextCenter = false)

                   columnCounter++
               }
           }
        }
    }

    @Composable
    private fun StandingsTableStats(teams: List<Pair<Int, Team>>) {
        val columnWeight = .1f
        var columnCounter = 1

        LazyColumn(
            modifier = Modifier.fillMaxHeight().fillMaxWidth()
        ) {
            // Table Stats Header
            item {
                Row(
                    Modifier
                        .background(Color.Black)
                ) {
                    TableCell(text = getString(games_played_header), weight = columnWeight, isBold = true)
                    TableCell(text = getString(wins_header), weight = columnWeight, isBold = true)
                    TableCell(text = getString(losses_header), weight = columnWeight, isBold = true)
                    TableCell(text = getString(overtime_losses_header), weight = columnWeight, isBold = true)
                    TableCell(text = getString(points_header), weight = columnWeight + 0.05f, isBold = true)
                    TableCell(text = getString(points_percentage_header), weight = columnWeight, isBold = true)
                    TableCell(text = getString(regulation_wins_header), weight = columnWeight, isBold = true)
                    TableCell(text = getString(regulation_plus_overtime_wins_header), weight = columnWeight + 0.05f, isBold = true)
                }
            }

            // Table Stats Content: Includes gp, w, l, ot, pts, p%, rw, row
            items(teams) {
                val (rank, teamData) = it
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(if (columnCounter % 2 == 0) Color.Black else Color.DarkGray)
                        .fillMaxHeight()
                ) {
                    // Games Played
                    TableCell(text = teamData.standings.wins.toString(), weight = columnWeight)

                    // Wins
                    TableCell(text = teamData.standings.wins.toString(), weight = columnWeight)

                    // Losses
                    TableCell(text = teamData.standings.losses.toString(), weight = columnWeight)

                    // Overtime Losses
                    TableCell(text = teamData.standings.overtimeLosses.toString(), weight = columnWeight)

                    // Points
                    TableCell(text = teamData.standings.points.toString(), weight = columnWeight, isBold = true)

                    // Point Percentage
                    TableCell(text = teamData.standings.pointsPercentage.toString(), weight = columnWeight)

                    // Regulation Wins
                    TableCell(text = teamData.standings.regulationWins.toString(), weight = columnWeight)

                    // Regulation Wins + Overtime Wins
                    TableCell(text = teamData.standings.regulationOvertimeWins.toString(), weight = columnWeight)

                    columnCounter++
                }
            }
        }
    }

    private fun createTeams(): List<Pair<Int, Team>> {
        val teamsList = mutableListOf<Pair<Int, Team>>()

        for (i in 0 until 32) {
            val team = Team(
                R.drawable.bos_light,
                "Boston Bruins",
                "BOS",
                Standings(
                    i + 1,
                    69,
                    30,
                    30,
                    9,
                    23,
                    30
                )
            )

            teamsList += Pair(team.standings.rank, team)
        }

        return teamsList
    }
}