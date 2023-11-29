package com.example.assignment4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment4.ui.theme.Assignment4Theme

class CVETripActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment4Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ActivityDetailsScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailsScreen(modifier: Modifier = Modifier) {

    // Content
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Qlaria", style = MaterialTheme.typography.bodyLarge) },
            navigationIcon = {
                IconButton(onClick = { /* Handle back button click */ }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
            modifier = Modifier
                .border(1.dp, Color.Black)
        )
        Column (
            modifier = Modifier
                .padding(16.dp)
        ){
            // Activity Title Text Box
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle value change */ },
                label = { Text("Activity Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Details, User Notes Text Box
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle value change */ },
                label = { Text("Details, User Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Start Date, Time Text Box
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle value change */ },
                label = { Text("Start Date, Time") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // End Date, Time Text Box
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle value change */ },
                label = { Text("End Date, Time") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Column with two text boxes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Booking Info Text Box
                OutlinedTextField(
                    value = "",
                    onValueChange = { /* Handle value change */ },
                    label = { Text("Booking Info") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                // Weather at Location Text Box
                OutlinedTextField(
                    value = "",
                    onValueChange = { /* Handle value change */ },
                    label = { Text("Weather at Location") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            // Save Button
            Button(
                onClick = { /* Handle save button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    Assignment4Theme {
        ActivityDetailsScreen()
    }
}