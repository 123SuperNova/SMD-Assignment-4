package com.example.assignment4

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.assignment4.ui.theme.Assignment4Theme
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Arrays
import java.util.Locale
import androidx.fragment.app.setFragmentResult

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

//    val apiKey = BuildConfig.PLACES_API_KEY
//    if (apiKey.isEmpty()) {
//        Toast.makeText(this, getString(R.string.error_api_key), Toast.LENGTH_LONG).show()
//        return
//    }
//
//    // Setup Places Client
//    if (!Places.isInitialized()) {
//        Places.initialize(applicationContext, apiKey)
//    }
//    Places.initialize(this, apiKey)

//    // Initialize the SDK
//    Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)
//
//    // Create a new PlacesClient instance
//    val placesClient = Places.createClient(this)

// Set the fields to specify which types of place data to
// return after the user has made a selection.



    private val myViewModel: MyViewModel by viewModels()
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

                // Initialize the AutocompleteSupportFragment.
                val autocompleteFragment =
                    supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                            as AutocompleteSupportFragment

                // Specify the types of place data to return.
                autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

                // Set up a PlaceSelectionListener to handle the response.
                autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                    override fun onPlaceSelected(place: Place) {
                        // TODO: Get info about the selected place.
                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    }

                    override fun onError(status: Status) {
                        // TODO: Handle the error.
                        Log.i(TAG, "An error occurred: $status")
                    }
                })

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val fields = listOf(Place.Field.ID, Place.Field.NAME)
//
//                    // Start the autocomplete intent.
//                    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                        .build(this)
//                    startAutocomplete.launch(intent)


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
                                                // Create an explicit intent to start SecondActivity
                                                val intent = Intent(activity, AddEditTripActivity::class.java)

                                                // Add any extra data you want to pass to SecondActivity
                                                intent.putExtra("taskId", "-1")

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
                            Body(mainActivity = this, viewModel = myViewModel)                        },
                    )
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (auth.currentUser == null) {
            //reload()
            // Create an explicit intent to start SecondActivity
            val intent = Intent(this, LoginActivity::class.java)

            // Add any extra data you want to pass to SecondActivity
            //intent.putExtra("key", "Hello from MainActivity!")

            // Start SecondActivity
            ContextCompat.startActivity(this, intent, null)
        }
    }
    public override fun onDestroy() {
        super.onDestroy()
    }
//    private val startAutocomplete =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val intent = result.data
//                if (intent != null) {
//                    val place = Autocomplete.getPlaceFromIntent(intent)
//                    Log.i(
//                        TAG, "Place: ${place.name}, ${place.id}"
//                    )
//                }
//            } else if (result.resultCode == Activity.RESULT_CANCELED) {
//                // The user canceled the operation.
//                Log.i(TAG, "User canceled autocomplete")
//            }
//        }

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
                mainActivity.finish()
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
fun Body(
    mainActivity: MainActivity,
    viewModel: MyViewModel, // Pass the view model instance
    modifier: Modifier = Modifier
) {
    // Observe the LiveData in a Composable
    val userData by viewModel.userData.observeAsState()

    // Trigger data retrieval when the composable is first created
    DisposableEffect(Unit) {
        viewModel.getFirestoreData()
        onDispose { /* Cleanup, if needed */ }
    }
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

        if (userData?.plans?.isEmpty() == true){
            Text(text="No Trip Plans")
        }
        else {
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
                    userData?.let {
                        items(it.plans) { trip->
                            TripPlans(mainActivity, plan = trip)
                        }
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
        }
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
                    TripPlans(mainActivity,plan = it)
                }
            })
    }
}

@Composable
fun TripPlans(mainActivity: MainActivity,plan: Trip){
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
                text = plan.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(end = 6.dp))
            Text(
                text = "",
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
            IconButton(onClick = {
                // Create an explicit intent to start SecondActivity
                val intent = Intent(mainActivity, AddEditTripActivity::class.java)

                // Add any extra data you want to pass to SecondActivity
                intent.putExtra("taskId", plan.id.toString())

                // Start SecondActivity
                ContextCompat.startActivity(mainActivity, intent, null)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Go to Details",
                    tint = Color.White
                )
            }
        }
    }
}


fun generateDummySuggestionsList(size: Int): List<Trip> {
    val dummyList = mutableListOf<Trip>()

    for (i in 1..size) {
        dummyList.add(
            Trip(
                title = "Suggestion $i",
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
        //Body()
    }
}

data class AutocompletePlace(
    val placeId: String,
    val name: String
)

fun Place.toAutocompletePlace(): AutocompletePlace {
    return AutocompletePlace(placeId!!, name!!)
}

@Composable
fun AutocompletePlacePicker(onPlaceSelected: (AutocompletePlace) -> Unit) {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    var selectedPlace by remember { mutableStateOf<AutocompletePlace?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val autocompleteFragment = remember {
        AutocompleteSupportFragment.newInstance().apply {
            setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
            setActivityMode(AutocompleteActivityMode.OVERLAY)
        }
    }

    // Compose UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Autocomplete fragment
        AndroidView(factory = { context ->
            autocompleteFragment.requireView().apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (48 * density).toInt() // Adjust height as needed
                )
            }
        }) { view ->
            // Add the view to your Compose layout
        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                autocompleteFragment.setText(it)
            },
            label = { Text("Search for a place") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Button to log selected place
        Button(
            onClick = {
                selectedPlace?.let {
                    onPlaceSelected(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Log Selected Place")
        }
    }

    // Set up the PlaceSelectionListener
    LaunchedEffect(autocompleteFragment) {
        autocompleteFragment.setOnPlaceSelectedListener(object : AutocompleteSupportFragment.OnPlaceSelectedListener() {
            override fun onPlaceSelected(place: AutocompletePlace) {
                selectedPlace = place
                keyboardController?.hide()
            }

            override fun onError(status: Status) {
                Log.e("AutocompleteExample", "Error: $status")
            }
        })
    }
}