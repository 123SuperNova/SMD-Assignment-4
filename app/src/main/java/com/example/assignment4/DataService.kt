package com.example.assignment4

import android.annotation.SuppressLint
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object DataService{
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore
    suspend fun getUserData(): List<Trip> {
        val userUid = Firebase.auth.currentUser?.uid.toString()
        val tripsCollectionRef = db.collection("users").document(userUid).collection("Trips")

        return try {
            val documents = tripsCollectionRef.get().await()
            documents.documents.mapNotNull { document ->
                document.toObject(Trip::class.java)
            }
        } catch (e: Exception) {
            // Handle exceptions
            emptyList()
        }
    }

    suspend fun setUserData(trips: List<Trip>): Boolean {
        val currentUserUid = Firebase.auth.currentUser?.uid.toString()

        if (currentUserUid.isNotBlank() && trips.isNotEmpty()) {
            val userDocumentRef = db.collection("users").document(currentUserUid)
            try {
                for (trip in trips) {
                    userDocumentRef.collection("Trips").document(trip.id.toString())
                        .set(trip).await()
                    for (activity in trip.activities){
                        userDocumentRef.collection("Trips").document(trip.id.toString()).collection("Activities").document(activity.id)
                            .set(activity).await()
                    }
                }
                return true
            } catch (e: Exception) {
                // Handle exceptions
                return false
            }
        }
        return false
    }
}

