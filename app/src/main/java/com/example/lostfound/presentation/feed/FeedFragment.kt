package com.example.lostfound.presentation.feed

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentFeedBinding
import com.example.lostfound.presentation.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class FeedFragment : BaseFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate) {
    private val viewModel: FeedViewModel by viewModels()

    override fun start() {
        val adapter = FeedPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            tab.text = if(position == 0) getString(R.string.lost_items) else getString(R.string.found_items)
        }.attach()
        listeners()
    }

    //listeners for navigation to profile screen and search view
    private fun listeners() {
        binding.profileIcon.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_profileFragment)
        }

        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })
    }
}