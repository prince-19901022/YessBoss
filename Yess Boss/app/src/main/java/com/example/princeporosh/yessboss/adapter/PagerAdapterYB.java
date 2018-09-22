package com.example.princeporosh.yessboss.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.princeporosh.yessboss.PopupListener;
import com.example.princeporosh.yessboss.TaskCompletedFragment;
import com.example.princeporosh.yessboss.TaskCreatorFragment;
import com.example.princeporosh.yessboss.TaskListFragment;
import com.example.princeporosh.yessboss.model.TaskCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince Porosh on 9/10/2018.
 */

public class PagerAdapterYB extends FragmentStatePagerAdapter {

    private List<Fragment> pages;
    private PopupListener popupListener;

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

    public void notifyAbout(TaskCategory selectedCategory){

        popupListener = (PopupListener) pages.get(0);
        popupListener.onCategorySelected(selectedCategory);

        popupListener = (PopupListener) pages.get(1);
        popupListener.onCategorySelected(selectedCategory);
    }

    public void reloadData(){
        TaskCreatorFragment.TaskCreatorListener listener = (TaskCreatorFragment.TaskCreatorListener) pages.get(0);
        listener.onTaskCreationCompleted();
    }
}
