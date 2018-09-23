package com.example.princeporosh.yessboss.utility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.example.princeporosh.yessboss.PopupListener;
import com.example.princeporosh.yessboss.R;
import com.example.princeporosh.yessboss.adapter.TaskCategoryAdapter;
import com.example.princeporosh.yessboss.model.TaskCategory;
import com.example.princeporosh.yessboss.preference.YesBossPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince Porosh on 9/16/2018.
 */

public class CategoryListPopup {

    private Context context;
    private View anchorView;
    private PopupWindow categoryListPopup = null;
    private RecyclerView categoryRecyclerView;
    private TaskCategoryAdapter adapter;
    
    private static boolean isShowing = false;

    public CategoryListPopup(Context context, View anchorView) {
        this.context = context;
        this.anchorView = anchorView;

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        initRecyclerView(popupView);

        categoryListPopup = new PopupWindow(popupView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                4, 4, 4, 4,
                4, 4, 4, 4}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.WHITE);
        categoryListPopup.setBackgroundDrawable(shapeDrawable);

        categoryListPopup.setOutsideTouchable(true);

        categoryListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isShowing = false;
            }
        });
    }

    public void showPopupWindow(){

        if(!isShowing){
            int[]anchorCoordinate = new int[2];
            anchorView.getLocationOnScreen(anchorCoordinate);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                categoryListPopup.setElevation(5.0f);
            }
            categoryListPopup.showAtLocation(anchorView, Gravity.NO_GRAVITY, anchorCoordinate[0], anchorCoordinate[1]);
            isShowing = true;

            adapter.setSavedCategories(getSavedCategories());
        }
    }

    public void showPopupWindowOnTopOfAnchor(){

        if(!isShowing){
            int[]anchorCoordinate = new int[2];
            anchorView.getLocationOnScreen(anchorCoordinate);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                categoryListPopup.setElevation(5.0f);
            }

            int popupHeight = (int)ViewUtils.convertDpToPixel(240.0f, context);
            categoryListPopup.showAtLocation(anchorView, Gravity.NO_GRAVITY, anchorCoordinate[0], anchorCoordinate[1] - popupHeight);
            isShowing = true;

            List<TaskCategory> catList = getSavedCategories();
            catList.remove(0);
            adapter.setSavedCategories(catList);
        }
    }

    private void initRecyclerView(View popupView){

        categoryRecyclerView = popupView.findViewById(R.id.rv_category_list);
        adapter = new TaskCategoryAdapter(new ArrayList<TaskCategory>(), context);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        categoryRecyclerView.setAdapter(adapter);
    }

    private List<TaskCategory> getSavedCategories(){
        List<TaskCategory> list = new ArrayList<>();
        YesBossPreference prefYesBoss = new YesBossPreference(context);

        try {
            JSONArray array = new JSONArray(prefYesBoss.getCategories());
            JSONObject item;
            int size = array.length();
            for (int i = 0; i < size; i++){

                item = array.getJSONObject(i);
                list.add(new TaskCategory(item.getString("category"), item.getBoolean("isPermanent")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void dismissPopupWindow(){
        if(isShowing){
            categoryListPopup.dismiss();
        }
    }

    public void addPopupListener(PopupListener pl){
        adapter.setPopupListener(pl);
    }
}
