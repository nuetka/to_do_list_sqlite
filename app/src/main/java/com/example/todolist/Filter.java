package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.CategoryAdapter;
import com.example.todolist.Model.NoteModel;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filter extends BottomSheetDialogFragment {
    public static final String TAG = "Filter";

    private RadioButton checkAll;
    private RadioButton resetAll;
    private int lastCheckedId1 = -1;
    private DataBaseHelper myyDb;
    Set<Integer> priority = new HashSet<>();
    List<String> filters = new ArrayList<>();




    public static Filter newInstance() {
        return new Filter();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RadioGroup radioGroup1 = view.findViewById(R.id.radioGroup1);
        myyDb = new DataBaseHelper(getContext());

        checkAll = view.findViewById(R.id.o1);
        resetAll = view.findViewById(R.id.o2);
//        lastCheckedId1 = radioGroup1.getCheckedRadioButtonId();
        filters = myyDb.getFilter();




        CheckBox odt = view.findViewById(R.id.one_day);

//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//                if(isChecked){
//
//                }


//                if (filters.isEmpty()) {
//                    // Если список пуст, добавляем новый элемент
//                    filters.add(isChecked ? "1" : "0");
//                } else {
//                    // Если список не пуст, заменяем первый элемент
//                    filters.set(0, isChecked ? "1" : "0");
//                }
//            }
//        });

        Button mSaveButton = view.findViewById(R.id.button_save);
        Button CancelButton = view.findViewById(R.id.button_cancel);

        CheckBox routine = view.findViewById(R.id.forever);
        CheckBox temp = view.findViewById(R.id.temp);
        CheckBox compl = view.findViewById(R.id.compl);
        CheckBox uncompl = view.findViewById(R.id.uncompl);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(odt.isChecked()){
                    filters.set(0, "1");
                }
                else{
                    filters.set(0, "0");
                }


                if(routine.isChecked()){
                    filters.set(1, "1");
                }
                else{
                    filters.set(1, "0");
                }

                if(temp.isChecked()){
                    filters.set(2, "1");
                }
                else{
                    filters.set(2, "0");
                }

                if(compl.isChecked()){
                    filters.set(3, "1");
                }
                else{
                    filters.set(3, "0");
                }

                if(uncompl.isChecked()){
                    filters.set(4, "1");
                }
                else{
                    filters.set(4, "0");
                }




                myyDb.updateFilter(filters);
                dismiss();

            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // не забыть удалить категорию если добавлениа
                dismiss();
            }
        });






// Получите ссылку на RecyclerView из макета
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCategories);

// Создайте список категорий (пример)
        List<String> categoriesList = Arrays.asList("Category 1", "Category 2", "Category 3");

// Создайте адаптер и установите его для RecyclerView
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoriesList);
        recyclerView.setAdapter(categoryAdapter);

// Вы можете также установить менеджер компоновки (Layout Manager), например, LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

//        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        final RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
        final RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
        final RadioButton radioButton3 = view.findViewById(R.id.radioButton3);
        final RadioButton radioButton4 = view.findViewById(R.id.radioButton4);
        final RadioButton radioButton5 = view.findViewById(R.id.radioButton5);

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priority.contains(3)) {
                    radioButton3.setChecked(false);
                    priority.remove(3);
                } else {
                    priority.add(3);
                    radioButton3.setChecked(true);
                }
            }
        });

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priority.contains(1)) {
                    radioButton1.setChecked(false);
                    priority.remove(1);
                } else {
                    priority.add(1);
                    radioButton1.setChecked(true);
                }
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priority.contains(2)) {
                    radioButton2.setChecked(false);
                    priority.remove(2);
                } else {
                    priority.add(2);
                    radioButton2.setChecked(true);
                }
            }
        });

        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priority.contains(4)) {
                    radioButton4.setChecked(false);
                    priority.remove(4);
                } else {
                    priority.add(4);
                    radioButton4.setChecked(true);
                }
            }
        });

        radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priority.contains(5)) {
                    radioButton5.setChecked(false);
                    priority.remove(5);
                } else {
                    priority.add(5);
                    radioButton5.setChecked(true);
                }
            }
        });



//        lastCheckedId1 = radioGroup.getCheckedRadioButtonId(); // Устанавливает последний выбранный RadioButton

        // Установка слушателей на чекбоксы
        // Установка слушателей на радиокнопки
        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId1 == R.id.o1) {
                    checkAll.setChecked(false);
                    lastCheckedId1 = -1;
                } else {
                    lastCheckedId1 = R.id.o1;
                }
            }
        });
        resetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId1 == R.id.o2) {
                    resetAll.setChecked(false);
                    lastCheckedId1 = -1;
                } else {
                    lastCheckedId1 = R.id.o2;
                }
            }
        });



    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);


        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }



}

