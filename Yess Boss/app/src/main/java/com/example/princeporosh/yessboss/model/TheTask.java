package com.example.princeporosh.yessboss.model;

/**
 * Created by Prince Porosh on 9/2/2018.
 */

public class TheTask {

    private String taskDescription = "";
    private String date = "";
    private String time = "" ;
    private String taskCategory = "";
    private byte priorityLevel;

    private boolean isDone;

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public byte getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(byte priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
