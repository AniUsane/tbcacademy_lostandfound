package com.example.lostfound.presentation.addItem

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentAddItemBinding
import com.example.lostfound.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddItemFragment : BaseFragment<FragmentAddItemBinding>(FragmentAddItemBinding::inflate) {
    private val viewModel: AddItemViewModel by viewModels()

    override fun start() {
        listeners()
        observeItems()
    }

    // Listener for save button
    private fun listeners() {
        binding.buttonSave.setOnClickListener {
            val title = binding.title.text.toString()
            val description = binding.description.text.toString()
            val location = binding.location.text.toString()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val status = if (binding.radioGroup.checkedRadioButtonId == R.id.radioLost) "LOST" else "FOUND"

            viewModel.addItem(title, description, location, status)
        }
    }

    //observes added items state
    private fun observeItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addItemState.collect { result ->
                result?.onSuccess {
                    Toast.makeText(requireContext(), "Item added successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }?.onFailure { error ->
                    Toast.makeText(requireContext(), "Could not add item: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}