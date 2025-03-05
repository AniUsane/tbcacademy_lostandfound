package com.example.lostfound.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LogInViewModel():ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginState = MutableStateFlow<Result<Boolean>?>(null)
    val loginState: StateFlow<Result<Boolean>?> get() = _loginState

    //login function which checks that emails and password fields are not empty.
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginState.value = Result.failure(Exception("Email and password cannot be empty"))
            return
        }

        //performs firebase authentication with email and password
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user

                if (user != null) {
                    _loginState.value = Result.success(true)
                } else {
                    _loginState.value = Result.failure(Exception("Login failed: No user found"))
                }
            } catch (e: Exception) {
                _loginState.value = Result.failure(e)
            }
        }
    }
}