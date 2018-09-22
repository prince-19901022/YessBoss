package com.example.princeporosh.yessboss;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.princeporosh.yessboss.model.TaskCategory;
import com.example.princeporosh.yessboss.model.TheTask;
import com.example.princeporosh.yessboss.preference.YesBossPreference;
import com.example.princeporosh.yessboss.utility.CategoryCreatorListener;
import com.example.princeporosh.yessboss.utility.CategoryListPopup;
import com.example.princeporosh.yessboss.utility.ToolbarWrapper;
import com.example.princeporosh.yessboss.utility.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskCreatorFragment extends Fragment implements View.OnClickListener, PopupListener, CategoryCreatorListener{

    public interface TaskCreatorListener{
        void onTaskCreationCompleted();
    }

    private ToolbarWrapper toolbarWrapper;

    private EditText taskToDoEditText;
    private EditText onDateEditText;
    private EditText onTimeEditText;
    private EditText underTheCategoryEditText;

    private ImageButton lowPriorityIndicator;
    private ImageButton midPriorityIndicator;
    private ImageButton highPriorityIndicator;
    private ImageButton categoryAdd;

    private TextView priorityLabelLow;
    private TextView priorityLabelMid;
    private TextView priorityLabelHigh;

    private DatePickerDialog datePicker = null;
    private TimePickerDialog timePicker = null;
    private CategoryListPopup popup;
    private YesBossPreference prefYesBoss;
    private TaskCreatorListener taskCreatorListener;

    private TheTask theTask;

    public TaskCreatorFragment() {
        // Required empty public constructor
    }

    public static TaskCreatorFragment getInstanceOf(ToolbarWrapper toolbarWrapper, TaskCreatorListener taskCreatorListener){

        TaskCreatorFragment taskCreatorFragment = new TaskCreatorFragment();
        taskCreatorFragment.toolbarWrapper = toolbarWrapper;
        taskCreatorFragment.taskCreatorListener = taskCreatorListener;
        return taskCreatorFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        toolbarWrapper.addCancelClickListener(this);
        toolbarWrapper.addSaveClickListener(this);
        return inflater.inflate(R.layout.fragment_task_creator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        theTask = new TheTask();
        prefYesBoss = new YesBossPreference(getActivity());

        taskToDoEditText = view.findViewById(R.id.et_task_todo);
        onDateEditText = view.findViewById(R.id.et_date);
        onTimeEditText = view.findViewById(R.id.et_time);
        underTheCategoryEditText = view.findViewById(R.id.et_catagory);

        lowPriorityIndicator = view.findViewById(R.id.btn_low_priority);
        midPriorityIndicator = view.findViewById(R.id.btn_mid_priority);
        highPriorityIndicator = view.findViewById(R.id.btn_high_priority);
        categoryAdd = view.findViewById(R.id.btn_category_add);

        priorityLabelLow = view.findViewById(R.id.tv_low_priority_level);
        priorityLabelMid = view.findViewById(R.id.tv_mid_priority_level);
        priorityLabelHigh = view.findViewById(R.id.tv_high_priority_level);

        popup = new CategoryListPopup(getActivity(), underTheCategoryEditText);

        onDateEditText.setFocusable(false);
        onDateEditText.setKeyListener(null);
        onDateEditText.setOnClickListener(this);

        onTimeEditText.setFocusable(false);
        onTimeEditText.setKeyListener(null);
        onTimeEditText.setOnClickListener(this);

        underTheCategoryEditText.setFocusable(false);
        underTheCategoryEditText.setKeyListener(null);
        underTheCategoryEditText.setOnClickListener(this);

        lowPriorityIndicator.setOnClickListener(this);
        midPriorityIndicator.setOnClickListener(this);
        highPriorityIndicator.setOnClickListener(this);
        categoryAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.btn_cancel : removeOwnSelf();break;
            case R.id.btn_save : onTaskSaved();break;
            case R.id.btn_low_priority : onLowPriorityIndicatorClick();break;
            case R.id.btn_mid_priority : onMidPriorityIndicatorClick();break;
            case R.id.btn_high_priority : onHighPriorityIndicatorClick();break;

            case R.id.et_date : onDateEditTextClick(); break;
            case R.id.et_time : onTimeEditTextClicked(); break;
            case R.id.btn_category_add : onCategoryAdd();break;
            case R.id.et_catagory : showCategory(); break;

        }
    }

    private void onCategoryAdd() {

        ViewUtils.showCategoryCreatorDialog(getActivity() , this);
    }

    private void showCategory(){
        popup.addPopupListener(this);
        popup.showPopupWindowOnTopOfAnchor();
    }

    private void onTaskSaved(){

        theTask.setTaskDescription(taskToDoEditText.getText().toString());
        theTask.setTaskCategory(underTheCategoryEditText.getText().toString());

        JSONArray taskArray;
        String prevTasks = prefYesBoss.getTaskList(theTask.getTaskCategory());

        try{
            if(prevTasks.isEmpty()){
                taskArray = new JSONArray();
            }else{
                taskArray = new JSONArray(prevTasks);
            }

            JSONObject jsonTask = new JSONObject();
            jsonTask.put("TaskDescription", theTask.getTaskDescription());
            jsonTask.put("TaskCategory",theTask.getTaskCategory());
            jsonTask.put("PriorityLevel",theTask.getPriorityLevel());
            jsonTask.put("Date",theTask.getDate());
            jsonTask.put("Time",theTask.getTime());
            taskArray.put(jsonTask);

            prefYesBoss.saveTaskC(theTask.getTaskCategory(), taskArray.toString());
            removeOwnSelf();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void removeOwnSelf(){

        toolbarWrapper.configToolBarForActivity();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter,R.anim.exit)
                .remove(this)
                .commit();
    }

    private void onLowPriorityIndicatorClick(){

        assignPriority((byte) 1);
    }

    private void onMidPriorityIndicatorClick(){
        assignPriority((byte) 2);
    }

    private void onHighPriorityIndicatorClick(){
        assignPriority((byte) 3);
    }

    private void onDateEditTextClick(){

        if(datePicker == null){
            
            DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    theTask.setDate(String.format("%02d-%02d-%04d", day, month+1, year));
                    onDateEditText.setText(theTask.getDate());
                }
            };

            int [] dateElements = getDateToShow();
            datePicker = new DatePickerDialog(getActivity(), onDateSet, dateElements[0],
                    dateElements[1], dateElements[2]);
        }

        if(!datePicker.isShowing()){
            datePicker.show();
        }
    }

    private void onTimeEditTextClicked(){

        TimePickerDialog.OnTimeSetListener onTimeSet = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                String am_pm = (hour <= 12)? "AM" : "PM";
                int hourToShow = (hour > 12)? hour - 12 : hour;
                hourToShow = (hourToShow == 0)? 12 : hourToShow;
                theTask.setTime(String.format("%02d : %02d %s", hourToShow, minute, am_pm));
                onTimeEditText.setText(theTask.getTime());
            }
        };

        int [] timeArr = getTimeToShow();
        timePicker = new TimePickerDialog(getActivity(),
                onTimeSet,
                timeArr[0],
                timeArr[1],
                false);

        if(!timePicker.isShowing()){
            timePicker.show();
        }
    }

    private int[] getDateToShow(){

        if(theTask.getDate().isEmpty()){
            Calendar cal = Calendar.getInstance();
            return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)};
        }

        String[] dateArr = onDateEditText.getText().toString().split("-");

        return new int[]{Integer.parseInt(dateArr[2]),
                Integer.parseInt(dateArr[1]) - 1,
                Integer.parseInt(dateArr[0])};
    }

    private int[] getTimeToShow(){

        if(theTask.getTime().isEmpty()){

            Calendar cal = Calendar.getInstance();
            return new int[]{cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)};
        }

        String [] timeArr = theTask.getTime().split(" ");

        int hour = Integer.parseInt(timeArr[0]);
        hour = (hour < 12 && timeArr[3].equals("PM"))? hour + 12 : hour;
        hour = (hour == 12 && timeArr[3].equals("AM"))? hour + 12 : hour;
        int min = Integer.parseInt(timeArr[2]);

        return new int[]{hour, min};
    }

    private void assignPriority(byte givenPriority){

        byte currPriority = theTask.getPriorityLevel();

        currPriority = (currPriority == givenPriority)? (byte) (currPriority - 1) : givenPriority;

        if(currPriority == 1){
            lowPriorityIndicator.setImageResource(R.drawable.ic_low_priority);
            midPriorityIndicator.setImageResource(R.drawable.ic_no_priority);
            highPriorityIndicator.setImageResource(R.drawable.ic_no_priority);

            priorityLabelLow.setVisibility(View.VISIBLE);
            priorityLabelMid.setVisibility(View.VISIBLE);
            priorityLabelHigh.setVisibility(View.VISIBLE);

            priorityLabelLow.setTextColor(Color.parseColor("#003663"));;
            priorityLabelHigh.setTextColor(Color.parseColor("#8a003663"));
            priorityLabelMid.setTextColor(Color.parseColor("#8a003663"));

        }else if(currPriority == 2){

            lowPriorityIndicator.setImageResource(R.drawable.ic_mid_priority);
            midPriorityIndicator.setImageResource(R.drawable.ic_mid_priority);
            highPriorityIndicator.setImageResource(R.drawable.ic_no_priority);

            priorityLabelLow.setVisibility(View.GONE);
            priorityLabelMid.setVisibility(View.VISIBLE);
            priorityLabelHigh.setVisibility(View.VISIBLE);

            priorityLabelHigh.setTextColor(Color.parseColor("#8a003663"));
            priorityLabelMid.setTextColor(Color.parseColor("#003663"));

        }else if(currPriority == 3){
            lowPriorityIndicator.setImageResource(R.drawable.ic_high_priority);
            midPriorityIndicator.setImageResource(R.drawable.ic_high_priority);
            highPriorityIndicator.setImageResource(R.drawable.ic_high_priority);

            priorityLabelLow.setVisibility(View.GONE);
            priorityLabelMid.setVisibility(View.GONE);
            priorityLabelHigh.setVisibility(View.VISIBLE);

            priorityLabelHigh.setTextColor(Color.parseColor("#003663"));

        }else{

            lowPriorityIndicator.setImageResource(R.drawable.ic_no_priority);
            midPriorityIndicator.setImageResource(R.drawable.ic_no_priority);
            highPriorityIndicator.setImageResource(R.drawable.ic_no_priority);

            priorityLabelLow.setVisibility(View.VISIBLE);
            priorityLabelMid.setVisibility(View.VISIBLE);
            priorityLabelHigh.setVisibility(View.VISIBLE);

            priorityLabelLow.setTextColor(Color.parseColor("#8a003663"));;
            priorityLabelHigh.setTextColor(Color.parseColor("#8a003663"));
            priorityLabelMid.setTextColor(Color.parseColor("#8a003663"));
        }

        theTask.setPriorityLevel(currPriority);
    }

    @Override
    public void onStop() {
        super.onStop();

        taskCreatorListener.onTaskCreationCompleted();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(taskToDoEditText.getWindowToken(), 0);
    }

    @Override
    public void onCategorySelected(TaskCategory selectedCategory) {

        underTheCategoryEditText.setText(selectedCategory.getCategory());
        TaskCategory.setLastSelectedCategory(selectedCategory.getCategory());
        toolbarWrapper.setSelectedCategory(TaskCategory.getLastSelectedCategory());
        popup.dismissPopupWindow();
    }

    @Override
    public void onSpecify(TaskCategory category) {
        try {

            if(!category.getCategory().isEmpty()){
                JSONArray array = new JSONArray(prefYesBoss.getCategories());
                JSONObject object = new JSONObject();
                object.put("category", category.getCategory());
                object.put("isPermanent", category.isPermanent());
                array.put(object);
                prefYesBoss.saveTaskCategory(array.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();

            Log.d("www.yb.com", "JSONException : "+e.getMessage());
        }
    }
}
