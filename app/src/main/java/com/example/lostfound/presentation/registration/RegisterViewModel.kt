package com.example.lostfound.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel:ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registerState = MutableStateFlow<Result<Boolean>?>(null)
    val registerState: StateFlow<Result<Boolean>?> get() = _registerState

    fun register(email:String, password:String, repeatedPassword:String) {
        if(email.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()){
            _registerState.value = Result.failure(Exception("Please fill in all the fields."))
            return
        }

        if (password != repeatedPassword) {
            _registerState.value = Result.failure(Exception("Passwords do not match."))
            return
        }

        //creates user using firebase authentication
        viewModelScope.launch(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _registerState.value = if(task.isSuccessful){
                        Result.success(true)
                    }else{
                        Result.failure(task.exception ?: Exception("Registration failed"))
                    }
                }
        }
    }
}