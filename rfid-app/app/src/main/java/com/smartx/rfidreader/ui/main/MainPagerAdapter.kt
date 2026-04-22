package com.smartx.rfidreader.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smartx.rfidreader.ui.main.config.ConfigFragment
import com.smartx.rfidreader.ui.main.reader.ReaderSelectionFragment
import com.smartx.rfidreader.ui.main.reading.ReadingFragment

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 3
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ReaderSelectionFragment()
        1 -> ConfigFragment()
        2 -> ReadingFragment()
        else -> throw IllegalArgumentException("Invalid position: $position")
    }
}
