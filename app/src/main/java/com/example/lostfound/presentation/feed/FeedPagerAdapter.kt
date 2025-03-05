package com.example.lostfound.presentation.feed

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lostfound.presentation.found.FoundFragment
import com.example.lostfound.presentation.lost.LostFragment

//Adapter for view pager2 for switching between fragments
class FeedPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) LostFragment() else FoundFragment()
    }
}