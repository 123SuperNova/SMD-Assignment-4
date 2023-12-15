package com.example.assignment4
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.assignment4.ui.theme.Assignment4Theme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.io.Serializable
import kotlin.properties.Delegates

class AddEditTripActivity : ComponentActivity() {
    private var taskID by Delegates.notNull<Long>()
    private val myViewModel: MyViewModel by viewModels()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskID = intent.serializable("taskid") ?: 0
        Log.d("taskId", taskID.toString())

        setContent {
            Assignment4Theme {
                val currentUserEmail = Firebase.auth.currentUser?.email.toString()
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
                            TopBar2(
                                // When menu is clicked open the
                                // drawer in coroutine scope
                                onMenuClicked = {
                                    this.finish()
                                })
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
                                                message = "New Activity",
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
                                                        "Creating New Activity",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    // Create an explicit intent to start SecondActivity
                                                    val intent = Intent(activity, CVETripActivity::class.java)

                                                    // Add any extra data you want to pass to SecondActivity
                                                    intent.putExtra("activityId", -1)

                                                    // Start SecondActivity
                                                    ContextCompat.startActivity(activity, intent, null)
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
                            AddEditTripPage(activity,taskID.toString(), myViewModel)
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
fun TopBar2(onMenuClicked: () -> Unit) {

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
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Localized description"
                )
            }
        }

    )
}

@Composable
fun AddEditTripPage(addEditTripActivity: AddEditTripActivity,tripId: String, viewModel: MyViewModel = MyViewModel(), modifier: Modifier = Modifier){
    var trip = viewModel.getPlanById(tripId)
    var textValue by remember {
        mutableStateOf(TextFieldValue())
    }
    var textValue2 by remember {
        mutableStateOf(TextFieldValue())
    }
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(
            value = textValue,
            onValueChange = { textValue = it },
            label = { Text("Trip Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = textValue2,
            onValueChange = { textValue2 = it },
            label = { Text("Trip description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ){
            if (viewModel.getPlanById(tripId)?.activities?.isEmpty() == true){
                Text(text = "No Activities")
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(Color.LightGray),
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
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
                    viewModel.getPlanById(tripId)?.let {
                        items(it.activities) {
                            ActivityCard(it)
                        }
                    }
                }
            }
            // Save Button
            Button(
                onClick = {
                    Log.d("textValue", textValue.text)
                    viewModel.addTrip(Trip(generateUniqueId(),textValue.text,textValue2.text))
                    viewModel.setFirestoreData()
                    addEditTripActivity.finish()
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
@Composable
private fun ActivityCard(activity: Activity){
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
                text = activity.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(end = 6.dp))
            Text(
                text = activity.start.toString(),
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
                text = activity.description,
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

@Preview(showBackground = true)
@Composable
fun AppPreview2() {
    Assignment4Theme {
        //AddEditTripPage()
    }
}
