package com.example.todolist;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.CategoryAdapter;
import com.example.todolist.Model.CategoryModel;
import com.example.todolist.Model.NoteModel;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

    private RecyclerView recyclerView;




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

        // Установить обработчики кликов
//        checkAll.setOnClickListener(v -> {
//            // Отметить все категории
//            myyDb.updateAllCategoriesSelection(true);
//            // Обновить адаптер RecyclerView
//            updateAdapter();
//        });
//
//        resetAll.setOnClickListener(v -> {
//            // Снять отметки со всех категорий
//            myyDb.updateAllCategoriesSelection(false);
//            // Обновить адаптер RecyclerView
//            updateAdapter();
//        });

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
        CheckBox start = view.findViewById(R.id.start);
        CheckBox end = view.findViewById(R.id.end);
        CheckBox start_time = view.findViewById(R.id.start_time);
        CheckBox end_time = view.findViewById(R.id.end_time);


        Button btn = view.findViewById(R.id.from1);
        ImageView x = view.findViewById(R.id.myImageView);
        Button btn1 = view.findViewById(R.id.from);
        ImageView x1 = view.findViewById(R.id.myImageView1);
        Button btn3 = view.findViewById(R.id.from4);
        ImageView x3 = view.findViewById(R.id.myImageView2);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение текущего времени.
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Создание нового экземпляра TimePickerDialog и отображение его.
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Установка выбранного пользователем времени в EditText.
                                btn.setText(hourOfDay + ":" + String.format("%02d", minute));
                                btn.setEnabled(false);
                                x.setVisibility(View.VISIBLE);

                            }
                        }, hour, minute, DateFormat.is24HourFormat(getActivity()));

                timePickerDialog.show();
            }
        });


             x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn.setEnabled(true);
                    x.setVisibility(View.GONE);
                    btn.setText("pick a time");
                }


            });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение текущего времени.
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Создание нового экземпляра TimePickerDialog и отображение его.
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Установка выбранного пользователем времени в EditText.
                                btn1.setText(hourOfDay + ":" + String.format("%02d", minute));
                                btn1.setEnabled(false);
                                x1.setVisibility(View.VISIBLE);

                            }
                        }, hour, minute, DateFormat.is24HourFormat(getActivity()));

                timePickerDialog.show();
            }
        });


        x1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setEnabled(true);
                x1.setVisibility(View.GONE);
                btn1.setText("pick a time");
            }


        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение текущего времени.
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Создание нового экземпляра TimePickerDialog и отображение его.
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Установка выбранного пользователем времени в EditText.
                                btn3.setText(hourOfDay + ":" + String.format("%02d", minute));
                                btn3.setEnabled(false);
                                x3.setVisibility(View.VISIBLE);

                            }
                        }, hour, minute, DateFormat.is24HourFormat(getActivity()));

                timePickerDialog.show();
            }
        });


        x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn3.setEnabled(true);
                x3.setVisibility(View.GONE);
                btn3.setText("pick a time");
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
        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        List<CategoryModel> categoriesList = new ArrayList<>();

// Создайте список категорий (пример)
        categoriesList = myyDb.getAllCategories();

