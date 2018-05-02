package com.travlog.android.apps.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.travlog.android.apps.ui.fragments.SignUpEmailFragment;
import com.travlog.android.apps.ui.fragments.SignUpPasswordFragment;

public class SignUpPagerAdapter extends FragmentPagerAdapter {

    private final SparseArray<Fragment> fragments;

    public SignUpPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new SparseArray<>();
        fragments.put(0, SignUpEmailFragment.newInstance());
        fragments.put(1, SignUpPasswordFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
