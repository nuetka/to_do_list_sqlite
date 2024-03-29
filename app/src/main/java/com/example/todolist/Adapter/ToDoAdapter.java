package com.example.todolist.Adapter;

import static com.example.todolist.AddNewTask.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
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
        }else {
            holder.repeat.setVisibility(View.VISIBLE);
        }

        String c=null;

        c = myDB.findCategoryNameByIndex(item.getCategoryId());



            if ( c.equals("нет")) {
                holder.cat.setVisibility(View.GONE);
            } else {
                holder.cat.setText(c);
                holder.cat.setVisibility(View.VISIBLE);
            }

        if(item.getDuration().equals(":")){
            holder.dur.setVisibility(View.GONE);
            holder.dur1.setVisibility(View.GONE);
        }else {
         holder.dur1.setText(convertTimeFormat(item.getDuration()));
            holder.dur.setVisibility(View.VISIBLE);
            holder.dur1.setVisibility(View.VISIBLE);
        }

        if(item.getPriority()==6){
            holder.priority.setVisibility(View.GONE);
        }else if(item.getPriority()==5){
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.flagg);
            drawable = DrawableCompat.wrap(drawable);
            // Convert hex color string to int and then set the tint
            holder.priority.setVisibility(View.VISIBLE);
            int color = Color.parseColor("#0CE305");
            DrawableCompat.setTint(drawable, color);
            holder.priority.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

        }else if(item.getPriority()==4){
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.flagg);
            drawable = DrawableCompat.wrap(drawable);
            int color = Color.parseColor("#B9F345");
            holder.priority.setVisibility(View.VISIBLE);
            DrawableCompat.setTint(drawable, color);
            holder.priority.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

        }else if(item.getPriority()==3){
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.flagg);
            drawable = DrawableCompat.wrap(drawable);
            int color = Color.parseColor("#E3DA2F");
            holder.priority.setVisibility(View.VISIBLE);
            DrawableCompat.setTint(drawable, color);
            holder.priority.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

        }else if(item.getPriority()==2){
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.flagg);
            drawable = DrawableCompat.wrap(drawable);
            int color = Color.parseColor("#E3892F");
            holder.priority.setVisibility(View.VISIBLE);
            DrawableCompat.setTint(drawable, color);
            holder.priority.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

        }else if(item.getPriority()==1){
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.flagg);
            drawable = DrawableCompat.wrap(drawable);
            int color = Color.parseColor("#FF0000");
            holder.priority.setVisibility(View.VISIBLE);
            DrawableCompat.setTint(drawable, color);
            holder.priority.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

        }



        // Then attach the listener
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){
                    myDB.updateStatus(item, 1);
                    mList.get(position).setStatus(1);

                } else {
                    myDB.updateStatus(item, 0);
                    mList.get(position).setStatus(0);
                }
                //mList = myDB.getAllTasks();
                //Collections.reverse(mList);


                List<String> sortt  = myDB.getSort();

                Collections.sort(mList, new Comparator<ToDoModel>() {
                    @Override
                    public int compare(ToDoModel o1, ToDoModel o2) {
                        if (sortt.get(0).equals("1")) {
                            // Сначала сортируем по статусу (невыполненные задачи идут первыми)
                            int statusCompare = Integer.compare(o1.getStatus(), o2.getStatus());
                            if (statusCompare != 0) {
                                return statusCompare;
                            }
                        } else if (sortt.get(0).equals("2")) {
                            int statusCompare = Integer.compare(o1.getStatus(), o2.getStatus());
                            if (statusCompare != 0) {
                                return -statusCompare; // Используем отрицание для изменения порядка сортировки
                            }
                        }

                        if (sortt.get(1).equals("1")) {
                            // Затем сортируем по приоритету (высший приоритет идет первым)
                            // Предполагаем, что более низкое числовое значение означает более высокий приоритет
                            return Integer.compare(o1.getPriority(), o2.getPriority());
                        } else if (sortt.get(1).equals("2")) {
                            return -Integer.compare(o1.getPriority(), o2.getPriority());
                        }
                        return Integer.compare(o1.getId(), o2.getId());
                    }
                });


                setTasks(mList);
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
        bundle.putInt("isRoutine", item.getIsRoutine());

        bundle.putString("repeatEndDate" , item.getRepeatEndDate());
        bundle.putString("repeatDays" , item.getRepeatDays());

        bundle.putString("duration" , item.getDuration());

        bundle.putString("date" , item.getDate());
        bundle.putString("start" , item.getStart());
        bundle.putString("end" , item.getEnd());

        bundle.putInt("status" , item.getStatus());
        bundle.putInt("categoryId" , item.getCategoryId());
        bundle.putInt("priority" , item.getPriority());
        bundle.putInt("snoty" , item.getSnoty());
        bundle.putInt("enoty" , item.getEnoty());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());


    }

    @Override
    public int getItemCount() {
        if(mList==null){
            return 0;
        }
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
        Log.e(TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+originalTime+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        if (originalTime == null || !originalTime.matches("\\d{2}:\\d{2}")) {
        }

        String[] parts = originalTime.split(":");
        return parts[0] + "h:" + parts[1] + "m";
    }

}
