package com.example.assignment4

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.assignment4.ui.theme.Assignment4Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter",
        "UnusedMaterialScaffoldPaddingParameter"
    )
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        // Initialize Firebase Auth
        auth = Firebase.auth
        setContent {
            Assignment4Theme {
                val currentUserEmail = auth.currentUser?.email.toString()
                // create a scaffold state, set it to close by default
                val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
                val coroutineScope = rememberCoroutineScope()

                var activity = this

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
                            TopBar(
                                // When menu is clicked open the
                                // drawer in coroutine scope
                                onMenuClicked = {
                                    coroutineScope.launch {
                                        // to close use -> scaffoldState.drawerState.close()
                                        scaffoldState.drawerState.open()
                                    }
                                })
                        },
                        // pass the drawer
                        drawerContent = {
                            Drawer(activity, currentUserEmail, scaffoldState)
                        },



                    floatingActionButton = {
                        // Create a floating action button in
                        // floatingActionButton parameter of scaffold
                        FloatingActionButton(
                            backgroundColor = Color.Blue,
                            onClick = {
                                // Check if a Snackbar is currently displayed
                                if (scaffoldState.snackbarHostState.currentSnackbarData != null) {
                                    // Close the Snackbar if it's open
                                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                } else {
                                    isFabExpanded =!isFabExpanded
                                    coroutineScope.launch {
                                        when (scaffoldState.snackbarHostState.showSnackbar(
                                            // Message In the snackbar
                                            message = "New Trip Plan",
                                            actionLabel = "Add"
                                        )) {
                                            SnackbarResult.Dismissed -> {
                                                isFabExpanded =!isFabExpanded
                                                // do something when
                                                // snack bar is dismissed
                                            }

                                            SnackbarResult.ActionPerformed -> {
                                                // when it appears
                                                isFabExpanded =!isFabExpanded
                                                Toast.makeText(
                                                    activity,
                                                    "Creating New Trip Plan",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            else -> {}
                                        }
                                    }
                                }
                            }
                        ) {
                            // Show different content based on the expanded state
                            if (isFabExpanded) {
                                // Content when expanded
                                Text(text = "-", fontSize = 40.sp, color = Color.White)
                            } else {
                                // Content when collapsed
                                Text(text = "+", fontSize = 30.sp, color = Color.White)
                            }
                        }
                    },

                    // Pass the body in
                                // content parameter
                        content = {
                            Body(name = currentUserEmail)
                        },
                    )
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            //reload()
            // Create an explicit intent to start SecondActivity
            val intent = Intent(this, LoginActivity::class.java)

            // Add any extra data you want to pass to SecondActivity
            intent.putExtra("key", "Hello from MainActivity!")

            // Start SecondActivity
            ContextCompat.startActivity(this, intent, null)
        }
    }
}

@Composable
fun Drawer(mainActivity: MainActivity, email: String, scaffoldState: ScaffoldState) {
    // Column Composable
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Repeat is a loop which
        // takes count as argument
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(email,
                modifier = Modifier
                    .padding(8.dp))
            IconButton(onClick = {
                coroutineScope.launch {
                    // to close use -> scaffoldState.drawerState.close()
                    scaffoldState.drawerState.close()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Close Menu"
                )
            }
        }
        Button(
            onClick = {
                Firebase.auth.signOut()
                val intent = Intent(mainActivity, LoginActivity::class.java)

                // Add any extra data you want to pass to SecondActivity
                intent.putExtra("key", "Hello from MainActivity!")

                // Start SecondActivity
                ContextCompat.startActivity(mainActivity, intent, null)
                Toast.makeText(
                    mainActivity,
                    "Successfully Logged Out.",
                    Toast.LENGTH_SHORT,
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Log Out"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onMenuClicked: () -> Unit) {

    // TopAppBar Composable
    CenterAlignedTopAppBar(
        // Provide Title
        title = {
            Text(text = "QALARIA")

        },
        // Provide the navigation Icon (Icon on the left to toggle drawer)
        navigationIcon = {
            IconButton(onClick = { onMenuClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        }

    )
}

@Composable
fun Body(name: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Trip Plans",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color(25, 140, 255),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        var array = generateDummyTripPlansList(3)
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(Color.LightGray),
            content = {
            items(array){
                TripPlans(plan = it)
            }
        })
        Text(
            text = "Suggestions",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color(25, 140, 255),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        var array2 = generateDummySuggestionsList(3)
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(Color.LightGray),
            content = {
                items(array2){
                    TripPlans(plan = it)
                }
            })
    }
}

@Composable
fun TripPlans(plan: Plans){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(Color(25, 140, 255))
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(
                text = plan.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(end = 6.dp))
            Text(
                text = plan.datetimeV,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                   textAlign = TextAlign.End
                ),
                color = Color.White
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(
                text = plan.description,
                fontSize = 16.sp,
                color = Color.LightGray
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Go to Details",
                    tint = Color.White
                )
            }
        }
    }
}

data class Plans(val name: String, val datetimeV: String, val description: String)

fun generateDummyTripPlansList(size: Int): List<Plans> {
    val dummyList = mutableListOf<Plans>()

    for (i in 1..size) {
        dummyList.add(
            Plans(
                name = "Trip $i",
                datetimeV = "2023-01-01 12:00 PM", // Replace with your desired format and data
                description = "Description for Trip $i"
            )
        )
    }

    return dummyList
}

fun generateDummySuggestionsList(size: Int): List<Plans> {
    val dummyList = mutableListOf<Plans>()

    for (i in 1..size) {
        dummyList.add(
            Plans(
                name = "Suggestion $i",
                datetimeV = "2023-01-01 12:00 PM", // Replace with your desired format and data
                description = "Description for Suggestion $i"
            )
        )
    }

    return dummyList
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    Assignment4Theme {
        Body("Android")
    }
}