// Создайте адаптер и установите его для RecyclerView
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoriesList, myyDb);
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
                    // Отметить все категории
                    myyDb.updateAllCategoriesSelection(true);
                    // Обновить адаптер RecyclerView
                    updateAdapter();
                    lastCheckedId1 = R.id.o1;

                    odt.setChecked(true);
                    compl.setChecked(true);
                    uncompl.setChecked(true);
                    routine.setChecked(true);
                    temp.setChecked(true);
                    if(!priority.contains(1)){
                        priority.add(1);
                        radioButton1.setChecked(true);
                    }

                    if(!priority.contains(2)){
                        priority.add(2);
                        radioButton2.setChecked(true);
                    }

                    if(!priority.contains(3)){
                        priority.add(3);
                        radioButton3.setChecked(true);
                    }

                    if(!priority.contains(4)){
                        priority.add(4);
                        radioButton4.setChecked(true);
                    }

                    if(!priority.contains(5)){
                        priority.add(5);
                        radioButton5.setChecked(true);
                    }

                    start.setChecked(true);
                    end.setChecked(true);
                    start_time.setChecked(true);
                    end_time.setChecked(true);



                    // Показать Toast
                    showToast("Все категории выбраны");
                    checkAll.setChecked(false);
                    lastCheckedId1 = -1;

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
                    // Снять отметки со всех категорий
                    myyDb.updateAllCategoriesSelection(false);
                    // Обновить адаптер RecyclerView
                    updateAdapter();
                    // Показать Toast

                    odt.setChecked(false);
                    routine.setChecked(false);
                    temp.setChecked(false);
                    compl.setChecked(false);
                    uncompl.setChecked(false);
                    start.setChecked(false);
                    end.setChecked(false);
                    start_time.setChecked(false);
                    end_time.setChecked(false);

                    if(priority.contains(1)){
                        priority.remove(1);
                        radioButton1.setChecked(false);
                    }

                    if(priority.contains(2)){
                        priority.remove(2);
                        radioButton2.setChecked(false);
                    }

                    if(priority.contains(3)){
                        priority.remove(3);
                        radioButton3.setChecked(false);
                    }

                    if(priority.contains(4)){
                        priority.remove(4);
                        radioButton4.setChecked(false);
                    }

                    if(priority.contains(5)){
                        priority.remove(5);
                        radioButton5.setChecked(false);
                    }

                    btn.setEnabled(true);
                    x.setVisibility(View.GONE);
                    btn.setText("pick a time");

                    btn1.setEnabled(true);
                    x1.setVisibility(View.GONE);
                    btn1.setText("pick a time");

                    btn3.setEnabled(true);
                    x3.setVisibility(View.GONE);
                    btn3.setText("pick a time");



                    start.setChecked(true);
                    end.setChecked(true);
                    start_time.setChecked(true);
                    end_time.setChecked(true);

                    showToast("Все категории сброшены");
                    resetAll.setChecked(false);
                    lastCheckedId1 = -1;
                }
            }
        });


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

                if(priority.contains((1))){
                    filters.set(5,"1");
                }
                else{
                    filters.set(5,"0");
                }

                if(priority.contains((2))){
                    filters.set(6,"1");
                }else{
                    filters.set(6,"0");
                }

                if(priority.contains((3))){
                    filters.set(7,"1");
                }else{
                    filters.set(7,"0");
                }

                if(priority.contains((4))){
                    filters.set(8,"1");
                }else{
                    filters.set(8,"0");
                }

                if(priority.contains((5))){
                    filters.set(9,"1");
                }else{
                    filters.set(9,"0");
                }


                if(start.isChecked()){
                    filters.set(10, "1");
                }
                else{
                    filters.set(10, "0");
                }

                if(end.isChecked()){
                    filters.set(11, "1");
                }
                else{
                    filters.set(11, "0");
                }

                if(start_time.isChecked()){
                    filters.set(12, "1");
                }
                else{
                    filters.set(12, "0");
                }

                if(end_time.isChecked()){
                    filters.set(13, "1");
                }
                else{
                    filters.set(13, "0");
                }

                String strpat1 = btn.getText().toString();
                if (strpat1.equals("pick a time")) {
                    filters.set(14, "0");
                } else {
                    filters.set(14, strpat1);
                }

                String strpat2 = btn1.getText().toString();
                if (strpat2.equals("pick a time")) {
                    filters.set(15, "0");
                } else {
                    filters.set(15, strpat2);
                }

                String strpat3 = btn3.getText().toString();
                if (strpat3.equals("pick a time")) {
                    filters.set(16, "0");
                } else {
                    filters.set(16, strpat3);
                }

