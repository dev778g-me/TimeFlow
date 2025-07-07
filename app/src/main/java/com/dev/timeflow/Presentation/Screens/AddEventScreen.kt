package com.dev.timeflow.Presentation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.rounded.DriveFileRenameOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun AddEventScreen(modifier: Modifier = Modifier) {
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    Scaffold {
        modifier.padding(it)
        Column(
          modifier = modifier
              .fillMaxSize()
              .padding(
                  horizontal = 16.dp
              )
        ){
            OutlinedTextField(
                modifier = modifier.fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = eventName,
                onValueChange = {
                    eventName = it
                },
                placeholder = {
                    Text(
                        text = "Add Event Name"
                    )
                },
                label = {
                    Text(
                        text = "Event Name"

                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.DriveFileRenameOutline,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )

            )
            OutlinedTextField(
                modifier = modifier.fillMaxWidth()
                    .padding(
                        vertical = 4.dp
                    ),
                value = eventDescription,
                onValueChange = {
                    eventDescription = it
                },
                placeholder = {
                    Text(
                        text = "Add Event Description"
                    )
                },
                label = {
                    Text(
                        text = "Event Description"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Description,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )

        }
    }

}