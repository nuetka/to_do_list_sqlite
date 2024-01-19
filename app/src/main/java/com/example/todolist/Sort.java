package com.example.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class Sort extends BottomSheetDialogFragment {
    public static final String TAG = "Sort";
    private DataBaseHelper myyyDb;
    private CheckBox u1_1, u1_2, u2_1, u2_2, u3_1, u3_2;

    List<String> sort = new ArrayList();


    public static Sort newInstance() {
        return new Sort();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sort, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myyyDb = new DataBaseHelper(getContext());

        sort= myyyDb.getSort();












        u1_1 = view.findViewById(R.id.u1_1);
        u1_2 = view.findViewById(R.id.u1_2);
        u2_1 = view.findViewById(R.id.u2_1);
        u2_2 = view.findViewById(R.id.u2_2);
//        u3_1 = view.findViewById(R.id.u3_1);
//        u3_2 = view.findViewById(R.id.u3_2);

        if(sort.get(0).equals("1")){
            u1_1.setChecked(true);
        }
        if(sort.get(0).equals("2")){
            u1_2.setChecked(true);
        }
        if(sort.get(1).equals("1")){
            u2_1.setChecked(true);
        }
        if(sort.get(1).equals("2")){
            u2_2.setChecked(true);
        }


        // Установка u1_1 отмеченным по умолчанию
//        u1_1.setChecked(true);

        // Слушатель для u1_1
        u1_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Если u1_1 выбран, снимаем выбор с u1_2
                    u1_2.setChecked(false);
                } else {
                    // Если u1_1 снят, обязательно выбираем u1_2
                    u1_2.setChecked(true);
                }
            }
        });

        // Слушатель для u1_2
        u1_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Если u1_2 выбран, снимаем выбор с u1_1
                    u1_1.setChecked(false);
                } else {
                    // Если u1_2 снят, обязательно выбираем u1_1
                    u1_1.setChecked(true);
                }
            }
        });


        u2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Если u1_2 выбран, снимаем выбор с u1_1
                    u2_2.setChecked(false);
                } else {

                }
            }
        });

        u2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Если u1_2 выбран, снимаем выбор с u1_1
                    u2_1.setChecked(false);
                } else {

                }
            }
        });

//        u3_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // Если u1_2 выбран, снимаем выбор с u1_1
//                    u3_2.setChecked(false);
//                } else {
//
//                }
//            }
//        });
//
//        u3_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // Если u1_2 выбран, снимаем выбор с u1_1
//                    u3_1.setChecked(false);
//                } else {
//
//                }
//            }
//        });


        Button mSaveButton = view.findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (u1_1.isChecked()){
                    sort.set(0, "1");
                }else if (u1_2.isChecked()){
                    sort.set(0, "2");
                }else {
                    sort.set(0, "0");
                }


                if (u2_1.isChecked()){
                    sort.set(1, "1");

                }else if (u2_2.isChecked()){
                    sort.set(1, "2");

                }else{
                    sort.set(1, "0");
                }


//                if (u3_1.isChecked()){
//                    sort.set(2, "1");
//
//                }else if (u3_2.isChecked()){
//                    sort.set(2, "2");
//
//                }



                myyyDb.updateSort(sort);
                dismiss();

            }
        });

        Button cb = view.findViewById(R.id.button_cancel);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

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