//                List<String> selectedCategoryNames = categoryAdapter.getSelectedCategories();
//
//                int j=17;
//                for (int i=0; i<selectedCategoryNames.size(); i++ ){
//                    if (filters.size() <= j) {
//                        // Если элемента нет, добавляем новый элемент
//                        filters.add(selectedCategoryNames.get(i));
//                    } else {
//                        // Если элемент существует, заменяем его
//                        filters.set(j, selectedCategoryNames.get(i));
//                    }
//                    j++;
//                }


                    myyDb.updateFilter(filters);
                dismiss();

            }
        });






        if(filters.get(0).equals("1")){
          odt.setChecked(true);
        }

        if(filters.get(1).equals("1")){
            routine.setChecked(true);
        }

        if(filters.get(2).equals("1")){
            temp.setChecked(true);
        }

        if(filters.get(3).equals("1")){
            compl.setChecked(true);
        }

        if(filters.get(4).equals("1")){
            uncompl.setChecked(true);
        }

        if(filters.get(5).equals("1")){
            radioButton1.setChecked(true);
            priority.add(1);
        }
        if(filters.get(6).equals("1")){
            radioButton2.setChecked(true);
            priority.add(2);
        }
        if(filters.get(7).equals("1")){
            radioButton3.setChecked(true);
            priority.add(3);
        }
        if(filters.get(8).equals("1")){
            radioButton4.setChecked(true);
            priority.add(4);
        }
        if(filters.get(9).equals("1")){
            radioButton5.setChecked(true);
            priority.add(5);
        }

        if(filters.get(10).equals("1")){
            start.setChecked(true);
        }

        if(filters.get(11).equals("1")){
            end.setChecked(true);
        }

        if(filters.get(12).equals("1")){
            start_time.setChecked(true);
        }

        if(filters.get(13).equals("1")){
            end_time.setChecked(true);
        }

        if(!filters.get(14).equals("0")){

            btn.setEnabled(false);
            x.setVisibility(View.VISIBLE);
            btn.setText(filters.get(14));

        }

        if(!filters.get(15).equals("0")){

            btn1.setEnabled(false);
            x1.setVisibility(View.VISIBLE);
            btn1.setText(filters.get(15));

        }

        if(!filters.get(16).equals("0")){

            btn3.setEnabled(false);
            x3.setVisibility(View.VISIBLE);
            btn3.setText(filters.get(16));

        }





//
//        if(start.isChecked()){
//            filters.set(10, "1");
//        }
//        else{
//            filters.set(10, "0");
//        }
//
//        if(end.isChecked()){
//            filters.set(11, "1");
//        }
//        else{
//            filters.set(11, "0");
//        }
//
//        if(start_time.isChecked()){
//            filters.set(12, "1");
//        }
//        else{
//            filters.set(12, "0");
//        }
//
//        if(end_time.isChecked()){
//            filters.set(13, "1");
//        }
//        else{
//            filters.set(13, "0");
//        }
//
//        String strpat1 = btn.getText().toString();
//        if (strpat1.equals("pick a time")) {
//            filters.set(14, "0");
//        } else {
//            filters.set(14, strpat1);
//        }
//
//        String strpat2 = btn1.getText().toString();
//        if (strpat1.equals("pick a time")) {
//            filters.set(15, "0");
//        } else {
//            filters.set(15, strpat2);
//        }
//
//        String strpat3 = btn3.getText().toString();
//        if (strpat1.equals("pick a time")) {
//            filters.set(16, "0");
//        } else {
//            filters.set(16, strpat3);
//        }


    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);


        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }

    private void updateAdapter() {
        // Получить текущий список категорий и обновить адаптер
        List<CategoryModel> updatedCategories = myyDb.getAllCategories();
        CategoryAdapter categoryAdapter = new CategoryAdapter(updatedCategories, myyDb);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }



}

