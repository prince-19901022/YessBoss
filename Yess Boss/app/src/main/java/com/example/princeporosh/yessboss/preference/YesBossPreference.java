package com.example.princeporosh.yessboss.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.princeporosh.yessboss.R;

/**
 * Created by Prince on 9/17/2018.
 */

public class YesBossPreference {

    private Context context;
    private static SharedPreferences sharedPref = null;
    private static final String DEFAULT_CATEGORIES = "[{\"category\":\"All\",\"isPermanent\":true}," +
            "{\"category\":\"Android\",\"isPermanent\":true}," +
            "{\"category\":\"iOS\",\"isPermanent\":true}," +
            "{\"category\":\"Office\",\"isPermanent\":true}," +
            "{\"category\":\"Shopping List\",\"isPermanent\":true}," +
            "{\"category\":\"Households\",\"isPermanent\":true}]";

    public YesBossPreference(Context context) {
        this.context = context;

        if(sharedPref == null){
            sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
        }
    }

    private void saveToPref(String key, String value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //String taskCategory is a json string that contains array of categories
    public void saveTaskCategory(String taskCategory){
        saveToPref("TASK_CATEGORY_LIST",taskCategory);
    }

    //This should return array of categories
    public String getCategories(){
        return sharedPref.getString("TASK_CATEGORY_LIST",DEFAULT_CATEGORIES);
    }

    //String taskList is a json string that contains array of tasks
    public void saveTaskC(String key, String taskList){
        saveToPref(key,taskList);
    }

    //This should return array of tasks
    public String getTaskList(String key){
        return sharedPref.getString(key,"");
    }
}
