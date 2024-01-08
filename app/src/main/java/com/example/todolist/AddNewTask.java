package com.example.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.todolist.Model.CategoryModel;
import com.example.todolist.Model.NoteModel;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";
    //widgets
    private EditText mEditText;
    private Button mSaveButton;
    private Button CancelButton;
    private Spinner categorySpinner;
    private ArrayAdapter<CategoryModel> adapter;
    private List<CategoryModel> categories;

    private int lastCheckedId = -1;
    private int is6Checked = 0;

    private DataBaseHelper myDb;
    private OnDateRequestListener dateRequestListener;

    private boolean isNewCategoryAdded = false;
    private boolean isMustEdit= false;

    private int priority;//is6Checjes заместо isRpotine
    private int snoty, enoty;



    private CheckBox checkBoxMonday;
    private CheckBox checkBoxTuesday;
    private CheckBox checkBoxWednesday;
    private CheckBox checkBoxThursday;
    private CheckBox checkBoxFriday;
    private CheckBox checkBoxSaturday;
    private CheckBox checkBoxSunday;

    private boolean notemode = false;
    private Bundle args;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask , container , false);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDb = new DataBaseHelper(getContext());
        TextView addTaskTab = view.findViewById(R.id.imagesTextView);
        TextView addNoteTab = view.findViewById(R.id.videosTextView);
        final TextView priorityText = view.findViewById(R.id.textview);

        final RelativeLayout relativeLayout = view.findViewById(R.id.loy1);
        final LinearLayout linearLayout = view.findViewById(R.id.linear);
        final LinearLayout linearForever = view.findViewById(R.id.linearForever);
        final LinearLayout linLay1 = view.findViewById(R.id.linearLayout1);
        final LinearLayout linLay2 = view.findViewById(R.id.linearLayout2);
        final LinearLayout linLay3 = view.findViewById(R.id.linearLayout3);
        final LinearLayout linLay4 = view.findViewById(R.id.linearLayout4);

        final View eventUnder = view.findViewById(R.id.videosUnderline);
        final TextView taskTextView = view.findViewById(R.id.imagesTextView);
        final View taskUnder = view.findViewById(R.id.imagesUnderline);

        final ImageView x = view.findViewById(R.id.myImageView);
        final ImageView x1 = view.findViewById(R.id.myImageView1);


        final Button btn = view.findViewById(R.id.from1);

        final Button btn1 = view.findViewById(R.id.from2);

        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        final RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
        final RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
        final RadioButton radioButton3 = view.findViewById(R.id.radioButton3);
        final RadioButton radioButton4 = view.findViewById(R.id.radioButton4);
        final RadioButton radioButton5 = view.findViewById(R.id.radioButton5);

        final RadioButton radioButton6 = view.findViewById(R.id.radioButton6);

        lastCheckedId = radioGroup.getCheckedRadioButtonId(); // Устанавливает последний выбранный RadioButton

        String dateText = dateRequestListener.onRequestDate();
        String sis = null;
        sis=myDb.getNoteTextByDate(dateText);
        final TextView eventTextView = view.findViewById(R.id.videosTextView);
        Bundle args = getArguments();

        try {

            args = getArguments();
            if (args != null) {
                String source = args.getString("source");
                if ("book".equals(source)) {
                    // book была нажата
                    // Скрыть текстовое представление приоритета и группу радиокнопок, когда пользователь выбирает "добавление события"
                    priorityText.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                    linLay1.setVisibility(View.GONE);
                    linLay2.setVisibility(View.GONE);
                    linLay3.setVisibility(View.GONE);
                    linLay4.setVisibility(View.GONE);
                    eventUnder.setBackgroundColor(Color.BLACK);
                    taskTextView.setTextColor(Color.GRAY);
                    taskUnder.setBackgroundColor(Color.GRAY);
                    if (sis == null || sis.isEmpty()) {
                        mEditText.setHint("Enter a note");
                    } else {
                        mEditText.setText(sis);
                    }
                    notemode = true;

                }
            }
        }catch(Exception e){

        }


        mEditText = view.findViewById(R.id.edittext);

        mSaveButton = view.findViewById(R.id.button_save);
        CancelButton = view.findViewById(R.id.button_cancel);


        if(sis!=null && (!sis.equals("")) ){
            eventTextView.setText("edit note");
        }

        categorySpinner = view.findViewById(R.id.categorySpinner);
        updateCategorySpinner();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryModel selectedCategory = (CategoryModel) parent.getItemAtPosition(position);
                if (selectedCategory.getId() == -1) {
                    if (!isNewCategoryAdded) {
                        showAddCategoryDialog();
                        isMustEdit=false;
                    }
                } else if (isMustEdit && isNewCategoryAdded && categories.get(0).getId()==selectedCategory.getId()){
                    showEditCategoryDialog(selectedCategory);
                    isMustEdit=false;
                }
                else {
                    isMustEdit=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });




        CheckBox checkBox = view.findViewById(R.id.notificationCheckbox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    snoty=1;
                } else {
                    snoty=0;

                }
            }
        });

        CheckBox checkBox1 = view.findViewById(R.id.notificationCheckbox1);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enoty=1;
                } else {
                   enoty=0;
                }
            }
        });

        //разбираюсь с перелистыванием с доб задачу на событие

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

                                // Вычисление разницы во времени теперь может быть выполнено здесь.
                                calculateTimeDifference();
                            }
                        }, hour, minute, DateFormat.is24HourFormat(getActivity()));

                timePickerDialog.show();
            }

            private void calculateTimeDifference() {
                if(x.getVisibility()==View.VISIBLE && x1.getVisibility()==View.VISIBLE) {
                    EditText editText = view.findViewById(R.id.h);
                    EditText editText1 = view.findViewById(R.id.m);

                    LocalTime time1 = LocalTime.of(0, 0);
                    LocalTime time2 = LocalTime.of(0, 0);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

                    try {
                        time1 = LocalTime.parse(btn.getText().toString(), formatter);
                        time2 = LocalTime.parse(btn1.getText().toString(), formatter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    long hoursBetween = 0;
                    long minutesBetween = 0;

                    long totalMinutesBetween = ChronoUnit.MINUTES.between(time1, time2);
                    hoursBetween = totalMinutesBetween / 60;
                    minutesBetween = totalMinutesBetween % 60;

                    editText.setText(String.format("%02d", hoursBetween));
                    editText1.setText(String.format("%02d", minutesBetween));
                }
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

                                // Вычисление разницы во времени теперь может быть выполнено здесь.
                                calculateTimeDifference();
                            }
                        }, hour, minute, DateFormat.is24HourFormat(getActivity()));

                timePickerDialog.show();
            }

            private void calculateTimeDifference() {
                if(x.getVisibility()==View.VISIBLE && x1.getVisibility()==View.VISIBLE) {
                    EditText editText = view.findViewById(R.id.h);
                    EditText editText1 = view.findViewById(R.id.m);

                    LocalTime time1 = LocalTime.of(0, 0);
                    LocalTime time2 = LocalTime.of(0, 0);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

                    try {
                        time1 = LocalTime.parse(btn.getText().toString(), formatter);
                        time2 = LocalTime.parse(btn1.getText().toString(), formatter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    long hoursBetween = 0;
                    long minutesBetween = 0;

                    long totalMinutesBetween = ChronoUnit.MINUTES.between(time1, time2);
                    hoursBetween = totalMinutesBetween / 60;
                    minutesBetween = totalMinutesBetween % 60;

                    editText.setText(String.format("%02d", hoursBetween));
                    editText1.setText(String.format("%02d", minutesBetween));
                }
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


        final Button btn_from = view.findViewById(R.id.forever);
        final ImageView x3 = view.findViewById(R.id.mIV);

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
                btn_from.setText("forever");
            }


        });




        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton1) {
                    radioButton1.setChecked(false);
                    lastCheckedId = -1;
                    priority=0;
                } else {
                    lastCheckedId = R.id.radioButton1;
                    priority=1;
                }
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton2) {
                    radioButton2.setChecked(false);
                    lastCheckedId = -1;
                    priority=0;
                } else {
                    lastCheckedId = R.id.radioButton2;
                    priority=2;
                }
            }
        });

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton3) {
                    radioButton3.setChecked(false);
                    lastCheckedId = -1;
                    priority=0;
                } else {
                    lastCheckedId = R.id.radioButton3;
                    priority=3;
                }
            }
        });


        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton4) {
                    radioButton4.setChecked(false);
                    lastCheckedId = -1;
                    priority=0;
                } else {
                    lastCheckedId = R.id.radioButton4;
                    priority=4;
                }
            }
        });


        radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedId == R.id.radioButton5) {
                    radioButton5.setChecked(false);
                    lastCheckedId = -1;
                    priority=0;
                } else {
                    lastCheckedId = R.id.radioButton5;
                    priority=5;
                }
            }
        });

        radioButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is6Checked==1) {
                    radioButton6.setChecked(false);
                    linearLayout.setVisibility(View.GONE);
                    linearForever.setVisibility(View.GONE);
                    is6Checked=0;
                } else {
                    radioButton6.setChecked(true);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearForever.setVisibility(View.VISIBLE);
                    is6Checked=1;
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
                mEditText.setHint("Enter Description");
                linLay2.setVisibility(View.VISIBLE);
                linLay3.setVisibility(View.VISIBLE);
                linLay4.setVisibility(View.VISIBLE);
                eventTextView.setTextColor(Color.GRAY);
                eventUnder.setBackgroundColor(Color.GRAY);
                taskTextView.setTextColor(Color.BLACK);
                taskUnder.setBackgroundColor(Color.BLACK);
                notemode=false;
            }
        });

        addNoteTab.setOnClickListener(new View.OnClickListener() {
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
                String dateText;
                dateText = dateRequestListener.onRequestDate();
                String sis = null;
                sis = myDb.getNoteTextByDate(dateText);

                    if (sis==null || sis.isEmpty()) {
                        mEditText.setHint("Enter a note");
//                        mEditText.setTextColor(Color.GRAY);
                    } else {
                        mEditText.setText(sis);
                    }
                notemode=true;

            }
        });

        //уже другое

        boolean isUpdate = false;

        if (args.size()>1){
            isUpdate = true;
            String task = args.getString("task");

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

        Button myButtonnn = view.findViewById(R.id.forever);
        final Bundle finalArgs = args;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();

                if(notemode){
                    NoteModel item = new NoteModel();
                    String dateText;
                    dateText = dateRequestListener.onRequestDate();
                    if(myDb.getNoteTextByDate(dateText)!=null) {

                        item.setDate(dateText);

                        item.setDescription(text);

                        myDb.insertNote(item);
                    }
                    else{
                        myDb.updateNoteByDate(dateText,text);
                    }

                }else{

                if (finalIsUpdate){
                    myDb.updateTask(finalArgs.getInt("id") , text);
                }else {
                    ToDoModel item = new ToDoModel();
                    if (is6Checked == 1) {
                        item.setIsRoutine(1);
                        String repeatDays =
                                (checkBoxMonday.isChecked() ? "1" : "0") +
                                        (checkBoxTuesday.isChecked() ? "1" : "0") +
                                        (checkBoxWednesday.isChecked() ? "1" : "0") +
                                        (checkBoxThursday.isChecked() ? "1" : "0") +
                                        (checkBoxFriday.isChecked() ? "1" : "0") +
                                        (checkBoxSaturday.isChecked() ? "1" : "0") +
                                        (checkBoxSunday.isChecked() ? "1" : "0");

                        item.setRepeatDays(repeatDays);

                        String buttonForeverText = myButtonnn.getText().toString();
                        if (buttonForeverText.equals("forever")) {
                            item.setRepeatEndDate("0");
                        } else {
                            item.setRepeatEndDate(buttonForeverText);
                        }


                    } else {
                        item.setIsRoutine(0);
                    }
                    item.setTask(text);
                    item.setStatus(0);

                    String dateText;
                    dateText = dateRequestListener.onRequestDate();
                    item.setDate(dateText);

                    item.setPriority(priority);
                    item.setSnoty(snoty);
                    item.setEnoty(enoty);

                    Button pat1 = view.findViewById(R.id.from1);
                    String strpat1 = pat1.getText().toString();
                    if (strpat1.equals("pick a time")) {
                        item.setStart("0");
                    } else {
                        item.setStart(strpat1);
                    }


                    Button pat2 = view.findViewById(R.id.from2);
                    String strpat2 = pat2.getText().toString();
                    if (strpat2.equals("pick a time")) {
                        item.setEnd("0");
                    } else {
                        item.setEnd(strpat1);
                    }

                    if (x.getVisibility() == View.VISIBLE) {

                        String txt = (String) btn.getText();
                        item.setStart(txt);
                        // editText.setText(String.valueOf(yourValue));
                    } else {
                        item.setStart("0");
                    }

                    if (x1.getVisibility() == View.VISIBLE) {

                        String txt = (String) btn1.getText();
                        item.setStart(txt);
                    } else {
                        item.setStart("0");
                    }

                    EditText editText = view.findViewById(R.id.h);
                    EditText editText1 = view.findViewById(R.id.m);
                    Log.e(TAG, "HOURS!" + editText.getText().toString());

                    if (!editText.getText().toString().equals("hours")) {
                        item.setDuration(editText.getText().toString() + ":" + editText1.getText().toString());
                    } else {
                        item.setDuration("0");
                    }


                    myDb.insertTask(item);
                }

                }
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

             checkBoxMonday = view.findViewById(R.id.checkbox_monday);
             checkBoxTuesday = view.findViewById(R.id.checkbox_tuesday);
             checkBoxWednesday = view.findViewById(R.id.checkbox_wednesday);
             checkBoxThursday = view.findViewById(R.id.checkbox_thursday);
             checkBoxFriday = view.findViewById(R.id.checkbox_friday);
             checkBoxSaturday = view.findViewById(R.id.checkbox_saturday);
             checkBoxSunday = view.findViewById(R.id.checkbox_sunday);




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



    public void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Добавить категорию");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newCategoryName = input.getText().toString();
            if (!newCategoryName.isEmpty()) {
                myDb.insertCategory(newCategoryName);
                isNewCategoryAdded = true;
                updateCategorySpinner();
            }
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public class CategoryAdapter extends ArrayAdapter<CategoryModel> {
        private boolean isDropdownOpened = false;

        public CategoryAdapter(Context context, List<CategoryModel> categories) {
            super(context, android.R.layout.simple_spinner_item, categories);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return initItemView(position, convertView, parent, false);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            isDropdownOpened = true;
            return initItemView(position, convertView, parent, true);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            isDropdownOpened = false;
        }

        private View initItemView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }
            TextView textView = (TextView) convertView;
            CategoryModel item = getItem(position);
            if (item != null) {
                String text = item.getName();
                if (isDropDown && position == 0 && isNewCategoryAdded) {
                    text += "\uD83D\uDD8D"; // Добавляем карандаш, если это первая категория в выпадающем списке
                }
                textView.setText(text);
            }
            return convertView;
        }
    }

    private void updateCategorySpinner() {
        categories = myDb.getAllCategories(getContext());

        if (isNewCategoryAdded) {
            // Сортируем список CategoryModel по убыванию id
            Collections.sort(categories, new Comparator<CategoryModel>() {
                @Override
                public int compare(CategoryModel category1, CategoryModel category2) {
                    return Integer.compare(category2.getId(), category1.getId());
                }
            });
        } else {
            // Добавляем пустую категорию
            CategoryModel emptyCategory = new CategoryModel();
            emptyCategory.setName("+");
            emptyCategory.setId(-1);
            categories.add(emptyCategory);
        }

        // Используем пользовательский адаптер
        CategoryAdapter customAdapter = new CategoryAdapter(getActivity(), categories);
        categorySpinner.setAdapter(customAdapter);
    }
    private void showEditCategoryDialog(CategoryModel category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Редактировать категорию");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(category.getName());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String categoryName = input.getText().toString();
            if (!categoryName.isEmpty()) {
                myDb.updateCategoryName(category.getId(), categoryName);
                updateCategorySpinner();
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.show();
    }




}