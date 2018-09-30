package com.example.princeporosh.yessboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.princeporosh.yessboss.R;
import com.example.princeporosh.yessboss.model.TaskCategory;
import com.example.princeporosh.yessboss.model.TheTask;
import com.example.princeporosh.yessboss.preference.YesBossPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince Porosh on 9/17/2018.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>{

    public interface TaskRemovedListener{
       void onItemRemoved();
    }

    private List<TheTask> taskList;
    private Context context;
    private TaskRemovedListener taskRemovedListener;

    public TaskListAdapter(List<TheTask> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.task_item, parent, false);

        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        TheTask task = taskList.get(position);
        if(!task.isDone()){
            holder.bindItemViewWith(task);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setNewTasks(List<TheTask> list){

        taskList.clear();
        taskList.addAll(list);
        notifyDataSetChanged();
    }

    public void setTaskRemovedListener(TaskRemovedListener taskRemovedListener) {
        this.taskRemovedListener = taskRemovedListener;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        private TextView taskDescriptionTV;
        private TextView taskCategoryTV;
        private CheckBox taskCompletionCB;
        private ImageView starOne;
        private ImageView starTwo;
        private ImageView starThree;

        public TaskViewHolder(View itemView) {
            super(itemView);

            taskDescriptionTV = itemView.findViewById(R.id.tv_task_description);
            taskCategoryTV = itemView.findViewById(R.id.tv_item_category);

            taskCompletionCB = itemView.findViewById(R.id.cb_task_completion);

            starOne = itemView.findViewById(R.id.iv_star_one);
            starTwo = itemView.findViewById(R.id.iv_star_two);
            starThree = itemView.findViewById(R.id.iv_star_three);

            taskCompletionCB.setOnCheckedChangeListener(this);
        }

        public void bindItemViewWith(TheTask task){

            taskDescriptionTV.setText(task.getTaskDescription());
            taskCategoryTV.setText(task.getTaskCategory());

            if(task.getPriorityLevel() == 1){
                starOne.setImageResource(R.drawable.ic_low_priority);
                starTwo.setImageResource(R.drawable.ic_no_priority);
                starThree.setImageResource(R.drawable.ic_no_priority);
            }else if(task.getPriorityLevel() == 2){
                starOne.setImageResource(R.drawable.ic_mid_priority);
                starTwo.setImageResource(R.drawable.ic_mid_priority);
                starThree.setImageResource(R.drawable.ic_no_priority);
            }else if(task.getPriorityLevel() == 3){
                starOne.setImageResource(R.drawable.ic_high_priority);
                starTwo.setImageResource(R.drawable.ic_high_priority);
                starThree.setImageResource(R.drawable.ic_high_priority);
            }else{
                starOne.setImageResource(R.drawable.ic_no_priority);
                starTwo.setImageResource(R.drawable.ic_no_priority);
                starThree.setImageResource(R.drawable.ic_no_priority);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

            if (isChecked){
                //Completed : Remove The Item At getAdapterPosition().
                //Completed : Update Preference

                int pos = getAdapterPosition();
                taskList.get(pos).setDone(isChecked);

                YesBossPreference prefYesBoss = new YesBossPreference(context);
                prefYesBoss.updateTask(getListToBeUpdate(taskList.get(pos)));

                taskList.remove(pos);
                notifyItemRemoved(pos);
                taskRemovedListener.onItemRemoved();
            }
        }

        private List<TheTask> getListToBeUpdate(TheTask task){

            if(TaskCategory.getLastSelectedCategory().equals("All")){

                List<TheTask> list = new ArrayList<>();

                for(TheTask item : taskList){

                    if(item.getTaskCategory().equals(task.getTaskCategory())){
                        list.add(item);
                    }
                }

                return list;
            }

            return taskList;
        }
    }
}
