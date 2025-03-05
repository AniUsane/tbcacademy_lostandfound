package com.example.lostfound.presentation.login

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lostfound.presentation.base.BaseFragment
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentLogInBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {
    private val logInViewModel: LogInViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun start() {

        sharedPreferences = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        fixRememberMeDataType()
        loadSavedCredentials()
        listeners()
        observeLoginState()

    }

    private fun listeners(){
        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.editText?.text.toString()
            val rememberMe = binding.checkBox.isChecked

            //checks that email and password fields are not empty
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //saves or clears credentials based on rememberMe
            if(rememberMe){
                saveCredentials(email, password)
            }else{
                clearCredentials()
            }

            logInViewModel.login(email, password)
        }

        binding.registerText.setOnClickListener{
            findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
        }
    }

    //observes login state and handles success or failure messages
    private fun observeLoginState(){
        viewLifecycleOwner.lifecycleScope.launch {
            logInViewModel.loginState.collectLatest {result ->
                result?.onSuccess {
                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_logInFragment_to_feedFragment)
                }?.onFailure {
                    Toast.makeText(requireContext(), "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    //saves user email and password
    private fun saveCredentials(email: String, password: String){
        sharedPreferences.edit().apply{
            putString("email", email)
            putString("password", password)
            putBoolean("rememberMe", true)
            apply()
        }
    }

    //clears saved credentials
    private fun clearCredentials(){
        sharedPreferences.edit()
            .remove("email")
            .remove("password")
            .remove("rememberMe")
            .apply()
    }

    //loads saved credentials if rememberMe was checked
    private fun loadSavedCredentials() {
        val rememberMe = sharedPreferences.all["rememberMe"]

        val isRemembered = when (rememberMe) {
            is Boolean -> rememberMe
            is String -> rememberMe.toBoolean()
            else -> false
        }


        if (isRemembered) {
            binding.email.setText(sharedPreferences.getString("email", ""))
            binding.password.editText?.setText(sharedPreferences.getString("password", ""))
            binding.checkBox.isChecked = true
        }
    }

    //solves data type for rememberMe
    private fun fixRememberMeDataType() {
        val prefs = sharedPreferences
        val rememberMeValue = prefs.all["rememberMe"]
        if (rememberMeValue is String) {
            val correctValue = rememberMeValue.toBoolean()
            prefs.edit()
                .remove("rememberMe")
                .putBoolean("rememberMe", correctValue)
                .apply()
        }
    }
}