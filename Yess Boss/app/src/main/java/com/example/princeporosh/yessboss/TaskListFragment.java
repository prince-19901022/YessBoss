package com.example.princeporosh.yessboss;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.princeporosh.yessboss.adapter.TaskListAdapter;
import com.example.princeporosh.yessboss.model.TaskCategory;
import com.example.princeporosh.yessboss.model.TheTask;
import com.example.princeporosh.yessboss.preference.YesBossPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment implements PopupListener, TaskCreatorFragment.TaskCreatorListener{

    private RecyclerView taskListRV;
    private TaskListAdapter adapter;
    private YesBossPreference prefYesBoss;
    private TextView noPendingTask;
    private Context context;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        prefYesBoss = new YesBossPreference(context);
        this.context = context;
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
        noPendingTask = view.findViewById(R.id.tv_msg_for_task_list);

        taskListRV.setLayoutManager(new LinearLayoutManager(context));
        taskListRV.setItemAnimator(new DefaultItemAnimator());

        String category = TaskCategory.getLastSelectedCategory();
        adapter = new TaskListAdapter(category.equals("All") ? prefYesBoss.getAllTask(true) : prefYesBoss.getTaskFor(category, true),
               context);

        adapter.setTaskRemovedListener(new TaskListAdapter.TaskRemovedListener() {
            @Override
            public void onItemRemoved() {
                handleEmptyMsgVisibility();
            }
        });

        taskListRV.setAdapter(adapter);

        handleEmptyMsgVisibility();
    }

    @Override
    public void onCategorySelected(TaskCategory selectedCategory) {

        if(selectedCategory.getCategory().equals("All")){
            adapter.setNewTasks(prefYesBoss.getAllTask(true));
        }else{
            adapter.setNewTasks(prefYesBoss.getTaskFor(selectedCategory.getCategory(),true));
        }

        handleEmptyMsgVisibility();
    }

    @Override
    public void onTaskCreationCompleted() {
        //Task will be loaded according last selected category
        adapter.setNewTasks(
                TaskCategory.getLastSelectedCategory().equals("All") ?
                        prefYesBoss.getAllTask(true) :
                        prefYesBoss.getTaskFor(TaskCategory.getLastSelectedCategory(),true));

        handleEmptyMsgVisibility();
    }

    private void handleEmptyMsgVisibility(){
        if(adapter.getItemCount() == 0){
            noPendingTask.setVisibility(View.VISIBLE);
        }else{
            noPendingTask.setVisibility(View.INVISIBLE);
        }
    }
}
