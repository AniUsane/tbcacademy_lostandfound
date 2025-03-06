package com.example.lostfound.presentation.found

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lostfound.R
import com.example.lostfound.data.model.LostFoundItem
import com.example.lostfound.databinding.FragmentFoundBinding
import com.example.lostfound.presentation.base.BaseFragment
import com.example.lostfound.presentation.feed.FeedViewModel
import com.example.lostfound.presentation.lost.ItemAdapter
import com.example.lostfound.presentation.lost.LostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FoundFragment : BaseFragment<FragmentFoundBinding>(FragmentFoundBinding::inflate) {

    private val viewModel: LostViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels({requireParentFragment()})
    @Inject lateinit var adapter: ItemAdapter

    override fun start() {

        binding.lostRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.lostRecycler.adapter = adapter

        observeFoundItems()
        observeAddItemState()
        listeners()
        observeFilteredItems()
    }

    //observes found items and updates recycler view
    private fun observeFoundItems() {
        lifecycleScope.launch {
            viewModel.foundItems.collectLatest { items ->
                adapter.submitList(items)
                feedViewModel.setFoundItems(items)
            }
        }
    }

    //listener to add item
    private fun listeners(){
        binding.addItem.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_addItemFragment)
        }
    }

    private fun observeFilteredItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedViewModel.filteredFoundItems.collectLatest { items ->
                updateRecyclerView(items)
            }
        }
    }

    private fun updateRecyclerView(items: List<LostFoundItem>) {
        (binding.lostRecycler.adapter as? ItemAdapter)?.submitList(items)
    }

    //observes add item state and shows whether it is successful or not
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