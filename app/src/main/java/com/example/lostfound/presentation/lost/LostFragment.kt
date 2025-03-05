package com.example.lostfound.presentation.lost

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lostfound.presentation.base.BaseFragment
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentLostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LostFragment : BaseFragment<FragmentLostBinding>(FragmentLostBinding::inflate) {

    private val viewModel: LostViewModel by viewModels()
    private lateinit var adapter: ItemAdapter
    override fun start() {
        adapter = ItemAdapter()

        binding.lostRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.lostRecycler.adapter = adapter

        observeItems()
        observeAddItemState()
        listeners()

    }

    //observes items and updates adapter
    private fun observeItems() {
        lifecycleScope.launch {
            viewModel.lostItems.collectLatest { items ->
                adapter.submitList(items)
            }
        }
    }

    private fun listeners(){
        binding.addItem.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_addItemFragment)
        }
    }

    private fun observeAddItemState() {
        lifecycleScope.launch {
            viewModel.addItemState.collectLatest { result ->
                result?.onSuccess {
                    Toast.makeText(requireContext(), "Item added successfully!", Toast.LENGTH_SHORT).show()
                }?.onFailure { error ->
                    Toast.makeText(requireContext(), "Failed to add item: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}