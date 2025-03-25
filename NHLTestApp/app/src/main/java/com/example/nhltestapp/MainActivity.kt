package com.example.nhltestapp

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.JsonReader
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.nhltestapp.R.string
import com.example.nhltestapp.R.string.*
import com.example.nhltestapp.ui.theme.NHLTestAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONArray
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import kotlin.coroutines.Continuation

class MainActivity : ComponentActivity() {
    //private var standingData: List<Pair<Int, Team>> = mutableListOf()
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalLayoutApi::class, DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainScreen()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun MainScreen() {
        var standingData by remember { mutableStateOf<List<Pair<Int, Team>>?>(null) }

        // Start the data fetching asynchronously
        scope.launch {
            // Wait for the getStandings() to finish
            standingData = getStandings()  // Assuming getStandings is a suspend function

            // After getting the data, show a toast
            Toast.makeText(this@MainActivity, "Data Loaded Successfully!", Toast.LENGTH_SHORT)
                .show()
        }

        // After getting the data, update the UI
        // Standings Header
        Column(
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
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Row {
                // Standings Table
                standingData?.let { StandingsTableShell(it) }
                standingData?.let { StandingsTableStats(it) }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getStandings(): List<Pair<Int, Team>> {
        var teams: List<Pair<Int, Team>> = mutableListOf()

        val currentDate = LocalDate.now()
        val apiURL: String = "https://api-web.nhle.com/v1/standings/${currentDate.toString()}"
        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiURL,
            null,
            { response ->
                teams = createTeams(response)
            },
            { error ->
                error.printStackTrace()
            }
        )
        queue.add(jsonObjectRequest)

        return teams
    }

    private fun createTeams(response: JSONObject): List<Pair<Int,Team>> {
        val teamsList = mutableListOf<Pair<Int, Team>>()

        val teams: JSONArray = response.getJSONArray("standings")
        for (i in 0 until teams.length()) {
            val team: JSONObject = teams.getJSONObject(i)
            val teamData = Team(
                R.drawable.bos_light,
                team.getJSONObject("teamName").getString("default"),
                team.getJSONObject("teamAbbrev").getString("default"),
                Standings(
                    team.getInt("leagueSequence"),
                    team.getInt("gamesPlayed"),
                    team.getInt("wins"),
                    team.getInt("losses"),
                    team.getInt("otLosses"),
                    team.getInt("regulationWins"),
                    team.getInt("regulationPlusOtWins")
                ))
            teamsList += Pair(teamData.standings.rank, teamData)
        }

        return teamsList
    }
}