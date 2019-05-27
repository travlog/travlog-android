/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travlog.android.apps.ui.adapters

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
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
