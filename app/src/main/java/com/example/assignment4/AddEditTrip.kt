package com.example.assignment4
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment4.sampledata.Activity
import com.example.assignment4.sampledata.Trip
import com.example.assignment4.ui.theme.Assignment4Theme
import java.util.Date

@Composable
fun AddEditTripPage(trip: Trip, modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = trip.title,
            modifier = Modifier,
            fontSize = 24.sp
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 25.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "\uD83C\uDF3F  Activities"
                    )
                }
            }
            items(trip.activities){
                ActivityCard(title = it.title, start = it.start, end = it.end, desc = it.booking)
            }
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
@Composable
private fun ActivityCard(title: String, start: Date, end: Date, desc: String){
    Card(
        modifier = Modifier.padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
//            Image(
//                painter = painterResource(id = image),
//                contentDescription = null,
//                modifier = Modifier.size(130.dp)
//                    .padding(8.dp),
//                contentScale = ContentScale.Fit,
//            )
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = title
                )
                Text(
                    text = desc
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview2() {
    val activities = listOf(Activity("Activity 1", Date(2023,10,10), Date(2023,10,10), "Lahore", "Booking 1" ))
    val trip = Trip(title = "Trip 1", description = "Trip 1 description", activities)
    Assignment4Theme {
        AddEditTripPage(trip)
    }
}
