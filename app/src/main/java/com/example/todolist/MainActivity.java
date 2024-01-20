package com.example.todolist;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.todolist.Adapter.ToDoAdapter;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.Manifest;

public class MainActivity extends AppCompatActivity implements AddNewTask.OnDateRequestListener, OnDialogCloseListener {


    private RecyclerView mRecyclerview;
    private FloatingActionButton fab;
    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private ToDoAdapter adapter;


    View point1;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    TextView dateTextView;
    ImageView calendarImage;
    Calendar selectedDate = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    String strDate;
    ImageButton previousButton, filter;
    ImageButton nextButton,book;
    View point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)||(!Settings.canDrawOverlays(MainActivity.this))) {

            new AlertDialog.Builder(this)
                    .setTitle("Необходимы разрешения")
                    .setMessage("Необходимо разрешение на показ поверх других приложений и отправку уведомлений.")
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= 33) {
                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                } else {

                                }
                            }

// For Android 6.0 (M) and above: Checking overlay permission
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!Settings.canDrawOverlays(MainActivity.this)) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                    startActivityForResult(intent, 102); // Use startActivityForResult to handle the result
                                }
                                else{

                                }
                            }
//

                        }
                    })
                    .show();
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        ImageButton btnOpenDrawer = findViewById(R.id.btnOpenDrawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Обработка выбора пункта меню
                int id = item.getItemId();
                if (id == R.id.menu_item1) {

                    Sort filterDialog = Sort.newInstance();
                    filterDialog.show(getSupportFragmentManager(), Sort.TAG);
                    return true;
//                } else {
                    // Закрываем боковое меню после выбора пункта, кроме "Сортировка"
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                    return true;
                } else if (id == R.id.menu_item2) {
                    // Toggle between light and dark themes
                    int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                    if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                        // Switch to dark theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        // Switch to light theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }

                    // Recreate the activity to apply the theme change
                    recreate();

                    return true;
                }

                if (id == R.id.menu_item5) {
//                    int completedTasks = myDB.getCompletedTasksCount();
//                    int totalTasks = myDB.getTotalTasksCount();

                    CircleChartDialog chartDialog = CircleChartDialog.newInstance();
                    chartDialog.show(getSupportFragmentManager(), "circleChartDialog");
                    return true;
                }

                if (id == R.id.menu_item6) {
                    Excel chartDialog = Excel.newInstance();
                    chartDialog.show(getSupportFragmentManager(), "excel");


                    return true;
                }
                if (id == R.id.menu_item3) {

                    String should = myDB.getDayBeforTasks();
                    if(should.equals("0")) {
                        // Create an AlertDialog
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Перенос задач")
                                .setMessage("Вы хотите включить перенос невыполненных задач? (Перенос осуществляется на один день)")
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        myDB.updateTasksDayBefor("1");

                                    }
                                })
                                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User clicked the Cancel button
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }else {
                        // Create an AlertDialog
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Перенос задач")
                                .setMessage("Вы хотите выключить перенос невыполненных задач?")
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        myDB.updateTasksDayBefor("0");

                                    }
                                })
                                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User clicked the Cancel button
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                    return true;
                }else if (id == R.id.menu_item7) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Полная очистка приложения")
                            .setMessage("Вы действительно хотите очистить все данные ?")
                            .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   myDB.resetDatabase();
                                    mList = null;
                                    adapter.setTasks(mList);


                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked the Cancel button
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

                return false;
            }
        });






    dateTextView = (TextView) findViewById(R.id.textview);
        previousButton = (ImageButton) findViewById(R.id.previousButton);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        filter = (ImageButton) findViewById(R.id.filter);
        calendarImage = (ImageView) findViewById(R.id.calendarImage);

        book = (ImageButton) findViewById(R.id.book);
        point = (View) findViewById(R.id.point);

        myDB = new DataBaseHelper(MainActivity.this);


        // установка текущей даты
        strDate = sdf.format(new Date());
        dateTextView.setText(strDate);

        point1 = (View) findViewById(R.id.point1);
        if(myDB.isAnySelected()==true){
            point1.setVisibility(View.VISIBLE);
        }else{
            point1.setVisibility(View.GONE);
        }


        String sis = myDB.getNoteTextByDate(dateTextView.getText().toString());
        if (sis != null && (!sis.equals(""))) {
            point.setVisibility(View.VISIBLE);
        }else{
            point.setVisibility(View.GONE);
        }

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate.add(Calendar.DAY_OF_MONTH, -1);
                updateDateTextView();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate.add(Calendar.DAY_OF_MONTH, 1);
                updateDateTextView();
            }
        });

        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mRecyclerview = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB, MainActivity.this);

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);

        mList = myDB.getAllTasks(strDate, true);
