package com.example.lostfound.presentation.feed

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentFeedBinding
import com.example.lostfound.presentation.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class FeedFragment : BaseFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate) {

    override fun start() {
        val adapter = FeedPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            tab.text = if(position == 0) getString(R.string.lost_items) else getString(R.string.found_items)
        }.attach()
        listeners()
    }

    //navigates to profile screen
    private fun listeners() {
        binding.profileIcon.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_profileFragment)
        }
    }
}