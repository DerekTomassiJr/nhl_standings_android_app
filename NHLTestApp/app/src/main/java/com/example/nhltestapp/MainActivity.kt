package com.example.nhltestapp

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nhltestapp.ui.theme.NHLTestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val teams = createTeams()

        setContent {
            NHLTestAppTheme {
                LazyColumn(
                    Modifier.fillMaxSize().padding(16.dp)
                ) {
                    items(teams) {
                        StandingsTable(it)
                    }
                }
            }
        }
    }

    @Composable
    fun StandingsTable(team: Team) {
        Card(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            Row(Modifier.background(Color.Gray)) {
                Image(
                    painter = painterResource(id = team.logoID),
                    contentDescription = "Hockey Logo",
                    modifier = Modifier.width(100.dp).height(100.dp)
                )
                Text(
                    text = team.name,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = team.standings.wins.toString(),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = team.standings.losses.toString(),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = team.standings.overtimeLosses.toString(),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = team.standings.points.toString(),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = team.standings.pointsPercentage.toString(),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = team.standings.regulationWins.toString(),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = team.standings.regulationOvertimeWins.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }

    private fun createTeams(): List<Team> {
      return listOf(
           Team(
               R.drawable.baseline_sports_hockey_24,
               "Boston Bruins",
               Standings(
                   69,
                   30,
                   30,
                   9,
                   23,
                   30
               )
           )
       )
    }
}