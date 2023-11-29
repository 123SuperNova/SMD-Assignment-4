package com.example.assignment4

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                        // Pass the body in
                        // content parameter
                        content = {
                            Greeting(name = currentUserEmail)
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Hello $name!",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assignment4Theme {
        Greeting("Android")
    }
}