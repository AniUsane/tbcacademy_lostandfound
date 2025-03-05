package com.example.lostfound.presentation.registration

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lostfound.presentation.base.BaseFragment
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun start() {
        listeners()
        observeRegistrationState()
    }

    private fun listeners(){
        binding.registerBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.editText?.text.toString()
            val repeatedPassword = binding.repeatedPassword.editText?.text.toString()

            if(email.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()){
                Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password != repeatedPassword){
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerViewModel.register(email, password, repeatedPassword)
        }

        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_logInFragment)
        }
    }

    //observes registration state and returns success or failure
    private fun observeRegistrationState(){
        viewLifecycleOwner.lifecycleScope.launch {
            registerViewModel.registerState.collectLatest { result ->
                result?.onSuccess {
                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registrationFragment_to_logInFragment)
                }?.onFailure {
                    Toast.makeText(requireContext(), "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}