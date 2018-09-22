package com.example.princeporosh.yessboss;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.princeporosh.yessboss.model.TaskCategory;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskCompletedFragment extends Fragment implements PopupListener{


    public TaskCompletedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_completed, container, false);
    }

    @Override
    public void onCategorySelected(TaskCategory selectedCategory) {
        Log.d("www.finished.com"," Category : "+selectedCategory.getCategory());
    }
}
