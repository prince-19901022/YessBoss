package com.example.princeporosh.yessboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.princeporosh.yessboss.PopupListener;
import com.example.princeporosh.yessboss.R;
import com.example.princeporosh.yessboss.model.TaskCategory;

import java.util.List;

/**
 * Created by Prince Porosh on 9/16/2018.
 */

public class TaskCategoryAdapter extends RecyclerView.Adapter<TaskCategoryAdapter.CategoryViewHolder> {

    private List<TaskCategory> dataSrc;
    private Context context;
    private PopupListener popupListener;

    public TaskCategoryAdapter(List<TaskCategory> dataSrc, Context context) {
        this.dataSrc = dataSrc;
        this.context = context;
    }

    public void setPopupListener(PopupListener popupListener){
        this.popupListener = popupListener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        holder.taskCategoryTV.setText(dataSrc.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return dataSrc.size();
    }

    public void setSavedCategories(List<TaskCategory> savedCategories) {
        dataSrc.clear();
        dataSrc.addAll(savedCategories);
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView taskCategoryTV;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            taskCategoryTV = itemView.findViewById(R.id.tv_category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(popupListener != null){
                popupListener.onCategorySelected(dataSrc.get(getAdapterPosition()));
            }else{
                Log.d("www.d.com", "Popup Listener Is Null");
            }
        }
    }

}
