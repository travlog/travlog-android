package com.travlog.android.apps.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray

import com.travlog.android.apps.ui.fragments.SignUpEmailFragment
import com.travlog.android.apps.ui.fragments.SignUpPasswordFragment

class SignUpPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments: SparseArray<Fragment> = SparseArray()

    init {
        fragments.put(0, SignUpEmailFragment.newInstance())
        fragments.put(1, SignUpPasswordFragment.newInstance())
    }

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size()
    }
}
