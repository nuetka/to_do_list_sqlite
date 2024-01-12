package com.example.todolist;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AddNewTask.OnDateRequestListener, OnDialogCloseListener {

    private RecyclerView mRecyclerview;
    private FloatingActionButton fab;
    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//            // Предложите пользователю включить разрешение
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + this.getPackageName()));
//            this.startActivity(intent);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.getPackageName()));
            this.startActivity(intent);
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
                // Можете добавить свой код обработки событий здесь

                // Закрываем боковое меню после выбора пункта
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
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

        String sis = myDB.getNoteTextByDate(dateTextView.getText().toString());
        if (sis != null && (!sis.equals(""))) {
            point.setVisibility(View.VISIBLE);
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

        mList = myDB.getAllTasks(strDate);
        Collections.reverse(mList);


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
                    dialog.show(getSupportFragmentManager(), AddNewTask.TAG);
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
        mList = myDB.getAllTasks(strDate);
        Collections.reverse(mList);
        adapter.setTasks(mList);
        String sis = myDB.getNoteTextByDate(dateTextView.getText().toString());
        if (sis != null && (!sis.equals(""))) {
            point.setVisibility(View.VISIBLE);
        }else{
            point.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks(strDate);
        Collections.reverse(mList);
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

}