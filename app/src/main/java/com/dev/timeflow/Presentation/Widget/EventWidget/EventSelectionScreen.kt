package com.dev.timeflow.Presentation.Widget.EventWidget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.text.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.dev.timeflow.Presentation.Viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventSelectionScreen(modifier: Modifier = Modifier) {

    val eventViewModel : EventViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        eventViewModel.getAllEvents()
    }
    val allEvents by eventViewModel.allEvents.collectAsState(emptyList())
    var selectedItem by remember { mutableStateOf(0) }
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text = "Select a Event "
                    )
                },
                actions = {
                    Button(
                        modifier = modifier.padding(end = 4.dp),
                        onClick = {}
                    ) {
                        androidx.compose.material3.Text(
                            text = "Save"
                        )
                    }
                }

            )
        }
    ){
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = modifier.fillMaxSize()
                .padding(it)
        ) {
           itemsIndexed(allEvents){
               index, event ->
               ListItem(
                   headlineContent = {
                       androidx.compose.material3.Text(
                           text = event.title
                       )
                   },
                   leadingContent = {
                       Checkbox(
                           checked = selectedItem == index,
                           onCheckedChange = {
                               selectedItem = index
                           }
                       )
                   },
                   supportingContent = {
                       androidx.compose.material3.Text(
                           text = event.description
                       )
                   }
               )
           }
        }
    }
}