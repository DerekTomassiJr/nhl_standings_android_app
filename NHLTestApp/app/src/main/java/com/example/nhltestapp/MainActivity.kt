package com.example.nhltestapp

import android.content.Context
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nhltestapp.ui.theme.NHLTestAppTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val teams = createTeams()

        setContent {
            NHLTestAppTheme {
                LazyColumn (
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(teams) {
                        FlowRow {
                            StandingsTable(it)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun StandingsTable(team: Team) {
        Box (
            modifier = Modifier.fillMaxSize()
        ) {
            BoxWithConstraints (
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxSize().padding(12.dp).width(maxWidth),
                    shape = CutCornerShape(8.dp, 8.dp, 8.dp, 8.dp)
                ) {
                    Row(
                        Modifier.background(Color.White).horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = team.standings.rank.toString(),
                                modifier = Modifier.padding(10.dp).width(10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Image(
                                painter = painterResource(id = team.logoID),
                                contentDescription = "Hockey Logo",
                                modifier = Modifier.width(50.dp).height(50.dp)
                            )
                        }
                        Column {
                            Text(
                                text = team.name,
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = team.standings.wins.toString(),
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = team.standings.losses.toString(),
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = team.standings.overtimeLosses.toString(),
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = team.standings.points.toString(),
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = team.standings.pointsPercentage.toString(),
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = team.standings.regulationWins.toString(),
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = team.standings.regulationOvertimeWins.toString(),
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createTeams(): List<Team> {
        val teamsList = mutableListOf<Team>()

        for (i in 0 until 32) {
            teamsList += (Team(
                R.drawable.bos_light,
                "Boston Bruins",
                Standings(
                    i + 1,
                    69,
                    30,
                    30,
                    9,
                    23,
                    30
                )
            ))
        }

        return teamsList
    }

    @Composable
    private fun StandingsHeader() {
        Box (
            modifier = Modifier.fillMaxSize()
        ) {
            BoxWithConstraints (
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxSize().padding(12.dp).width(maxWidth),
                    shape = CutCornerShape(8.dp, 8.dp, 8.dp, 8.dp)
                ) {
                    Row(
                        Modifier.background(Color.White).horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "League",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = "Team",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = "W",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = "L",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = "OT",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = "PTS",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = "P%",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = "RW",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column {
                            Text(
                                text = "ROW",
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}