package com.example.assignment4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.UUID

data class Activity(val id: String = "0", val title: String = "", val description: String = "", val start: String = "", val end: String = "", val location: String = "", val booking: String = "")
data class Trip(val id: String = "0", val title: String = "", val description: String = "", val activities: List<Activity> = emptyList())

data class UsersPlans(var plans: List<Trip> = emptyList()){
}

class MyViewModel : ViewModel() {

    private val _userData = MutableLiveData<UsersPlans>()
    val userData: LiveData<UsersPlans> get() = _userData

    fun getFirestoreData(){
        viewModelScope.launch {
            try {
                val trips = DataService.getUserData()
                _userData.value = UsersPlans(plans = trips)
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    fun setFirestoreData(){
        viewModelScope.launch {
            try {
                val trips = _userData.value?.plans ?: emptyList()
                val success = DataService.setUserData(trips)
                // Handle success or failure
                if (success){
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    fun setUserPlans(userPlans: UsersPlans) {
        _userData.value = userPlans
    }

    fun addTrip(trip: Trip) {
        val currentData = _userData.value ?: UsersPlans()
        val updatedPlans = currentData.plans.toMutableList().apply {
            add(trip)
        }
        _userData.value = currentData.copy(plans = updatedPlans)
    }

    fun addActivity(tripId: String, activity: Activity) {
        val currentData = _userData.value ?: UsersPlans()
        val updatedPlans = currentData.plans.toMutableList().apply {
            val tripIndex = indexOfFirst { it.id == tripId }
            if (tripIndex != -1) {
                this[tripIndex] = this[tripIndex].copy(
                    activities = this[tripIndex].activities + activity
                )
            }
        }
        _userData.value = currentData.copy(plans = updatedPlans)
    }

    fun getPlanById(planId: String): Trip? {
        return _userData.value?.plans?.find { it.id == planId }
    }

    fun getActivityById(planId: String, activityId: String): Activity? {
        val plan = getPlanById(planId)
        return plan?.activities?.find { it.id == activityId }
    }
}

fun generateUniqueId(): String {
    return UUID.randomUUID().toString()
}
