package com.example.nhltestapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.nhltestapp.R.string.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

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

        getStandings(standingData) { response ->
            standingData = response
        }

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
                       .background(Color.Black)
                       .fillMaxHeight()
               ) {
                   // Rank
                   TableCell(text = rank.toString(), weight = .1f, isTextCenter = false)

                   // Team Logo
                   //TableCell(imageID = teamData.logoID, logoName = teamData.name, weight = .1f)

                   // Team Abbreviation
                   TableCell(text = teamData.abbreviation, weight = .15f, isTextCenter = false)
               }
           }
        }
    }

    @Composable
    private fun StandingsTableStats(teams: List<Pair<Int, Team>>) {
        val columnWeight = .1f

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
                        .background(Color.Black)
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
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStandings(standingData: List<Pair<Int,Team>>?, callback: (List<Pair<Int, Team>>) -> Unit) {
        if (standingData?.size == 0) {
            return
        }

        var teams: List<Pair<Int, Team>> = mutableListOf()

        val currentDate = LocalDate.now()
        val apiURL: String = "https://api-web.nhle.com/v1/standings/${currentDate.toString()}"
        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiURL,
            null,
            Response.Listener<JSONObject>() { response ->
                    teams = createTeams(response)
                    callback(teams)
            },
            { error ->
                error.printStackTrace()
            }
        )
        jsonObjectRequest.retryPolicy =
            DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        queue.add(jsonObjectRequest)
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