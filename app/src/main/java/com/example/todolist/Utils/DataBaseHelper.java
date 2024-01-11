package com.example.todolist.Utils;

import static com.example.todolist.AddNewTask.TAG;
import static com.example.todolist.AddNewTask.newInstance;

import com.example.todolist.AlarmActivity;
import com.example.todolist.MainActivity;
import com.example.todolist.R;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.todolist.Model.CategoryModel;
import com.example.todolist.Model.NoteModel;
import com.example.todolist.Model.ToDoModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static  final String DATABASE_NAME = "TODO_DATABASE";
    private static  final String TABLE_NAME = "TODO_TABLE";
    private static  final String COL_1 = "ID";
    private static  final String COL_2 = "TASK";
    private static  final String COL_3 = "STATUS";
    private static  final String PR = "PRIORITY";
    private static  final String ROUTINE = "IS_ROUTINE";
    private static  final String RED = "REPEAT_END_DATE";
    private static  final String RD = "REPEAT_DAYS";
    private static  final String SNOTY = "SNOTY";
    private static  final String ENOTY = "ENOTY";

    private static  final String START = "START";
    private static  final String ENDD = "ENDD";//END
    private static  final String DURATION = "DURATION";

    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "category_name";

    private static final String TABLE_COMPLETED_DATES = "completed_dates";
    private static final String COL_COMPLETED_1 = "ID";
    private static final String COL_COMPLETED_2 = "ROUTINE_TASK_ID";
    private static final String COL_COMPLETED_3 = "COMPLETED_DATE"; // просто дата
    private static final String COL_COMPLETED_4 = "STATUS";




    private static final String TABLE_NOTES = "notes";
    private static final String COL_NOTE_ID = "ID";
    private static final String COL_NOTE_DESCRIPTION = "DESCRIPTION";
    private static final String COL_NOTE_DATE = "DATE";
    private  Context con;


    public DataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 8 );
        this.con=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "("
                    + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CATEGORY_NAME + " TEXT)");
            // Вставка значений по умолчанию
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, "нет");
            db.insert(TABLE_CATEGORIES, null, values);

            values.put(COLUMN_CATEGORY_NAME, "Работа");
            db.insert(TABLE_CATEGORIES, null, values);

            values.clear();
            values.put(COLUMN_CATEGORY_NAME, "Учёба");
            db.insert(TABLE_CATEGORIES, null, values);

            values.clear();
            values.put(COLUMN_CATEGORY_NAME, "Хобби");
            db.insert(TABLE_CATEGORIES, null, values);

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_2 + " TEXT,"
                    + COL_3 + " INTEGER,"
                    + PR + " INTEGER,"
                    + "DATE TEXT,"
                    + COLUMN_CATEGORY_ID + " INTEGER,"
                    + ROUTINE + " INTEGER,"
                    + RED + " TEXT,"
                    + RD + " TEXT,"
                    + SNOTY + " INTEGER,"
                    + ENOTY + " INTEGER,"
                    + START + " TEXT,"
                    + DURATION + " TEXT,"
                    + ENDD + " TEXT,"
                    + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_1 + "))");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_COMPLETED_DATES + "("
                + COL_COMPLETED_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_COMPLETED_2 + " INTEGER,"
                + COL_COMPLETED_3 + " TEXT,"
                + COL_COMPLETED_4 + " INTEGER,"
                + "FOREIGN KEY(" + COL_COMPLETED_2 + ") REFERENCES " + TABLE_NAME + "(" + COL_1 + "))");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
                + COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NOTE_DESCRIPTION + " TEXT,"
                + COL_NOTE_DATE + " TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public void insertNote(NoteModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTE_DESCRIPTION, note.getDescription());
        values.put(COL_NOTE_DATE, note.getDate());
        db.insert(TABLE_NOTES, null, values);
    }

    @SuppressLint("Range")
    public String getNoteTextByDate(String date) {
        db = this.getReadableDatabase();
        String query = "SELECT " + COL_NOTE_DESCRIPTION + " FROM " + TABLE_NOTES + " WHERE " + COL_NOTE_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date});

        String noteText = "";


        if (cursor != null && cursor.moveToFirst()) {
            noteText = cursor.getString(cursor.getColumnIndex(COL_NOTE_DESCRIPTION));
        }


        Log.e(TAG, "ошибка"+noteText+"ggg");
        cursor.close();

        return noteText;
    }

    public void updateNoteByDate(String date, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTE_DESCRIPTION, newDescription);
        db.update(TABLE_NOTES, values, COL_NOTE_DATE + "=?", new String[]{date});
    }

    public void deleteNoteByDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COL_NOTE_DATE + "=?", new String[]{date});
    }

    public void insertTask(ToDoModel model) throws ParseException {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , model.getTask());
        values.put(COL_3 , 0);
        values.put(PR, model.getPriority());
        values.put("DATE", model.getDate()); // <=== New line
        values.put(COLUMN_CATEGORY_ID, model.getCategoryId());
        values.put(ROUTINE, model.getIsRoutine());
        values.put(SNOTY, model.getSnoty());
        values.put(ENOTY, model.getEnoty());
        values.put(START, model.getStart());
        values.put(ENDD, model.getEnd());
        values.put(DURATION, model.getDuration());
        values.put(RED, model.getRepeatEndDate());
        values.put(RD, model.getRepeatDays());

        long taskId = db.insert(TABLE_NAME, null, values);

        if (model.getIsRoutine()==1) { // проверить на совпадение с repeat dates
            // Если задача рутинная, добавьте информацию о рутинной задаче в таблицу с прошедшими датами
            ContentValues completedValues = new ContentValues();
            completedValues.put(COL_COMPLETED_2, taskId);
            completedValues.put(COL_COMPLETED_3, model.getDate());
            completedValues.put(COL_COMPLETED_4, 0); // По умолчанию, задача не выполнена

            db.insert(TABLE_COMPLETED_DATES, null, completedValues);
        }else{
            // Получите время в миллисекундах для будильника (в данном примере, это время начала задачи)
            long alarmTimeMillis = getAlarmTimeMillis(model);

            // Установите будильник
            setAlarm(model.getId(), alarmTimeMillis);
        }
    }

    public void insertCategory(String categoryName) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);

        db.insert(TABLE_CATEGORIES, null, values);
    }

    public void updateCategoryName(int categoryId, String newCategoryName) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, newCategoryName);

        String whereClause = COLUMN_CATEGORY_ID + " = ?";
        String[] whereArgs = { String.valueOf(categoryId) };

        db.update(TABLE_CATEGORIES, values, whereClause, whereArgs);
    }

    public void deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        db.close();
    }

    public void updateTask(int id , String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , task);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id , int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3 , status);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
    }

    public List<CategoryModel> getAllCategories(Context context) {
        List<CategoryModel> categoriesList;
        categoriesList = new ArrayList<>();
        try {
            db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES, null);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                    @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                    CategoryModel category = new CategoryModel();
                    category.setName(categoryName);
                    category.setId(categoryId);
                    categoriesList.add(category);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }catch(Exception e){
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }

        return categoriesList;
    }

    @SuppressLint("Range")
    public String findCategoryNameByIndex(int categoryId) {
        db = this.getReadableDatabase();
        String categoryName = "0";
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_CATEGORIES,
                    new String[]{COLUMN_CATEGORY_NAME},
                    COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(categoryId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
            }
        } catch (Exception e) {
            // Обработка исключения
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoryName;
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(String date){
        Log.e(TAG, "Error message   "+date);

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();
        db.beginTransaction();
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE = ? OR " + ROUTINE + " = 1", new String[]{date});

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setPriority(cursor.getInt(cursor.getColumnIndex(PR)));
                        task.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
                        task.setCategoryId(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));

                        task.setStart((cursor.getString(cursor.getColumnIndex(START))));
                        task.setEnd((cursor.getString(cursor.getColumnIndex(ENDD))));

                        task.setSnoty(cursor.getInt(cursor.getColumnIndex(SNOTY)));
                        task.setEnoty(cursor.getInt(cursor.getColumnIndex(ENOTY)));

                        task.setDuration((cursor.getString(cursor.getColumnIndex(DURATION))));
                        task.setIsRoutine(cursor.getInt(cursor.getColumnIndex(ROUTINE)));

                        Log.e(TAG, "Error message   "+task.getIsRoutine());


                        // Existing routine task check
                        if (task.getIsRoutine() == 1) {
                            int routineTaskId = cursor.getInt(cursor.getColumnIndex(COL_1));
                            task.setId(routineTaskId);

                            task.setRepeatEndDate((cursor.getString(cursor.getColumnIndex(RED))));
                            task.setRepeatDays((cursor.getString(cursor.getColumnIndex(RD))));

                            // Check for existing completed dates for the routine task
                            Cursor completedCursor = db.rawQuery("SELECT * FROM " + TABLE_COMPLETED_DATES +
                                            " WHERE " + COL_COMPLETED_2 + " = ? AND " + COL_COMPLETED_3 + " = ?",
                                    new String[]{String.valueOf(routineTaskId), date});

                            if (completedCursor.moveToFirst()) {
                                // Task exists for this date
                                task.setStatus(completedCursor.getInt(completedCursor.getColumnIndex(COL_COMPLETED_4)));
                            } else {
                                // Task does not exist for this date, check if it's before the repeat end date
                                Cursor taskCursor = db.rawQuery("SELECT " + RED + " FROM " + TABLE_NAME +
                                        " WHERE " + COL_1 + " = ?", new String[]{String.valueOf(routineTaskId)});

                                if (taskCursor.moveToFirst()) {
                                    String repeatEndDate = taskCursor.getString(taskCursor.getColumnIndex(RED));
                                    Log.e(TAG, "Error message   "+repeatEndDate);
                                    if (repeatEndDate.equals("0") || compareDates(date, repeatEndDate) <= 0) {
                                        // Create a new task with status 0
                                        ContentValues completedValues = new ContentValues();
                                        completedValues.put(COL_COMPLETED_2, routineTaskId);
                                        completedValues.put(COL_COMPLETED_3, date);
                                        completedValues.put(COL_COMPLETED_4, 0); // Default status

                                        db.insert(TABLE_COMPLETED_DATES, null, completedValues);

                                        // Set status for the new task
                                        task.setStatus(0);
                                    }
                                }
                                taskCursor.close();
                            }
                            completedCursor.close();
                        }
                        else{
                            task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        }
                        if((task.getIsRoutine()==1 && (task.getRepeatEndDate().equals("0") || compareDates(date, task.getRepeatEndDate()) <= 0))||task.getIsRoutine()==0) {
                            modelList.add(task);
                        }
                    } while (cursor.moveToNext());
                }
            }

        }finally {
            db.endTransaction();
            cursor.close();
        }
        Collections.sort(modelList, new Comparator<ToDoModel>() {
            @Override
            public int compare(ToDoModel task1, ToDoModel task2) {
                // Сравниваем по статусу
                return Integer.compare(task1.getStatus(), task2.getStatus());
            }
        });
        return modelList;
    }

    private int compareDates(String date1, String date2) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dateObject1 = dateFormat1.parse(date1);
            Date dateObject2 = dateFormat2.parse(date2);

            if (dateObject1 != null && dateObject2 != null) {
                return dateObject1.compareTo(dateObject2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressLint("Range")
    public ToDoModel getTaskById(int taskId) {
        db = this.getWritableDatabase();

        String[] columns = {COL_1, COL_2, COL_3, PR, "DATE", COLUMN_CATEGORY_ID, ROUTINE, RED, RD, SNOTY, ENOTY, START, DURATION, ENDD};
        String selection = COL_1 + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        ToDoModel task = null;
        if (cursor != null && cursor.moveToFirst()) {
            task = new ToDoModel();
            task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
            task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
            task.setPriority(cursor.getInt(cursor.getColumnIndex(PR)));
            task.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            task.setCategoryId(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
            task.setStart((cursor.getString(cursor.getColumnIndex(START))));
            task.setEnd((cursor.getString(cursor.getColumnIndex(ENDD))));
            task.setSnoty(cursor.getInt(cursor.getColumnIndex(SNOTY)));
            task.setEnoty(cursor.getInt(cursor.getColumnIndex(ENOTY)));
            task.setDuration((cursor.getString(cursor.getColumnIndex(DURATION))));
            task.setIsRoutine(cursor.getInt(cursor.getColumnIndex(ROUTINE)));
            task.setRepeatEndDate(cursor.getString(cursor.getColumnIndex(RED)));
            task.setRepeatDays(cursor.getString(cursor.getColumnIndex(RD)));
        }

        if (cursor != null) {
            cursor.close();
        }

        return task;
    }

    private long getAlarmTimeMillis(ToDoModel model) {
        // Ваш код для получения времени в миллисекундах, например, из model.getStart() или model.getDate()

        // Пример: Получение времени в миллисекундах из строки даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        try {
            Date date = dateFormat.parse(model.getDate() + " " + model.getStart());
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(int taskId, long alarmTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(con, MainActivity.class); // открывается при нажатии будильника / Интент для связи переправки компоненктов
        intent.putExtra("taskId", taskId); // передайте идентификатор задачи, чтобы определить, какую задачу отображать при срабатывании будильника
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);// второй на случай если приложение сейчас не запущено а первое почистить стек и текущий экземпляр на вершину

        PendingIntent pendingIntent = PendingIntent.getActivity(con, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); // типо конверта в котором интенет




        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(alarmTimeMillis,pendingIntent);


//
//        // Установка будильника
//        if (alarmManager != null) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
//        }

        // кодда будильнок срабатывает
        Intent intent1 = new Intent(con, AlarmActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);// второй на случай если приложение сейчас не запущено а первое почистить стек и текущий экземпляр на вершину
        PendingIntent pendingIntent1 = PendingIntent.getActivity(con, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); // типо конверта в котором интенет флаг значит заменить созданный заново передаваемым

        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent1);
        Toast.makeText(con, "Будильник установлен", Toast.LENGTH_SHORT).show();
    }



}
