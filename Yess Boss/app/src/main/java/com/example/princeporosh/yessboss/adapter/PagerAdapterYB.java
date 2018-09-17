package com.example.princeporosh.yessboss.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.princeporosh.yessboss.TaskCompletedFragment;
import com.example.princeporosh.yessboss.TaskListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince Porosh on 9/10/2018.
 */

public class PagerAdapterYB extends FragmentStatePagerAdapter {

    private List<Fragment> pages;

    public PagerAdapterYB(FragmentManager fm) {
        super(fm);
        pages = new ArrayList<>();

        pages.add(new TaskListFragment());
        pages.add(new TaskCompletedFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
