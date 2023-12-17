package com.example.assignment4

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment4.ui.theme.Assignment4Theme
import java.io.Serializable
import kotlin.properties.Delegates

class CVETripActivity : ComponentActivity() {
    private var taskID by Delegates.notNull<Long>()
    private var ActivityID by Delegates.notNull<Long>()
    private val myViewModel: MyViewModel by viewModels()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment4Theme {
                val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

                var activity = this

                taskID = intent.serializable("taskid") ?: 0
                ActivityID = intent.serializable("activityId") ?: 0

                var isFabExpanded by remember { mutableStateOf(false) }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // Scaffold Composable
                    Scaffold(

                        // pass the scaffold state
                        scaffoldState = scaffoldState,

                        // pass the topbar we created
                        topBar = {
                            TopBar2(
                                // When menu is clicked open the
                                // drawer in coroutine scope
                                onMenuClicked = {
                                    this.finish()
                                })
                        },

                        // Pass the body in
                        // content parameter
                        content = {
                            ActivityDetailsScreen(taskID.toString(), ActivityID.toString(), myViewModel)
                        },
                    )
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailsScreen(
    taskID: String,
    activityID: String,
    viewModel: MyViewModel = MyViewModel(),
    modifier: Modifier = Modifier
) {

    var textValue by remember {
        mutableStateOf(TextFieldValue())
    }
    var textValue2 by remember {
        mutableStateOf(TextFieldValue())
    }
    var textValue3 by remember {
        mutableStateOf(TextFieldValue())
    }
    var textValue4 by remember {
        mutableStateOf(TextFieldValue())
    }
    var textValue5 by remember {
        mutableStateOf(TextFieldValue())
    }
    var textValue6 by remember {
        mutableStateOf(TextFieldValue())
    }
    // Content
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp)
        ){
            // Activity Title Text Box
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Activity Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Details, User Notes Text Box
            OutlinedTextField(
                value = textValue2,
                onValueChange = { textValue2 = it },
                label = { Text("Details, User Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Start Date, Time Text Box
            OutlinedTextField(
                value = textValue3,
                onValueChange = { textValue3 = it },
                label = { Text("Start Date, Time") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // End Date, Time Text Box
            OutlinedTextField(
                value = textValue4,
                onValueChange = { textValue4 = it },
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
                    value = textValue5,
                    onValueChange = { textValue5 = it },
                    label = { Text("Booking Info") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                // Weather at Location Text Box
                OutlinedTextField(
                    value = textValue6,
                    onValueChange = { textValue6 = it },
                    label = { Text("Weather at Location") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            // Save Button
            Button(
                onClick = {
                    viewModel.addActivity(taskID, Activity(generateUniqueId(),textValue.text,textValue2.text,
                        textValue3.text,textValue4.text, textValue5.text, textValue6.text))
                    viewModel.setFirestoreData()
                },
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
        //ActivityDetailsScreen()
    }
}