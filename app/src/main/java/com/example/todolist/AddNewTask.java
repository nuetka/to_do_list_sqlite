package com.example.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.Model.CategoryModel;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";
    //widgets
    private EditText mEditText;
    private Button mSaveButton;
    private Spinner categorySpinner;
    private ArrayAdapter<CategoryModel> adapter;
    private List<CategoryModel> categories;

    private int lastCheckedId = -1;
    private int is6Checked = 0;

    private DataBaseHelper myDb;
    private OnDateRequestListener dateRequestListener;

    private boolean isNewCategoryAdded = false;
    private boolean isMustEdit= false;

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



        //разбираюсь с перелистыванием с доб задачу на событие
        TextView addTaskTab = view.findViewById(R.id.imagesTextView);
        TextView addEventTab = view.findViewById(R.id.videosTextView);
        final TextView priorityText = view.findViewById(R.id.textview);

        final RelativeLayout relativeLayout = view.findViewById(R.id.loy1);
        final LinearLayout linearLayout = view.findViewById(R.id.linear);
        final LinearLayout linearForever = view.findViewById(R.id.linearForever);
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


        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        final RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
        final RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
        final RadioButton radioButton3 = view.findViewById(R.id.radioButton3);
        final RadioButton radioButton4 = view.findViewById(R.id.radioButton4);
        final RadioButton radioButton5 = view.findViewById(R.id.radioButton5);

        final RadioButton radioButton6 = view.findViewById(R.id.radioButton6);

        lastCheckedId = radioGroup.getCheckedRadioButtonId(); // Устанавливает последний выбранный RadioButton

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