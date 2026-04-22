package com.smartx.rfidreader.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smartx.rfidreader.ui.main.config.ConfigFragment
import com.smartx.rfidreader.ui.main.reading.ReadingFragment

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ConfigFragment()
        1 -> ReadingFragment()
        else -> throw IllegalArgumentException("Invalid position: $position")
    }
}
