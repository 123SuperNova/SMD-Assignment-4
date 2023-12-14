package com.example.assignment4
import androidx.compose.runtime.Composable
import com.example.assignment4.sampledata.Trip
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import java.util.Date
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AddEditTripPage(trip: Trip, modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        Text(
            text = trip.title,
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
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
                        "\uD83C\uDF3F  Activities",
//                        style = MaterialTheme.typography.h3
                    )
                }
            }
            items(trip.activities){
                ActivityCard(title = it.title, start = it.start, end = it.end, desc = it.booking)
            }
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
//        elevation = 5.dp,
//        backgroundColor = MaterialTheme.colors.surface
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
                    text = title,
//                    style = MaterialTheme.typography.h4,
//                    color = MaterialTheme.colors.onSurface,
                )
                Text(
                    text = desc,
//                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}
