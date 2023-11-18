package com.example.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    //widgets
    private EditText mEditText;
    private Button mSaveButton;

    private int lastCheckedId = -1;

    private int lastCheckedId1 = -1;

    private DataBaseHelper myDb;
    private OnDateRequestListener dateRequestListener;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask , container , false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditText = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);
        myDb = new DataBaseHelper(getActivity());

        //разбираюсь с перелистыванием с доб задачу на событие
        TextView addTaskTab = view.findViewById(R.id.imagesTextView);
        TextView addEventTab = view.findViewById(R.id.videosTextView);
        final TextView priorityText = view.findViewById(R.id.textview);

        final RelativeLayout relativeLayout = view.findViewById(R.id.loy1);
        final LinearLayout linearLayout = view.findViewById(R.id.linear);
        final LinearLayout linLay = view.findViewById(R.id.linearLayout);
        final LinearLayout linLay1 = view.findViewById(R.id.linearLayout1);
        final LinearLayout linLay2 = view.findViewById(R.id.linearLayout2);
        final LinearLayout linLay3 = view.findViewById(R.id.linearLayout3);
        final LinearLayout linLay4 = view.findViewById(R.id.linearLayout4);

        final TextView eventTextView = view.findViewById(R.id.videosTextView);
        final View eventUnder = view.findViewById(R.id.videosUnderline);
        final TextView taskTextView = view.findViewById(R.id.imagesTextView);
        final View taskUnder = view.findViewById(R.id.imagesUnderline);

        final ImageView x = view.findViewById(R.id.myImageView);
        final ImageView x1 = view.findViewById(R.id.myImageView1);


        final Button btn = view.findViewById(R.id.from1);
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

        final Button btn1 = view.findViewById(R.id.from2);
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


        final Button btn_from = view.findViewById(R.id.from);
        final ImageView x3 = view.findViewById(R.id.myImageView3);

        btn_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Получение текущей даты.
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Создание нового экземпляра DatePickerDialog и отображение его.
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Установка выбранной пользователем даты на кнопку.
                                btn_from.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                btn_from.setEnabled(false);
                                x3.setVisibility(View.VISIBLE);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_from.setEnabled(true);
                x3.setVisibility(View.GONE);
                btn_from.setText("pick a time");
            }


        });

        final Button btn_to = view.findViewById(R.id.to);
        final ImageView x4 = view.findViewById(R.id.myImageView4);

        btn_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Получение текущей даты.
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Создание нового экземпляра DatePickerDialog и отображение его.
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Установка выбранной пользователем даты на кнопку.
                                btn_to.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                btn_to.setEnabled(false);
                                x4.setVisibility(View.VISIBLE);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_to.setEnabled(true);
                x4.setVisibility(View.GONE);
                btn_to.setText("pick a time");
            }


        });



        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        final RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
        final RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
        final RadioButton radioButton3 = view.findViewById(R.id.radioButton3);
        final RadioButton radioButton4 = view.findViewById(R.id.radioButton4);
        final RadioButton radioButton5 = view.findViewById(R.id.radioButton5);

        final RadioGroup radioGroup1 = view.findViewById(R.id.radioGroup1);
        final RadioButton radioButton6 = view.findViewById(R.id.radioButton6);
        final RadioButton radioButton7 = view.findViewById(R.id.radioButton7);

        lastCheckedId = radioGroup.getCheckedRadioButtonId(); // Устанавливает последний выбранный RadioButton

        lastCheckedId1 = radioGroup1.getCheckedRadioButtonId(); // Устанавливает последний выбранный RadioButton

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton1) {
                    radioButton1.setChecked(false);
                    lastCheckedId = -1;
                } else {
                    lastCheckedId = R.id.radioButton1;
                }
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton2) {
                    radioButton2.setChecked(false);
                    lastCheckedId = -1;
                } else {
                    lastCheckedId = R.id.radioButton2;
                }
            }
        });

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton3) {
                    radioButton3.setChecked(false);
                    lastCheckedId = -1;
                } else {
                    lastCheckedId = R.id.radioButton3;
                }
            }
        });


        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton4) {
                    radioButton4.setChecked(false);
                    lastCheckedId = -1;
                } else {
                    lastCheckedId = R.id.radioButton4;
                }
            }
        });


        radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton5) {
                    radioButton5.setChecked(false);
                    lastCheckedId = -1;
                } else {
                    lastCheckedId = R.id.radioButton5;
                }
            }
        });

        radioButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId1 == R.id.radioButton6) {
                    radioButton6.setChecked(false);
                    linearLayout.setVisibility(View.GONE);
                    radioButton7.setVisibility(View.VISIBLE);
                    lastCheckedId1 = -1;
                } else {
                    lastCheckedId1 = R.id.radioButton6;
                    linearLayout.setVisibility(View.VISIBLE);
                    radioButton7.setVisibility(View.GONE);
                }
            }
        });

        radioButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId1 == R.id.radioButton7) {
                    radioButton7.setChecked(false);
                    linLay.setVisibility(View.GONE);
                    radioButton6.setVisibility(View.VISIBLE);
                    lastCheckedId1 = -1;
                } else {
                    lastCheckedId1 = R.id.radioButton7;
                    linLay.setVisibility(View.VISIBLE);
                    radioButton6.setVisibility(View.GONE);
                }
            }
        });





        addTaskTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Показать текстовое представление приоритета и группу радиокнопок, когда пользователь выбирает "добавление задачи"
                priorityText.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                linLay1.setVisibility(View.VISIBLE);
                linLay2.setVisibility(View.VISIBLE);
                linLay3.setVisibility(View.VISIBLE);
                linLay4.setVisibility(View.VISIBLE);
                eventTextView.setTextColor(Color.GRAY);
                eventUnder.setBackgroundColor(Color.GRAY);
                taskTextView.setTextColor(Color.BLACK);
                taskUnder.setBackgroundColor(Color.BLACK);
            }
        });

        addEventTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Скрыть текстовое представление приоритета и группу радиокнопок, когда пользователь выбирает "добавление события"
                priorityText.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                linLay1.setVisibility(View.GONE);
                linLay2.setVisibility(View.GONE);
                linLay3.setVisibility(View.GONE);
                linLay4.setVisibility(View.GONE);
                eventTextView.setTextColor(Color.BLACK);
                eventUnder.setBackgroundColor(Color.BLACK);
                taskTextView.setTextColor(Color.GRAY);
                taskUnder.setBackgroundColor(Color.GRAY);
            }
        });

        //уже другое

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            if (task.length() > 0 ){
                mSaveButton.setEnabled(false);
            }

        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                }else{
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();

                if (finalIsUpdate){
                    myDb.updateTask(bundle.getInt("id") , text);
                }else{
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);

                    String dateText;
                    dateText = dateRequestListener.onRequestDate();
                    item.setDate(dateText);


                    myDb.insertTask(item);

                }
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
    public interface OnDateRequestListener {
        String onRequestDate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dateRequestListener = (OnDateRequestListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDateRequestListener");
        }
    }
}