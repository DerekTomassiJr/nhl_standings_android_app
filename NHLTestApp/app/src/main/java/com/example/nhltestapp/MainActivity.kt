package com.example.nhltestapp

import android.content.Context
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import com.example.nhltestapp.R.string.league_header
import com.example.nhltestapp.R.string.standings_header
import com.example.nhltestapp.ui.theme.NHLTestAppTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val teams = createTeams()

        setContent {
            // Standings Header
            Column (
                Modifier.background(Color.Black)
            ) {
                Row {
                    Text(
                        text = getString(standings_header),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp).padding(8.dp)
                    )
                }
                Row {
                    // Standings Table
                    StandingsTableShell(teams)
                }
            }
        }
    }

    @Composable
    fun RowScope.TableCell(
        text: String,
        weight: Float,
        isBold: Boolean
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier
                .border(1.dp, Color.Black)
                .weight(weight)
                .padding(8.dp)
        )
    }

    @Composable
    fun StandingsTableShell(teams: List<Team>) {
        val tableData = (1..32).mapIndexed { index, _ ->
            index + 1 to "Item $index"
        }

        val column1Weight = .3f
        val column2Weight = .7f

        LazyColumn(
            Modifier.fillMaxSize().padding(16.dp)
        ) {
            // Table Header
            item {
               Row(
                   Modifier.background(Color.Black)
               ) {
                   TableCell(text = getString(league_header), weight = column1Weight, isBold = true)
                   TableCell(text = "Column 2", weight = column2Weight, isBold = true)
               }
            }

           items(tableData) {
               val (rank, teamData) = it
               Row(
                   Modifier.fillMaxWidth().background(Color.Black)
               ) {
                   TableCell(text = rank.toString(), weight = column1Weight, isBold = false)
                   TableCell(text = teamData, weight= column2Weight, isBold = false)
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
}