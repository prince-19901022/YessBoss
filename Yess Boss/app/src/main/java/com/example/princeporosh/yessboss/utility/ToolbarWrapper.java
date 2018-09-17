package com.example.princeporosh.yessboss.utility;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.princeporosh.yessboss.R;

/**
 * Created by Prince Porosh on 8/27/2018.
 */

public class ToolbarWrapper {

    private static ToolbarWrapper singletonInstance;

    private ImageButton saveButton;
    private ImageButton cancelButton;
    private ImageButton dropDown;
    private TextView taskCategoryTV;
    private TextView screenTitleTV;
    private SearchView taskSearchView;
    private View anchorView;

    private ConstraintLayout toolBarLayout;
    private ConstraintSet activitySet;
    private ConstraintSet fragmentSet;


    private ToolbarWrapper(){

    }

    private void init(Toolbar toolBar){

        saveButton = toolBar.findViewById(R.id.btn_save);
        cancelButton = toolBar.findViewById(R.id.btn_cancel);
        taskCategoryTV = toolBar.findViewById(R.id.tv_task_catagory);
        screenTitleTV = toolBar.findViewById(R.id.tv_screen_title);
        taskSearchView = toolBar.findViewById(R.id.search_view);
        anchorView = toolBar.findViewById(R.id.popup_anchor);
        EditText searchText =  taskSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        dropDown = toolBar.findViewById(R.id.dropdown_icon);

        toolBarLayout = toolBar.findViewById(R.id.cl_toolbar);

        activitySet = new ConstraintSet();
        activitySet.clone(toolBarLayout);

        fragmentSet = new ConstraintSet();
        fragmentSet.clone(toolBarLayout);

        searchText.setTextColor(Color.WHITE);
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16.0f);
        searchText.setHintTextColor(Color.WHITE);

        taskSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskCategoryTV.setVisibility(View.GONE);
                dropDown.setVisibility(View.GONE);
            }
        });

        taskSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                taskCategoryTV.setVisibility(View.VISIBLE);
                dropDown.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    public static ToolbarWrapper getSingletonInstance(Toolbar toolBar){
        if(singletonInstance == null){
            singletonInstance = new ToolbarWrapper();
        }
        singletonInstance.init(toolBar);
        return singletonInstance;
    }

    public void addSaveClickListener(View.OnClickListener listener){
        saveButton.setOnClickListener(listener);
    }

    public void addCancelClickListener(View.OnClickListener cancelListener){
        cancelButton.setOnClickListener(cancelListener);
    }

    public void addCategoryClickListener(View.OnClickListener categoryListener){
        taskCategoryTV.setOnClickListener(categoryListener);
        dropDown.setOnClickListener(categoryListener);
    }

    public void addSearchViewListener(SearchView.OnQueryTextListener queryListener){
        taskSearchView.setOnQueryTextListener(queryListener);
    }

    public void configToolBarForActivity(){
        TransitionManager.beginDelayedTransition(toolBarLayout);
        activitySet.applyTo(toolBarLayout);
    }

    public void configToolBarForFragment(){
        TransitionManager.beginDelayedTransition(toolBarLayout);
        fragmentSet.setVisibility(R.id.tv_task_catagory, ConstraintSet.GONE);
        fragmentSet.setVisibility(taskSearchView.getId(), ConstraintSet.GONE);
        fragmentSet.setVisibility(dropDown.getId(), ConstraintSet.GONE);

        fragmentSet.setVisibility(saveButton.getId(), ConstraintSet.VISIBLE);
        fragmentSet.setVisibility(cancelButton.getId(), ConstraintSet.VISIBLE);
        fragmentSet.setVisibility(screenTitleTV.getId(), ConstraintSet.VISIBLE);

        fragmentSet.applyTo(toolBarLayout);
    }

    public void setSelectedCategory(String category){
        taskCategoryTV.setText(category);
    }

    public View getAnchorView(){
        return anchorView;
    }
}
