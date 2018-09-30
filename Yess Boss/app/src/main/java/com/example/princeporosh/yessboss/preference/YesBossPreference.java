package com.example.princeporosh.yessboss.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.princeporosh.yessboss.R;
import com.example.princeporosh.yessboss.model.TheTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    //It will serve for merging two list.
    private TheTask sentinel;

    public YesBossPreference(Context context) {
        this.context = context;
        sentinel = new TheTask();
        //Just a value which is less than any valid priority level[ranges from 0-3]
        sentinel.setPriorityLevel((byte) -1);

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
    private void saveTask(String key, String taskList){
        saveToPref(key,taskList);
    }

    //This should return array of tasks
    private String getTaskList(String key){
        return sharedPref.getString(key,"");
    }

    private List<TheTask> merge(List<TheTask> leftList, List<TheTask> rightList){
        List<TheTask> mergedList = new ArrayList<>();

        // Iteration pointer for left list;
        int i = 0;

        // Iteration pointer for right list;
        int j = 0;

        // Iteration pointer for merged list;
        int k;

        //total size after merging two list
        int mergeSize = leftList.size() + rightList.size();

        //This condition means both the list are empty there is nothing to merge
        if(mergeSize == 0){
            return mergedList;
        }

        leftList.add(sentinel);
        rightList.add(sentinel);

        for(k = 0; k < mergeSize; k++){

            if(leftList.get(i).getPriorityLevel() >= rightList.get(j).getPriorityLevel()){
                mergedList.add(leftList.get(i));
                i = i + 1;
            }else{
                mergedList.add(rightList.get(j));
                j = j + 1;
            }
        }

        return mergedList;
    }

    public void insertTask(TheTask task){

        List<TheTask> sortedList = new ArrayList<>();
        sortedList.add(task);

        sortedList = merge(getTaskFor(task.getTaskCategory(), true),sortedList);

        String json = convertIntoJSONString(sortedList);

        if(!json.isEmpty()){
            saveTask(task.getTaskCategory(), convertIntoJSONString(sortedList));
        }
    }

    private String convertIntoJSONString(List<TheTask> list){

        try{

            JSONArray taskArray = new JSONArray();
            JSONObject jsonTask;

            for(TheTask item : list){

                jsonTask = new JSONObject();
                jsonTask.put("TaskDescription", item.getTaskDescription());
                jsonTask.put("TaskCategory",item.getTaskCategory());
                jsonTask.put("PriorityLevel",item.getPriorityLevel());
                jsonTask.put("Date",item.getDate());
                jsonTask.put("Time",item.getTime());
                jsonTask.put("Done",item.isDone());
                taskArray.put(jsonTask);
            }

            return taskArray.toString();
        }catch(JSONException ex){
            ex.printStackTrace();
            Log.d("www.pref.com", ex.getMessage());
        }

        return "";
    }

    // isPending = true means i want the list of uncompleted task
    // isPending = false means i want the list of completed task

    public List<TheTask> getTaskFor(String category, boolean isPending){
        List<TheTask> list = new ArrayList<>();
        TheTask task;

        String tasks = getTaskList(category);

        if (!tasks.isEmpty()){

            try {
                JSONArray array = new JSONArray(tasks);
                int size = array.length();

                for (int i = 0; i < size; i++){

                    JSONObject obj = array.getJSONObject(i);
                    task = new TheTask();

                    task.setTaskCategory(obj.getString("TaskCategory"));
                    task.setTaskDescription(obj.getString("TaskDescription"));
                    task.setPriorityLevel((byte) obj.getInt("PriorityLevel"));
                    task.setTime(obj.getString("Time"));
                    task.setDate(obj.getString("Date"));
                    task.setDone(obj.getBoolean("Done"));

                    if(isPending){

                        if(!task.isDone()){
                            list.add(task);
                        }
                    }else{

                        if(task.isDone()){
                            list.add(task);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public List<TheTask> getAllTask(boolean isPendingTask){
        List<TheTask> listOfAllTask = new ArrayList<>();

        String categories = getCategories();

        try {
            JSONArray array = new JSONArray(categories);
            JSONObject object;
            int count = array.length();

            for(int i= 0; i < count; i++){
                object = array.getJSONObject(i);
                listOfAllTask.addAll(merge(listOfAllTask,getTaskFor(object.getString("category"),isPendingTask)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listOfAllTask;
    }

    public void updateTask(List<TheTask> updatedList){

        String json = convertIntoJSONString(updatedList);

        if (!json.isEmpty()){

            saveTask(updatedList.get(0).getTaskCategory(), json);
        }
    }
}
