package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.todolist.Utils.DataBaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.graphics.Color;

public class CircleChartDialog extends DialogFragment {

    private TextView startDateTextView;
    private TextView endDateTextView;

    private int completedTasks;
    private int totalTasks;
    private DataBaseHelper myDB;
    private PieChart pieChart; // Объявляем как поле класса

    public static CircleChartDialog newInstance() {
        return new CircleChartDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle_chart, container, false);
        pieChart = view.findViewById(R.id.pieChart);


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
        updateChart();


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

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(completedTasks, "Completed"));
        entries.add(new PieEntry(totalTasks - completedTasks, "Incomplete"));

        PieDataSet dataSet = new PieDataSet(entries, "Tasks Status");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setDrawEntryLabels(false);

        updateChart();
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
                    updateChart();
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

    private void updateChart() {
        // Получение данных и обновление круговой диаграммы на основе выбранного диапазона дат
//        long startDate = (Long) startDateTextView.getTag();
//        long endDate = (Long) endDateTextView.getTag();
//
//        String startDateStr = String.valueOf(startDate);
//        String endDateStr = String.valueOf(endDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        String startDateStr = dateFormat.format(new Date((Long) startDateTextView.getTag()));
        String endDateStr = dateFormat.format(new Date((Long) endDateTextView.getTag()));

        // Получение статистики задач за выбранный период
        int completedTasksInRange = myDB.getCompletedTasksCountInRange(startDateStr, endDateStr);
        int totalTasksInRange = myDB.getTotalTasksCountInRange(startDateStr, endDateStr);


        Log.e("ChartData", "Completed Tasks in Range: " + completedTasksInRange);
        Log.e("ChartData", "Total Tasks in Range: " + totalTasksInRange);
        Log.e("ChartData", "Date: " +  startDateStr);


        // Обновление данных в диаграмме
        updateChartData(completedTasksInRange, totalTasksInRange);
    }

    private void updateChartData(int completedTasks, int totalTasks) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(completedTasks, "Выполненные"));
        entries.add(new PieEntry(totalTasks - completedTasks, "Невыполненные"));

        PieDataSet dataSet = new PieDataSet(entries, "Статус задач");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
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
}