//        Collections.reverse(mList);


        adapter.setTasks(mList);

        try {


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);
                }
            });
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
            itemTouchHelper.attachToRecyclerView(mRecyclerview);

            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AddNewTask dialog = AddNewTask.newInstance();
                        Bundle args = new Bundle();
                        args.putString("source", "book");
                        dialog.setArguments(args);
                        dialog.show(getSupportFragmentManager(), AddNewTask.TAG);
                    } catch (Exception e) {

                    }
                }
            });

            ItemTouchHelper itemTouchHelperForBook = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
            itemTouchHelperForBook.attachToRecyclerView(mRecyclerview);
        }catch(Exception e){
            e.printStackTrace();

        }

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Filter dialog = Filter.newInstance();
                    dialog.show(getSupportFragmentManager(), Filter.TAG);

            }


        });

        ItemTouchHelper itemTouchHelperForBook = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelperForBook.attachToRecyclerView(mRecyclerview);

        }

    private void updateDateTextView() {
        strDate = sdf.format(selectedDate.getTime());
        dateTextView.setText(strDate);

        // Check if the selected date is today and set the color of the text accordingly
        Date today = new Date();
        if(sdf.format(today).equals(strDate)){
            dateTextView.setTextColor(Color.BLACK);
        } else {
            dateTextView.setTextColor(Color.GRAY);
        }

        String sis= myDB.getNoteTextByDate(dateTextView.getText().toString());
        if(sis!=null && (!sis.equals("")) ){
            point.setVisibility(View.VISIBLE);
        }
        else{
            point.setVisibility(View.GONE);
        }
        refresh();
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, monthOfYear);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            strDate = sdf.format(selectedDate.getTime());
            dateTextView.setText(strDate);

            // Check if the selected date is today and set the color of the text accordingly
            Date today = new Date();
            if(sdf.format(today).equals(strDate)){
                dateTextView.setTextColor(Color.BLACK);
            } else {
                dateTextView.setTextColor(Color.GRAY);
            }
            refresh();
        }
    };


    public void refresh(){
        mList = myDB.getAllTasks(strDate, true);
//        Collections.reverse(mList);
        adapter.setTasks(mList);
        String sis = myDB.getNoteTextByDate(dateTextView.getText().toString());
        if (sis != null && (!sis.equals(""))) {
            point.setVisibility(View.VISIBLE);
        }else{
            point.setVisibility(View.GONE);
        }

        if(myDB.isAnySelected()==true){
            point1.setVisibility(View.VISIBLE);
        }else{
            point1.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks(strDate, true);
//        Collections.reverse(mList);
        String sis = myDB.getNoteTextByDate(dateTextView.getText().toString());
        if (sis != null && (!sis.equals(""))) {
            point.setVisibility(View.VISIBLE);
        }
        else{
            point.setVisibility(View.GONE);
        }
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public String onRequestDate() {
        return dateTextView.getText().toString();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"NotificationPermission", "MissingPermission"})
    public void createFileReadyNotification(String title, String description) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Создание канала уведомлений для Android O и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_CHANNEL_DESCRIPTION");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
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


