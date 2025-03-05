package com.example.lostfound.presentation.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> get() = _userEmail

    //loads user email in the profile screen
    fun loadUserEmail(){
        viewModelScope.launch(Dispatchers.IO) {
            val email = auth.currentUser?.email ?: sharedPreferences.getString("email", null)
            _userEmail.value = email
        }
    }

    //logout logic for the user
    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            auth.signOut()

            sharedPreferences.edit()
                .remove("email")
                .remove("password")
                .remove("rememberMe")
                .apply()

            _userEmail.value = null
        }
    }
}