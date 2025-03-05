package com.example.lostfound.presentation.addItem

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lostfound.presentation.base.BaseFragment
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentAddItemBinding
import com.example.lostfound.data.model.ItemStatus
import com.example.lostfound.data.model.LostFoundItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddItemFragment : BaseFragment<FragmentAddItemBinding>(FragmentAddItemBinding::inflate) {
    private val viewModel: AddItemViewModel by viewModels()

    override fun start() {

        listeners()
        observeItems()

    }

    //listener for save button
    private fun listeners(){
        binding.buttonSave.setOnClickListener {
            val title = binding.titleLabel.text.toString()
            val description = binding.description.text.toString()

            if(title.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRadioId = binding.radioGroup.checkedRadioButtonId
            val status = when (selectedRadioId) {
                R.id.radioLost -> ItemStatus.LOST
                R.id.radioFound -> ItemStatus.FOUND
                else -> ItemStatus.LOST
            }

            val newItem = LostFoundItem(
                title = title,
                description = description,
                status = status
            )

            viewModel.addItem(newItem)

        }
    }

    //function observes the items states and notifies whether they are successfully added or not
    private fun observeItems(){
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