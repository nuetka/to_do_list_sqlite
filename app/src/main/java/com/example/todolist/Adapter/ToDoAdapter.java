package com.example.todolist.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.R;
import com.example.todolist.Utils.DataBaseHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB , MainActivity activity) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ToDoModel item = mList.get(position);
        holder.mCheckBox.setOnCheckedChangeListener(null); // detach the listener
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));


        // Set the time for the task
        if(item.getStart().equals("0")) {
            holder.start.setText("___");
        }
        else{
            holder.start.setText(item.getStart());
        }
        if(item.getEnd().equals("0")) {
            holder.end.setText("___");
        }
        else{
            holder.end.setText(item.getEnd());
        }

        if(item.getSnoty()==1){
            holder.notificationCheckbox.setChecked(true);
        }
        else{
            holder.notificationCheckbox.setChecked(false);
        }

        if(item.getEnoty()==1){
            holder.notificationCheckbox1.setChecked(true);
        }
        else{
            holder.notificationCheckbox1.setChecked(false);

        }

        if(item.getIsRoutine()==0){
            holder.repeat.setVisibility(View.GONE);
        }

        String c=null;

        c = myDB.findCategoryNameByIndex(item.getCategoryId());



            if ( c.equals("0")) {
                holder.cat.setText("");
            } else {
                holder.cat.setText(c);
            }


        convertTimeFormat("34:55");

        if(item.getDuration().equals(":")){
            holder.dur.setVisibility(View.GONE);
            holder.dur1.setVisibility(View.GONE);
        }else {
         holder.dur1.setText(convertTimeFormat(item.getDuration()));
        }

        if(item.getPriority()==0){
            holder.priority.setVisibility(View.GONE);
        }else if(item.getPriority()==1){
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.flagg);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.RED);
            holder.priority.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

        }



        // Then attach the listener
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myDB.updateStatus(item.getId(), 1);
                    mList.get(position).setStatus(1);

                } else {
                    myDB.updateStatus(item.getId(), 0);
                    mList.get(position).setStatus(0);
                }
                //mList = myDB.getAllTasks();
                //Collections.reverse(mList);
                setTasks(mList.stream()
                        .sorted(Comparator.comparing(ToDoModel::getStatus))
                        .collect(Collectors.toList()));
            }
        });
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deletTask(int position){
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox, notificationCheckbox, notificationCheckbox1;
        TextView start, end, repeat,cat, dur, dur1, priority;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
            start = itemView.findViewById(R.id.start);
            end = itemView.findViewById(R.id.end);
            notificationCheckbox= itemView.findViewById(R.id.notificationCheckbox);
            notificationCheckbox1 = itemView.findViewById(R.id.notificationCheckbox1);
            repeat = itemView.findViewById(R.id.repeat);
            cat = itemView.findViewById(R.id.cat);
            dur = itemView.findViewById(R.id.dur);
            dur1 = itemView.findViewById(R.id.dur1);
            priority = itemView.findViewById(R.id.priority);
        }
    }

    public String convertTimeFormat(String originalTime) {
        if (originalTime == null || !originalTime.matches("\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid time format. Expected format is HH:mm.");
        }

        String[] parts = originalTime.split(":");
        return parts[0] + "h:" + parts[1] + "m";
    }

}
