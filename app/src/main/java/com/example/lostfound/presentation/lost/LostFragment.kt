package com.example.lostfound.presentation.lost

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lostfound.presentation.base.BaseFragment
import com.example.lostfound.R
import com.example.lostfound.data.model.LostFoundItem
import com.example.lostfound.databinding.FragmentLostBinding
import com.example.lostfound.presentation.feed.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LostFragment : BaseFragment<FragmentLostBinding>(FragmentLostBinding::inflate) {

    private val viewModel: LostViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels({requireParentFragment()})
    @Inject lateinit var adapter: ItemAdapter

    override fun start() {

        binding.lostRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.lostRecycler.adapter = adapter

        observeItems()
        observeAddItemState()
        listeners()
        observeFilteredItems()
    }

    //observes items and updates adapter
    private fun observeItems() {
        lifecycleScope.launch {
            viewModel.lostItems.collectLatest { items ->
                adapter.submitList(items)
                feedViewModel.setLostItems(items)
            }
        }
    }

    //observes filtered lost items
    private fun observeFilteredItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedViewModel.filteredLostItems.collectLatest { items ->
                updateRecyclerView(items)
            }
        }
    }

    private fun updateRecyclerView(items: List<LostFoundItem>) {
        (binding.lostRecycler.adapter as? ItemAdapter)?.submitList(items)
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