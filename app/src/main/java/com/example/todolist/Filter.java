package com.example.todolist;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.CategoryAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filter extends BottomSheetDialogFragment {

    private RadioButton radioButtonCheckAll;
    private RadioButton radioButtonResetAll;
    private int lastCheckedId1 = -1;
    Set<Integer> priority = new HashSet<>();



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

        radioButtonCheckAll = view.findViewById(R.id.o1);
        radioButtonResetAll = view.findViewById(R.id.o2);
//        lastCheckedId1 = radioGroup1.getCheckedRadioButtonId();


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
        radioButtonCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId1 == R.id.radioButton2) {
                    radioButtonCheckAll.setChecked(false);
                    lastCheckedId1 = -1;
                } else {
                    lastCheckedId1 = R.id.radioButton2;
                }
            }
        });
        radioButtonResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId1 == R.id.radioButton2) {
                    radioButtonResetAll.setChecked(false);
                    lastCheckedId1 = -1;
                } else {
                    lastCheckedId1 = R.id.radioButton2;
                }
            }
        });

    }
}

