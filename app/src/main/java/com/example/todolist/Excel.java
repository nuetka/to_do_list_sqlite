package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Excel extends DialogFragment {

    private TextView startDateTextView;
    private TextView endDateTextView;
    private Button ok;

    private DataBaseHelper myDB;


    public static Excel newInstance() {
        return new Excel();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.excel, container, false);
        ok = view.findViewById(R.id.okButton);



// Inside your onCreate or constructor, initialize myDB
        myDB = new DataBaseHelper(getContext());
//        pieChart = view.findViewById(R.id.pieChart);

        startDateTextView = view.findViewById(R.id.startDateTextView);
        endDateTextView = view.findViewById(R.id.endDateTextView);

        // Получение текущей даты
        Calendar calendar = Calendar.getInstance();

        // Установка тегов для начальной и конечной даты
        long currentDateInMillis = calendar.getTimeInMillis();
        startDateTextView.setTag(currentDateInMillis);
        endDateTextView.setTag(currentDateInMillis);

        // Установка текста дат и обновление графика
        updateDateText(currentDateInMillis, currentDateInMillis);



        // Initialize start date selection button
        Button selectStartButton = view.findViewById(R.id.selectStartButton);
        selectStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true); // true for start date
            }
        });


        // Initialize end date selection button
        Button selectEndButton = view.findViewById(R.id.selectEndButton);
        selectEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false); // false for end date
            }
        });

        // Установка начальных дат
        updateDateText(System.currentTimeMillis(), System.currentTimeMillis());


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input1 = startDateTextView.getText().toString();
                String input2 = endDateTextView.getText().toString();

                String date1 = input1.replace("Начальная дата: ", "");
                String date2 = input2.replace("Конечная дата: ", "");

                //привести из формата с . к формату с -

                // Преобразование строк в объекты LocalDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate startDate = LocalDate.parse(date1, formatter);
                LocalDate endDate = LocalDate.parse(date2, formatter);

                // Изменение формата дат
                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String newStartDateStr = startDate.format(newFormatter);
                String newEndDateStr = endDate.format(newFormatter);

                // Создание списка дат
                List<String> dateRange = new ArrayList<>();
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    dateRange.add(date.format(newFormatter));
                }
                List<ToDoModel> tasks = new ArrayList<>();

                for(String d: dateRange) {

                    List<ToDoModel> t = myDB.getAllTasks(d, false);
                    tasks.addAll(t); // Добавляем все задачи из t в tasks
                }

                // Путь к папке "Загрузки"
                File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String fileName = "ваш_файл.xlsx";
                String filePath = new File(downloadsFolder, fileName).getAbsolutePath();

                // Вызовите метод для сохранения задач в Excel
                ExcelExporter.exportTasksToExcel(tasks, filePath);

                // Отображение уведомления о завершении
                Log.d("Notification", "File saved at: " + filePath);
                createFileReadyNotification(fileName, "Файл успешно сохранен в папке 'Загрузки'");





            }
        });


        return view;
    }

    private void showDatePickerDialog(final boolean isStartDate) {
        DatePickerFragment datePickerFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                if (isStartDate) {
                    // Set the start date
                    startDateTextView.setTag(calendar.getTimeInMillis());
                    updateDateText(calendar.getTimeInMillis(), endDateTextView.getTag() != null ? (Long) endDateTextView.getTag() : calendar.getTimeInMillis());
                } else {
                    // Set the end date
                    endDateTextView.setTag(calendar.getTimeInMillis());
                    updateDateText(startDateTextView.getTag() != null ? (Long) startDateTextView.getTag() : calendar.getTimeInMillis(), calendar.getTimeInMillis());
                }

                // Update chart if both dates are selected
                if (startDateTextView.getTag() != null && endDateTextView.getTag() != null) {
                   ok.setVisibility(View.VISIBLE);
                }
            }
        });
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
    private void updateDateText(long startDate, long endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

        startDateTextView.setText("Начальная дата: " + dateFormat.format(startDate));
        endDateTextView.setText("Конечная дата: " + dateFormat.format(endDate));
    }



    public static class DatePickerFragment extends DialogFragment {

        private final DatePickerDialog.OnDateSetListener listener;

        public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }
    }

    @SuppressLint({"NotificationPermission", "MissingPermission"})
    public void createFileReadyNotification(String title, String description) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

        // Создание канала уведомлений для Android O и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_CHANNEL_DESCRIPTION");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "YOUR_CHANNEL_ID")
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_file_download) // Замените на вашу иконку
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Используйте статическую переменную для генерации уникального идентификатора
        int notificationId = generateUniqueId();
        notificationManager.notify(notificationId, builder.build());
    }

    // Метод для генерации уникального идентификатора уведомления
    private static int uniqueId = 0;

    private int generateUniqueId() {
        return uniqueId++;
    }

}
