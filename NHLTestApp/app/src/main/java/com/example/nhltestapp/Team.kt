package com.example.nhltestapp

import android.media.Image
import androidx.compose.ui.graphics.painter.Painter

data class Team(
    val logoID: Int,
    val name: String,
    val standings: Standings,
)
