package com.example.lostfound.presentation.profile

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lostfound.presentation.base.BaseFragment
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun start() {
        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        loadSavedLanguage()
        viewModel.loadUserEmail()
        observeEmail()
        listeners()
    }

    //observes user email and shows it on the screen
    private fun observeEmail(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userEmail.collectLatest { email ->
                binding.userEmail.text = email ?: "No email found"
            }
        }
    }

    //loads saved language
    private fun loadSavedLanguage(){
        val savedLanguage = sharedPreferences.getString("language", "en") ?: "en"
        binding.languageToggle.isChecked = savedLanguage == "ka"
    }

    //sets app's language and recreates activity
    private fun setAppLanguage(language: String){
        sharedPreferences.edit().putString("language", language).apply()
        LocaleManager.setLocale(requireContext(), language)

        requireActivity().recreate()
    }

    private fun listeners(){
        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
            Toast.makeText(requireContext(), "Logged our successfully", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_profileFragment_to_logInFragment)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_feedFragment)
        }

        binding.languageToggle.setOnCheckedChangeListener { _, isChecked ->
            val selectedLanguage = if(isChecked) "ka" else "en"
            setAppLanguage(selectedLanguage)
        }
    }

}