package com.dev.timeflow.View.Screens.onBoarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.glance.text.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    androidx.compose.material3.Text(text = "Timeflow", fontWeight = FontWeight.Bold)
                }
            )
        }
    ) {
        Column(
            modifier = modifier.fillMaxSize()
                .padding(it)
        ) { }
    }
}