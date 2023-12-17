package com.example.assignment4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.ArrowDropDown
import androidx.compose.material3.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewTreeObserver
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.PlacesStatusCodes

class MainActivity : ComponentActivity() {

    private lateinit var placesClient: PlacesClient

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        placesClient = Places.createClient(this)

        setContent {
            PlacesAutocomplete()
        }
    }
}

@Composable
fun PlacesAutocomplete() {
    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var isDropdownVisible by remember { mutableStateOf(false) }
    var keyboardController by remember { mutableStateOf<SoftwareKeyboardController?>(null) }
    val density = LocalDensity.current.density

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                fetchAutocompletePredictions(it)
            },
            placeholder = { Text("Search for a place") },
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Handle search action if needed
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        if (isDropdownVisible && predictions.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
            ) {
                LazyColumn {
                    items(predictions) { prediction ->
                        DropdownItem(
                            prediction = prediction,
                            onItemSelected = {
                                selectedPlace = it
                                query = it.getPrimaryText(null).toString()
                                isDropdownVisible = false
                                keyboardController?.hide()
                            }
                        )
                    }
                }
            }
        }

        if (selectedPlace != null) {
            Text(
                text = "Selected Place: ${selectedPlace?.name}",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun DropdownItem(
    prediction: AutocompletePrediction,
    onItemSelected: (Place) -> Unit
) {
    val placeId = prediction.placeId

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { fetchPlaceDetails(placeId, onItemSelected) }
            .padding(8.dp)
    ) {
        Text(text = prediction.getPrimaryText(null).toString())
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = prediction.getSecondaryText(null).toString(), color = Color.Gray)
    }
}

