package com.example.princeporosh.yessboss.model;

/**
 * Created by Prince Porosh on 9/16/2018.
 */

public class TaskCategory {

    private String category;
    private boolean isPermanent;
    private static String lastSelectedCategory = "All";

    public TaskCategory(String category, boolean isPermanent) {
        this.category = category;
        this.isPermanent = isPermanent;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public static String getLastSelectedCategory(){
        return lastSelectedCategory;
    }

    public static void setLastSelectedCategory(String lsc){
        lastSelectedCategory = lsc;
    }
}
