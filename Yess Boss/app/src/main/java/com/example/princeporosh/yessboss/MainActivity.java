package com.example.princeporosh.yessboss;


import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.princeporosh.yessboss.adapter.PagerAdapterYB;
import com.example.princeporosh.yessboss.model.TaskCategory;
import com.example.princeporosh.yessboss.utility.CategoryListPopup;
import com.example.princeporosh.yessboss.utility.ToolbarWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupListener, TaskCreatorFragment.TaskCreatorListener{

    private FloatingActionButton createTaskFAB;
    private Toolbar toolBar;
    private ToolbarWrapper toolbarWrapper;
    private CategoryListPopup categoryPopup;

    private ViewPager viewPager;
    private PagerAdapterYB pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createTaskFAB = findViewById(R.id.fab_task_creator);
        toolBar = findViewById(R.id.tool_bar);
        viewPager = findViewById(R.id.view_pager);
        createTaskFAB.setOnClickListener(this);

        toolbarWrapper = ToolbarWrapper.getSingletonInstance(toolBar);
        toolbarWrapper.addCategoryClickListener(this);
        toolbarWrapper.setSelectedCategory(TaskCategory.getLastSelectedCategory());

        pagerAdapter = new PagerAdapterYB(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        categoryPopup = new CategoryListPopup(this, toolbarWrapper.getAnchorView());
        categoryPopup.addPopupListener(this);

        setSupportActionBar(toolBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.fab_task_creator : onTaskCreate(); break;
            case R.id.tv_task_catagory :
            case R.id.dropdown_icon : showTaskCategory(); break;
        }
    }

    private void showTaskCategory() {
        categoryPopup.showPopupWindow();
    }

    private void onTaskCreate() {

        viewPager.setVisibility(View.GONE);
        createTaskFAB.setVisibility(View.GONE);

        toolbarWrapper.configToolBarForFragment();
        TaskCreatorFragment fragment = TaskCreatorFragment.getInstanceOf(toolbarWrapper, this);
        categoryPopup.dismissPopupWindow();

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter,R.anim.exit)
                .add(R.id.fragment_holder, fragment)
                .commit();
    }

    @Override
    public void onCategorySelected(TaskCategory selectedCategory) {

        toolbarWrapper.setSelectedCategory(selectedCategory.getCategory());
        TaskCategory.setLastSelectedCategory(selectedCategory.getCategory());

        pagerAdapter.notifyAbout(selectedCategory);
        categoryPopup.dismissPopupWindow();
    }


    @Override
    public void onTaskCreationCompleted() {
        viewPager.setVisibility(View.VISIBLE);
        createTaskFAB.setVisibility(View.VISIBLE);
        pagerAdapter.reloadData();
    }
}
