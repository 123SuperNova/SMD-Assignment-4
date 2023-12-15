
// private lateinit var placesClient: PlacesClient
//     private lateinit var fieldSelector: FieldSelector

//     private lateinit var binding: PlaceAutocompleteActivityBinding


// val task = placesClient.findAutocompletePredictions(requestBuilder.build())
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