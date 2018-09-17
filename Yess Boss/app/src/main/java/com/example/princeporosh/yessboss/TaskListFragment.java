package com.example.princeporosh.yessboss;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.princeporosh.yessboss.adapter.TaskListAdapter;
import com.example.princeporosh.yessboss.model.TheTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {

    private RecyclerView taskListRV;
    private TaskListAdapter adapter;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskListRV = view.findViewById(R.id.rv_task_list);

        taskListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskListRV.setItemAnimator(new DefaultItemAnimator());

        //TODO : 2. Remove getFakeTask() and populate RV with real task according to category
        adapter = new TaskListAdapter(getFakeTask(), getActivity());
        taskListRV.setAdapter(adapter);
    }

    private List<TheTask> getFakeTask(){

        List<TheTask> list = new ArrayList<>();
        TheTask task;

        for(int i = 0; i < 5; i++){
            task = new TheTask();
            task.setPriorityLevel((byte) 1);
            task.setTaskCategory("All");
            task.setTaskDescription("This is just a low priority test task. Best Of Luck");
            list.add(task);
        }

        for(int i = 0; i < 5; i++){
            task = new TheTask();
            task.setPriorityLevel((byte) 2);
            task.setTaskCategory("All");
            task.setTaskDescription("This is just a mid priority test task. Best Of Luck");
            list.add(task);
        }

        for(int i = 0; i < 5; i++){
            task = new TheTask();
            task.setPriorityLevel((byte) 3);
            task.setTaskCategory("All");
            task.setTaskDescription("This is just a high priority test task. Best Of Luck");
            list.add(task);
        }

        for(int i = 0; i < 5; i++){
            task = new TheTask();
            task.setPriorityLevel((byte) 0);
            task.setTaskCategory("All");
            task.setTaskDescription("This is just a priority less test task. Best Of Luck");
            list.add(task);
        }

        return list;
    }
}