fun fetchAutocompletePredictions(query: String) {
    val context = LocalContext.current
    val placesClient = Places.createClient(context)

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setTypeFilter(TypeFilter.ADDRESS)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            predictions = response.autocompletePredictions
        }
        .addOnFailureListener { exception ->
            Toast.makeText(context, "Error fetching predictions: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
}

fun fetchPlaceDetails(placeId: String, onItemSelected: (Place) -> Unit) {
    val context = LocalContext.current
    val placesClient = Places.createClient(context)

    val request = FetchPlaceRequest.builder(placeId)
        .build()

    placesClient.fetchPlace(request)
        .addOnSuccessListener { place ->
            onItemSelected(place)
        }
        .addOnFailureListener { exception ->
            Toast.makeText(context, "Error fetching place details: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
}

@Preview
@Composable
fun PreviewPlacesAutocomplete() {
    PlacesAutocomplete()
}


// import android.content.Intent
// import android.os.Bundle
// import android.text.TextUtils
// import android.view.View
// import android.widget.CheckBox
// import android.widget.TextView
// import androidx.activity.result.contract.ActivityResultContracts
// import androidx.annotation.IdRes
// import androidx.annotation.StringRes
// import androidx.appcompat.app.AlertDialog
// import com.google.android.gms.common.api.Status
// import com.google.android.libraries.places.api.Places
// import com.google.android.libraries.places.api.model.AutocompleteSessionToken
// import com.google.android.libraries.places.api.model.LocationBias
// import com.google.android.libraries.places.api.model.LocationRestriction
// import com.google.android.libraries.places.api.model.Place
// import com.google.android.libraries.places.api.model.RectangularBounds
// import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
// import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
// import com.google.android.libraries.places.api.net.PlacesClient
// import com.google.android.libraries.places.widget.Autocomplete
// import com.google.android.libraries.places.widget.AutocompleteActivity
// import com.google.android.libraries.places.widget.AutocompleteSupportFragment
// import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
// import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
// import androidx.compose.foundation.layout.Arrangement
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.wrapContentHeight
// import androidx.compose.foundation.lazy.LazyColumn
// import androidx.compose.foundation.lazy.items
// import androidx.compose.material3.Button
// import androidx.compose.material3.Card
// import androidx.compose.material3.MaterialTheme
// import androidx.compose.material3.Text
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.tooling.preview.Preview
// import androidx.compose.ui.unit.dp
// import androidx.compose.ui.unit.sp
// import com.example.assignment4.sampledata.Activity
// import com.example.assignment4.sampledata.Trip
// import androidx.activity.ComponentActivity
// import androidx.activity.compose.setContent
// import androidx.compose.material3.Surface

// import com.example.assignment4.ui.theme.Assignment4Theme

// /**
//  * Activity to demonstrate Place Autocomplete (activity widget intent, fragment widget, and
//  * [PlacesClient.findAutocompletePredictions]).
//  */
// class PlaceAutocompleteActivity : ComponentActivity() {

//     private lateinit var placesClient: PlacesClient
//     private lateinit var fieldSelector: FieldSelector

//     private lateinit var binding: PlaceAutocompleteActivityBinding
//     override fun onCreate(savedInstanceState: Bundle?) {
//         super.onCreate(savedInstanceState)
//         setContent {
//             Assignment4Theme {
//                 // A surface container using the 'background' color from the theme
//                 Surface(
//                     modifier = Modifier.fillMaxSize(),
//                     color = MaterialTheme.colorScheme.background
//                 ) {
//                     AutoCompleteActivityScreen()
//                 }
//             }
//         }
//     }
// }

// //        binding = PlaceAutocompleteActivityBinding.inflate(layoutInflater)
// //        setContentView(binding.root)
// //
// //        // Retrieve a PlacesClient (previously initialized - see MainActivity)
// //        placesClient = Places.createClient(this)
// //
// //        // Set up view objects
// //
// //        val useTypesFilterCheckBox =
// //            findViewById<CheckBox>(R.id.autocomplete_use_types_filter_checkbox)
// //        useTypesFilterCheckBox.setOnCheckedChangeListener { _, isChecked: Boolean ->
// //            binding.autocompleteTypesFilterEdittext.isEnabled = isChecked
// //        }
// //        fieldSelector = FieldSelector(
// //            findViewById(R.id.use_custom_fields),
// //            findViewById(R.id.custom_fields_list),
// //            savedInstanceState
// //        )
// //        setupAutocompleteSupportFragment()
// //
// //        // Set listeners for Autocomplete activity
// //        binding.autocompleteActivityButton
// //            .setOnClickListener { startAutocompleteActivity() }
// //
// //        // Set listeners for programmatic Autocomplete
// //        binding.fetchAutocompletePredictionsButton
// //            .setOnClickListener { findAutocompletePredictions() }
// //
// //        // UI initialization
// //        setLoading(false)
// //    }

// //    override fun onSaveInstanceState(bundle: Bundle) {
// //        super.onSaveInstanceState(bundle)
// //        fieldSelector.onSaveInstanceState(bundle)
// //    }

//     private fun setupAutocompleteSupportFragment() {
//         val autocompleteSupportFragment =
//             supportFragmentManager.findFragmentById(R.id.autocomplete_support_fragment) as AutocompleteSupportFragment?
//         autocompleteSupportFragment!!.setPlaceFields(placeFields)
//         autocompleteSupportFragment.setOnPlaceSelectedListener(placeSelectionListener)
//         findViewById<View>(R.id.autocomplete_support_fragment_update_button)
//             .setOnClickListener {
//                 autocompleteSupportFragment
//                     .setPlaceFields(placeFields)
//                     .setText(query)
//                     .setHint(hint)
//                     .setCountries(countries)
//                     .setLocationBias(locationBias)
//                     .setLocationRestriction(locationRestriction)
//                     .setTypesFilter(getTypesFilter())
//                     .setActivityMode(mode)
//             }
//     }

//     private val placeSelectionListener: PlaceSelectionListener
//         get() = object : PlaceSelectionListener {
//             override fun onPlaceSelected(place: Place) {
//                 binding.response.text =
//                     StringUtil.stringifyAutocompleteWidget(place, isDisplayRawResultsChecked)
//             }

//             override fun onError(status: Status) {
//                 binding.response.text = status.statusMessage
//             }
//         }

//     /**
//      * Launches Autocomplete activity and handles result
//      */
//     private var autocompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//         result ->
//         when (result.resultCode) {
//             AutocompleteActivity.RESULT_OK -> {
//                 val data: Intent? = result.data
//                 if (data != null) {
//                     val place = Autocomplete.getPlaceFromIntent(data)
//                     binding.response.text =
//                         StringUtil.stringifyAutocompleteWidget(place, isDisplayRawResultsChecked)
//                 }
//             }
//             AutocompleteActivity.RESULT_ERROR -> {
//                 val status = Autocomplete.getStatusFromIntent(intent)
//                 binding.response.text = status.statusMessage
//             }
//             AutocompleteActivity.RESULT_CANCELED -> {
//                 // The user canceled the operation.
//             }
//         }
//     }

//     private fun startAutocompleteActivity() {
//         val autocompleteIntent = Autocomplete.IntentBuilder(mode, placeFields)
//             .setInitialQuery(query)
//             .setHint(hint)
//             .setCountries(countries)
//             .setLocationBias(locationBias)
//             .setLocationRestriction(locationRestriction)
//             .setTypesFilter(getTypesFilter())
//             .build(this)
//         autocompleteLauncher.launch(autocompleteIntent)
//     }

//     private fun findAutocompletePredictions() {
//         setLoading(true)
//         val requestBuilder = FindAutocompletePredictionsRequest.builder()
//             .setQuery(query)
// //            .setCountries(countries)
// //            .setOrigin(origin)
// //            .setLocationBias(locationBias)
// //            .setLocationRestriction(locationRestriction)
// //            .setTypesFilter(getTypesFilter())
//         if (isUseSessionTokenChecked) {
//             requestBuilder.sessionToken = AutocompleteSessionToken.newInstance()
//         }
//         val task = placesClient.findAutocompletePredictions(requestBuilder.build())
//         task.addOnSuccessListener { response: FindAutocompletePredictionsResponse? ->
//             response?.let {
//                 binding.response.text = StringUtil.stringify(it, isDisplayRawResultsChecked)
//             }
//         }
//         task.addOnFailureListener { exception: Exception ->
//             exception.printStackTrace()
//             binding.response.text = exception.message
//         }
//         task.addOnCompleteListener {
//             setLoading(
//                 false
//             )
//         }
//     }

//     //////////////////////////
//     // Helper methods below //
//     //////////////////////////
//     private val placeFields: List<Place.Field>
//         get() = if ((findViewById<View>(R.id.use_custom_fields) as CheckBox).isChecked) {
//             fieldSelector.selectedFields
//         } else {
//             fieldSelector.allFields
//         }

//     private val query: String?
//         get() = getTextViewValue(R.id.autocomplete_query)

//     private val hint: String?
//         get() = getTextViewValue(R.id.autocomplete_hint)

//     private val countries: List<String>
//         get() {
//             val countryString = getTextViewValue(R.id.autocomplete_country) ?: return emptyList()
//             return StringUtil.countriesStringToArrayList(countryString)
//         }

//     private fun getTextViewValue(@IdRes textViewResId: Int): String? {
//         val value = (findViewById<View>(textViewResId) as TextView).text.toString()
//         return if (TextUtils.isEmpty(value)) null else value
//     }

//     private val locationBias: LocationBias?
//         get() = getBounds(
//             R.id.autocomplete_location_bias_south_west, R.id.autocomplete_location_bias_north_east
//         )

//     private val locationRestriction: LocationRestriction?
//         get() = getBounds(
//             R.id.autocomplete_location_restriction_south_west,
//             R.id.autocomplete_location_restriction_north_east
//         )

//     private fun getBounds(resIdSouthWest: Int, resIdNorthEast: Int): RectangularBounds? {
//         val southWest = findViewById<TextView>(resIdSouthWest).text.toString()
//         val northEast = findViewById<TextView>(resIdNorthEast).text.toString()
//         if (TextUtils.isEmpty(southWest) && TextUtils.isEmpty(northEast)) {
//             return null
//         }
//         val bounds = StringUtil.convertToLatLngBounds(southWest, northEast)
//         if (bounds == null) {
//             showErrorAlert(R.string.error_alert_message_invalid_bounds)
//             return null
//         }
//         return RectangularBounds.newInstance(bounds)
//     }

//     private val origin: LatLng?
//         get() {
//             val originStr =
//                 findViewById<TextView>(R.id.autocomplete_location_origin).text.toString()
//             if (TextUtils.isEmpty(originStr)) {
//                 return null
//             }
//             val origin = StringUtil.convertToLatLng(originStr)
//             if (origin == null) {
//                 showErrorAlert(R.string.error_alert_message_invalid_origin)
//                 return null
//             }
//             return origin
//         }

//     private fun getTypesFilter(): List<String> {
//         return if (binding.autocompleteTypesFilterEdittext.isEnabled)
//             binding.autocompleteTypesFilterEdittext.text.toString().split("[\\s,]+".toRegex())
//         else emptyList()
//     }

//     private val mode: AutocompleteActivityMode
//         get() {
//             val isOverlayMode =
//                 binding.autocompleteActivityOverlayMode.isChecked
//             return if (isOverlayMode) AutocompleteActivityMode.OVERLAY else AutocompleteActivityMode.FULLSCREEN
//         }

//     private val isDisplayRawResultsChecked: Boolean
//         get() = binding.displayRawResults.isChecked

//     private val isUseSessionTokenChecked: Boolean
//         get() = binding.autocompleteUseSessionToken.isChecked

//     private fun setLoading(loading: Boolean) {
//         findViewById<View>(R.id.loading).visibility = if (loading) View.VISIBLE else View.INVISIBLE
//     }

//     private fun showErrorAlert(@StringRes messageResId: Int) {
//         AlertDialog.Builder(this)
//             .setTitle(R.string.error_alert_title)
//             .setMessage(messageResId)
//             .show()
//     }
// }
