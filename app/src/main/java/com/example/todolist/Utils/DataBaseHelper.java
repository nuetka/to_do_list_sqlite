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
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.todolist.Model.CategoryModel;
import com.example.todolist.Model.NoteModel;
import com.example.todolist.Model.ToDoModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
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
    private static final String IS_SELECTED = "is_selected";

    private static final String TABLE_COMPLETED_DATES = "completed_dates";
    private static final String COL_COMPLETED_1 = "ID";
    private static final String COL_COMPLETED_2 = "ROUTINE_TASK_ID";
    private static final String COL_COMPLETED_3 = "COMPLETED_DATE"; // просто дата
    private static final String COL_COMPLETED_4 = "STATUS";




    private static final String TABLE_NOTES = "notes_filters";
    private static final String COL_NOTE_ID = "ID";

    private static final String COL_NOTE_DESCRIPTION = "DESCRIPTION";
    private static final String COL_NOTE_DATE = "DATE";
    private  Context con;



    private static final String SETTINGS_TABLE = "settings_table";

    private static final String SETTING = "setting";
    private static final String SETTING_ID = "ID"; // нулевой id - filter


    public DataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 10 );
        this.con=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "("
                    + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + IS_SELECTED + " INTEGER DEFAULT 1,"
                    + COLUMN_CATEGORY_NAME + " TEXT)");
            // Вставка значений по умолчанию
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, "нет");
            db.insert(TABLE_CATEGORIES, null, values);

            values.clear();
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

           db.execSQL("CREATE TABLE IF NOT EXISTS " +  SETTINGS_TABLE+ "("
                + SETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SETTING + " TEXT)");

                values.clear();
                values.put(SETTING, "[1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0]");
                db.insert(SETTINGS_TABLE, null, values);

                values.clear();
                values.put(SETTING, "[1,0,0]");
                db.insert(SETTINGS_TABLE, null, values);
                values.clear();
                values.put(SETTING, "0");
                db.insert(SETTINGS_TABLE, null, values);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    @SuppressLint("Range")
    public int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORIES +
                " WHERE " + COLUMN_CATEGORY_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{categoryName});

        int categoryId = -1; // Значение по умолчанию, если категория не найдена

        if (cursor != null && cursor.moveToFirst()) {
            categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
        }

        if (cursor != null) {
            cursor.close();
        }

        return categoryId;
    }


    @SuppressLint("Range")
    public int getCompletedTasksCountInRange(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query for completed tasks from main table
        String queryMainTable = "SELECT COUNT(*) FROM " + TABLE_NAME +
                " WHERE " + COL_COMPLETED_4 + " = 1" +
                " AND " + "DATE" + " BETWEEN ? AND ?";

        Cursor cursorMainTable = db.rawQuery(queryMainTable, new String[]{startDate, endDate});
        int countMainTable = 0;

        if (cursorMainTable != null && cursorMainTable.moveToFirst()) {
            countMainTable = cursorMainTable.getInt(0);
        }

        if (cursorMainTable != null) {
            cursorMainTable.close();
        }

        // Query for completed tasks from completed dates table
        String queryCompletedDatesTable = "SELECT COUNT(*) FROM " + TABLE_COMPLETED_DATES +
                " WHERE " + COL_COMPLETED_4 + " = 1" +
                " AND " + COL_COMPLETED_3 + " BETWEEN ? AND ?";

        Cursor cursorCompletedDatesTable = db.rawQuery(queryCompletedDatesTable, new String[]{startDate, endDate});
        int countCompletedDatesTable = 0;

        if (cursorCompletedDatesTable != null && cursorCompletedDatesTable.moveToFirst()) {
            countCompletedDatesTable = cursorCompletedDatesTable.getInt(0);
        }

        if (cursorCompletedDatesTable != null) {
            cursorCompletedDatesTable.close();
        }

        // Sum up the counts
        int totalCount = countMainTable + countCompletedDatesTable;

        return totalCount;
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Close the existing database
        if (db != null && db.isOpen()) {
            db.close();
        }

        // Delete the database file
        if (con != null) {
            con.deleteDatabase(DATABASE_NAME);
        }

        // Reopen the database
        db = this.getWritableDatabase();

        // Recreate the database by calling onCreate
        onCreate(db);
    }

    public int getTotalTasksCountInRange(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        int routineTaskCount = 0;

        // Query for non-routine tasks
        String queryNonRoutine = "SELECT COUNT(*) FROM " + TABLE_NAME +
                " WHERE " + "DATE" + " BETWEEN ? AND ?" +
                " AND " + ROUTINE + " = 0";

        Cursor cursorNonRoutine = db.rawQuery(queryNonRoutine, new String[]{startDate, endDate});
        int countNonRoutine = 0;

        if (cursorNonRoutine != null && cursorNonRoutine.moveToFirst()) {
            countNonRoutine = cursorNonRoutine.getInt(0);
        }

        if (cursorNonRoutine != null) {
            cursorNonRoutine.close();
        }

        // Query for routine tasks within the date range, considering the different scenarios
        String queryRoutine = "SELECT " + "DATE" + ", " + RED + ", " + RD +
                " FROM " + TABLE_NAME +
                " WHERE " + "DATE" + " BETWEEN ? AND ?" +
                " AND " + ROUTINE + " = 1";

        Cursor cursorRoutine = db.rawQuery(queryRoutine, new String[]{startDate, endDate});
        ArrayList<String[]> routineTasks = new ArrayList<>();

        if (cursorRoutine != null) {
            while (cursorRoutine.moveToNext()) {
                @SuppressLint("Range") String date = cursorRoutine.getString(cursorRoutine.getColumnIndex("DATE"));
                @SuppressLint("Range") String red = cursorRoutine.getString(cursorRoutine.getColumnIndex(RED));
                @SuppressLint("Range") String repeatDays = cursorRoutine.getString(cursorRoutine.getColumnIndex(RD));
                routineTasks.add(new String[]{date, red, repeatDays});
                routineTaskCount += calculateRoutineTaskCount(routineTasks, startDate, endDate);
            }
            cursorRoutine.close();
        }

        int totalCount = countNonRoutine + routineTaskCount;

        return totalCount;
    }

    public CategoryModel getCategoryById(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        CategoryModel categoryModel = null;

        String[] projection = {COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME, IS_SELECTED};
        String selection = COLUMN_CATEGORY_ID + "=?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        Cursor cursor = db.query(TABLE_CATEGORIES, projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
            boolean isSelected = cursor.getInt(cursor.getColumnIndex(IS_SELECTED)) == 1;

            categoryModel = new CategoryModel(id, name, isSelected);
            cursor.close();
        }

        return categoryModel;
    }


    public int calculateRoutineTaskCount(ArrayList<String[]> routineTasks, String startDate, String endDate) {
        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            Calendar calendar = Calendar.getInstance();

            for (String[] task : routineTasks) {
                String taskDate = task[0];
                String red = task[1];
                String repeatDays = task[2];

                Date taskStartDate = sdf.parse(taskDate);
                Date taskEndDate = red.equals("0") ? end : sdf.parse(red); // Если RED = 0, задача повторяется до endDate

                // Итерируем по каждой дате в диапазоне
                for (Date date = start; !date.after(end); ) {
                    // Проверяем, попадает ли текущая дата в диапазон повторения задачи
                    if (!date.before(taskStartDate) && !date.after(taskEndDate)) {
                        // Проверяем, соответствует ли день недели задаче
                        if (isDayOfWeek(sdf.format(date), repeatDays)) {
                            count++;
                        }
                    }

                    // Переходим к следующему дню
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, 1);
                    date = calendar.getTime();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return count;
    }

    @SuppressLint("Range")
    public String getDayBeforTasks() {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + SETTINGS_TABLE + " WHERE " + SETTING_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"3"});

        String filter = "";


        if (cursor != null && cursor.moveToFirst()) {
            filter = cursor.getString(cursor.getColumnIndex(SETTING));
        }

        cursor.close();

        return filter;
    }

    public void updateTasksDayBefor(String filter) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Преобразуйте список в строку в нужном вам формате

        values.put(SETTING, filter);

        db.update(SETTINGS_TABLE, values, "ID=?", new String[]{"3"});
    }





    public void updateFilter(List<String> filter) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Преобразуйте список в строку в нужном вам формате
        String filtostr = "[" + TextUtils.join(",", filter) + "]";
        values.put(SETTING, filtostr);

        db.update(SETTINGS_TABLE, values, "ID=?", new String[]{"1"});
    }

    public void updateSort(List<String> filter) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Преобразуйте список в строку в нужном вам формате
        String filtostr = "[" + TextUtils.join(",", filter) + "]";
        values.put(SETTING, filtostr);

        db.update(SETTINGS_TABLE, values, "ID=?", new String[]{"2"});
    }

    @SuppressLint("Range")
    public List<String> getFilter() {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + SETTINGS_TABLE + " WHERE " + SETTING_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"1"});

        String filter = "";


        if (cursor != null && cursor.moveToFirst()) {
            filter = cursor.getString(cursor.getColumnIndex(SETTING));
        }

        cursor.close();

        filter = filter.substring(1, filter.length() - 1); // Удаляем квадратные скобки
        String[] parts = filter.split(",");
        List<String> resultList = Arrays.asList(parts);

// Если вам нужен именно ArrayList, а не List, вы можете создать новый ArrayList:
        List<String> resultArrayList = new ArrayList<>(Arrays.asList(parts));

        return resultArrayList;
    }


    @SuppressLint("Range")
    public List<String> getSort() {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + SETTINGS_TABLE + " WHERE " + SETTING_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"2"});

        String filter = "";


        if (cursor != null && cursor.moveToFirst()) {
            filter = cursor.getString(cursor.getColumnIndex(SETTING));
        }

        cursor.close();

        filter = filter.substring(1, filter.length() - 1); // Удаляем квадратные скобки
        String[] parts = filter.split(",");
        List<String> resultList = Arrays.asList(parts);

// Если вам нужен именно ArrayList, а не List, вы можете создать новый ArrayList:
        List<String> resultArrayList = new ArrayList<>(Arrays.asList(parts));

        return resultArrayList;
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
            if (isDayOfWeek(model.getDate(), model.getRepeatDays())) {
                ContentValues completedValues = new ContentValues();
                completedValues.put(COL_COMPLETED_2, taskId);
                completedValues.put(COL_COMPLETED_3, model.getDate());
                completedValues.put(COL_COMPLETED_4, 0); // По умолчанию, задача не выполнена

                db.insert(TABLE_COMPLETED_DATES, null, completedValues);
            }
        }else{
            // Получите время в миллисекундах для будильника (в данном примере, это время начала задачи)
            long alarmTimeMillis1 = getAlarmTimeMillisStart(model);
            long alarmTimeMillis2 = getAlarmTimeMillisEnd(model);

            // Установите будильник
            setAlarm(model.getId(), alarmTimeMillis1, alarmTimeMillis2, model.getTask(), model.getSnoty(), model.getEnoty());
        }
    }

    public void insertCategory(String categoryName) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        values.put(IS_SELECTED, 1);

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

    public void updateStatus(ToDoModel item , int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(item.getIsRoutine()==0) {
            values.put(COL_3, status);
            db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(item.getId())});
        }else{

            values.put( COL_COMPLETED_4, status);
            db.update(TABLE_COMPLETED_DATES, values, "ID=? AND "+COL_COMPLETED_3+"=?", new String[]{String.valueOf(item.getId()), String.valueOf(item.getDate())});

        }
    }

    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
        cancelAlarm(id);
    }

    @SuppressLint("Range")
    public List<CategoryModel> getAllCategories() {
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
                    category.setSelected(cursor.getInt(cursor.getColumnIndex(IS_SELECTED)) == 1);
                    categoriesList.add(category);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }catch(Exception e){

        }

        return categoriesList;
    }

    @SuppressLint("Range")
    public String findCategoryNameByIndex(int categoryId) {
        db = this.getReadableDatabase();
        String categoryName = "нет";
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
    public List<ToDoModel> getAllTasks(String date, boolean isSortNeeded){
        Log.e(TAG, "Error message   "+date);

        String date_befor = date;
        db = this.getWritableDatabase();

        String stroka=null;







        Cursor cursorr = null;
        try {
            cursorr = db.rawQuery("SELECT * FROM " + SETTINGS_TABLE + " WHERE " + SETTING_ID + " = ?", new String[]{"3"});
            if (cursorr != null && cursorr.moveToFirst()) {
                stroka = cursorr.getString(cursorr.getColumnIndex(SETTING));
                if(stroka.equals("0")){
                    date_befor=date;
                }else{
                    date_befor=getDayBefore(date);
                }




            }
        } finally {
            if (cursorr != null) {
                cursorr.close();
            }
        }

        String datete = null;

        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();
        db.beginTransaction();
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE = ? OR DATE = ? OR " + ROUTINE + " = 1", new String[]{date, date_befor});

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setPriority(cursor.getInt(cursor.getColumnIndex(PR)));
                        task.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
                        task.setCategoryId(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));

                        datete=task.getDate();
                        task.setStart((cursor.getString(cursor.getColumnIndex(START))));
                        task.setEnd((cursor.getString(cursor.getColumnIndex(ENDD))));

                        task.setSnoty(cursor.getInt(cursor.getColumnIndex(SNOTY)));
                        task.setEnoty(cursor.getInt(cursor.getColumnIndex(ENOTY)));

                        task.setDuration((cursor.getString(cursor.getColumnIndex(DURATION))));
                        task.setIsRoutine(cursor.getInt(cursor.getColumnIndex(ROUTINE)));

                        Log.e(TAG, "Error message   "+task.getIsRoutine());

                        boolean passed = false;

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
                                task.setDate(completedCursor.getString(completedCursor.getColumnIndex(COL_COMPLETED_3)));
                                Log.e(TAG, "ЖОПАNEW  "+"|"+task.getStatus());
                                passed=true;
                            } else {
                                // Task does not exist for this date, check if it's before the repeat end date
                                Cursor taskCursor = db.rawQuery("SELECT " + RED + ", " + RD + " FROM " + TABLE_NAME +
                                        " WHERE " + COL_1 + " = ?", new String[]{String.valueOf(routineTaskId)});

                                if (taskCursor.moveToFirst()) {
                                    String repeatEndDate = taskCursor.getString(taskCursor.getColumnIndex(RED));
                                    String rd = taskCursor.getString(taskCursor.getColumnIndex(RD));
                                    Log.e(TAG, "Error message   "+repeatEndDate);
                                    if ((repeatEndDate.equals("0") || compareDates(date, repeatEndDate) <= 0) && compareDates1(datete, date) <= 0)  {
                                        Log.e(TAG, "ЖОПА  "+repeatEndDate+"|"+date+"|"+repeatEndDate+"|"+datete);
                                        if (isDayOfWeek(date, rd)) {
                                            passed=true;
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
                                }
                                taskCursor.close();
                            }
                            completedCursor.close();
                        }
                        else{
                            task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        }
                        if(((task.getIsRoutine()==1 && (passed))||task.getIsRoutine()==0)) {
                            if(stroka.equals("0") ||    task.getDate().equals(date)&&stroka.equals("1")||(stroka.equals("0")&&task.getDate().equals(date_befor)&&task.getStatus()==0)) {
                                modelList.add(task);
                            }
                        }
                    } while (cursor.moveToNext());
                }
            }

        }finally {
            cursor.close();
        }

        if(isSortNeeded) {
            Cursor cursor1 = null;
            try {
                cursor1 = db.rawQuery("SELECT * FROM " + SETTINGS_TABLE + " WHERE " + SETTING_ID + " = ?", new String[]{"1"});
                if (cursor1 != null && cursor1.moveToFirst()) {
                    String str = cursor1.getString(cursor1.getColumnIndex(SETTING));


                    // Ваши дальнейшие действия с переменной str
                    Iterator<ToDoModel> iterator = modelList.iterator();

                    while (iterator.hasNext()) {
                        ToDoModel model = iterator.next();
                        Log.e("MyApp", "str.charAt(0): " + str.charAt(0) + ", model.getIsRoutine(): " + model.getIsRoutine());
                        if (str.charAt(1) == '0' && model.getIsRoutine() == 0) {
                            Log.e("MyApp", "Удаление: " + model);
                            iterator.remove();
                        }
                        else if (str.charAt(1) == '0' && model.getIsRoutine() == 1) {
                            iterator.remove();
                        }
                        else if (str.charAt(3) == '0' && model.getRepeatEndDate().equals("0")) {
                            iterator.remove();
                        }
                        else if (str.charAt(5) == '0' && model.getStatus() == 1) {
                            iterator.remove();
                        }
                        else if (str.charAt(7) == '0' && model.getStatus() == 0) { // невыполенные не нужны
                            iterator.remove();
                        }

                        else if(!getCategoryById(model.getCategoryId()).isSelected()){
                            iterator.remove();
                        }


                    }




                    }
            } finally {
                if (cursor1 != null) {
                    cursor1.close();
                }
            }
        }



            Cursor cursor2 = null;
            try {
                cursor2 = db.rawQuery("SELECT * FROM " + SETTINGS_TABLE + " WHERE " + SETTING_ID + " = ?", new String[]{"2"});
                if (cursor2 != null && cursor2.moveToFirst()) {
                    String str = cursor2.getString(cursor2.getColumnIndex(SETTING));

                     //Сортировка списка
                    Collections.sort(modelList, new Comparator<ToDoModel>() {
                        @Override
                        public int compare(ToDoModel o1, ToDoModel o2) {
                            if (str.charAt(1) == '1') {
                                // Сначала сортируем по статусу (невыполненные задачи идут первыми)
                                int statusCompare = Integer.compare(o1.getStatus(), o2.getStatus());
                                if (statusCompare != 0) {
                                    return statusCompare;
                                }
                            } else if (str.charAt(1) == '2') {
                                int statusCompare = Integer.compare(o1.getStatus(), o2.getStatus());
                                if (statusCompare != 0) {
                                    return -statusCompare; // Используем отрицание для изменения порядка сортировки
                                }
                            }

                            if (str.charAt(3) == '1') {
                                // Затем сортируем по приоритету (высший приоритет идет первым)
                                // Предполагаем, что более низкое числовое значение означает более высокий приоритет
                                return Integer.compare(o1.getPriority(), o2.getPriority());
                            } else if (str.charAt(3) == '2') {
                                return -Integer.compare(o1.getPriority(), o2.getPriority());
                            }
                            return Integer.compare(o1.getId(), o2.getId());
                        }
                    });

                }
            } finally {
                if (cursor2 != null) {
                    cursor2.close();
                }
            }

            db.endTransaction();

        return modelList;
    }

    public static boolean isDayOfWeek(String dateStr, String repeatDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Понедельник = 0



            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2; // Понедельник = 0
            if (dayOfWeek == -1) { // Воскресенье становится 6
                dayOfWeek = 6;
            }
            // Проверяем, соответствует ли день недели
            return repeatDays.charAt(dayOfWeek) == '1';

        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Если произошла ошибка парсинга даты, возвращаем false
        }
    }

    public static String getDayBefore(String dateString) {
        // Define the formatter based on the expected format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parse the input string to a LocalDate
        LocalDate date = LocalDate.parse(dateString, formatter);

        // Subtract one day from the date
        LocalDate dayBefore = date.minusDays(1);

        // Return the formatted date string
        return dayBefore.format(formatter);
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

    private static int compareDates1(String date1, String date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date dateObject1 = dateFormat.parse(date1);
            Date dateObject2 = dateFormat.parse(date2);

            if (dateObject1 != null && dateObject2 != null) {
                return dateObject1.compareTo(dateObject2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Return 0 in case of parsing error or null dates
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

    private long getAlarmTimeMillisStart(ToDoModel model) {
        // Преобразуйте дату и время из модели в Calendar
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        try {
            Date date = dateFormat.parse(model.getDate() + " " + model.getStart());
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                  calendar.set(Calendar.SECOND, 0);

                return calendar.getTimeInMillis();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private long getAlarmTimeMillisEnd(ToDoModel model) {
        // Преобразуйте дату и время из модели в Calendar
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        try {
            Date date = dateFormat.parse(model.getDate() + " " + model.getEnd());
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                // Установите точное время срабатывания в 12:23:00
//                calendar.set(Calendar.HOUR_OF_DAY, 12);
//                calendar.set(Calendar.MINUTE, 23);
                calendar.set(Calendar.SECOND, 0);

                return calendar.getTimeInMillis();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void updateCategorySelectedStatus(int categoryId, boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SELECTED, isSelected ? 1 : 0);
        db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        db.close();
    }

    public String[] getSelectedCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> selectedCategories = new ArrayList<>();

        try {
            Cursor cursor = db.rawQuery("SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORIES + " WHERE " + IS_SELECTED + " = 1", null);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                    selectedCategories.add(categoryName);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return selectedCategories.toArray(new String[0]);
    }

    public void updateAllCategoriesSelection(boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SELECTED, isSelected ? 1 : 0);
        db.update(TABLE_CATEGORIES, values, null, null);
        db.close();
    }

    public void updateCategorySelectionById(int categoryId, boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SELECTED, isSelected ? 1 : 0);
        String selection = COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(categoryId) };
        db.update(TABLE_CATEGORIES, values, selection, selectionArgs);
        db.close();
    }

    @SuppressLint("Range")
    public boolean isAnySelected() {
        db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean isSelected = false;

        try {
            String query = "SELECT COUNT(*) FROM categories WHERE is_selected = 0";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Проверяем, есть ли хотя бы одна отмеченная категория

                if((cursor.getInt(0) > 0)){
                    isSelected=true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        String query = "SELECT * FROM " + SETTINGS_TABLE + " WHERE " + SETTING_ID + " = ?";
        Cursor cursor1 = db.rawQuery(query, new String[]{"1"});

        String filter = "";


        if (cursor1 != null && cursor1.moveToFirst()) {
            filter = cursor1.getString(cursor1.getColumnIndex(SETTING));
        }

        cursor1.close();

        for(int i=0; i<13; i++) {
            if ((i % 2) == 0) {
                if (filter.charAt(i + 1) == '0') {
                    isSelected = true;
                }
            }
        }

        filter = filter.substring(1, filter.length() - 1); // Удаляем квадратные скобки
        String[] parts = filter.split(",");
        List<String> resultList = Arrays.asList(parts);

// Если вам нужен именно ArrayList, а не List, вы можете создать новый ArrayList:
        List<String> resultArrayList = new ArrayList<>(Arrays.asList(parts));



           for(int j=14; j<17; j++){
               if(!resultArrayList.get(j).equals("0")){
                   isSelected=true;
               }
           }



        return isSelected;
    }

//    @SuppressLint("ScheduleExactAlarm")
//    private void setAlarm(int taskId, long alarmTimeMillis, String taskText) {
//        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
//
//        Intent intent = new Intent(con, AlarmActivity.class);
//        intent.putExtra("taskId", taskId);
//        intent.putExtra("task_text", taskText);  // Передаем текст задачи
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(con, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        // Используем метод setExact для точного срабатывания будильника
//        if (alarmManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
//            } else {
//                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
//            }
//        }
//    }

//    @SuppressLint("ScheduleExactAlarm")
//    private void setAlarm(int taskId, long startAlarmTimeMillis, long endAlarmTimeMillis, String taskText) {
//        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
//
//        Log.e("AlarmSet", "Start Alarm Time: " + startAlarmTimeMillis);
//        Log.e("AlarmSet", "End Alarm Time: " + endAlarmTimeMillis);
//
//        // Создаем Intent для начала выполнения задачи
//        Intent startIntent = new Intent(con, AlarmActivity.class);
//        startIntent.putExtra("taskId", taskId);
//        startIntent.putExtra("task_text", taskText + " (Start)");  // Передаем текст задачи с пометкой начала выполнения
//        startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Создаем PendingIntent для начала выполнения задачи
//        PendingIntent startPendingIntent = PendingIntent.getActivity(con, taskId, startIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        // Создаем Intent для конца выполнения задачи
//        Intent endIntent = new Intent(con, AlarmActivity.class);
//        endIntent.putExtra("taskId", (-1)*taskId);
//        endIntent.putExtra("task_text", taskText + " (End)");  // Передаем текст задачи с пометкой конца выполнения
//        endIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Создаем PendingIntent для конца выполнения задачи
//        PendingIntent endPendingIntent = PendingIntent.getActivity(con, (-1)*taskId, endIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        // Используем метод setExact для точного срабатывания будильников
//        if (alarmManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startAlarmTimeMillis, startPendingIntent);
//                if (endAlarmTimeMillis > 0) {
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endAlarmTimeMillis, endPendingIntent);
//                }
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startAlarmTimeMillis, startPendingIntent);
//                if (endAlarmTimeMillis > 0) {
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, endAlarmTimeMillis, endPendingIntent);
//                }
//            } else {
//                alarmManager.set(AlarmManager.RTC_WAKEUP, startAlarmTimeMillis, startPendingIntent);
//                if (endAlarmTimeMillis > 0) {
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, endAlarmTimeMillis, endPendingIntent);
//                }
//            }
//        }
//    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(int taskId, long startAlarmTimeMillis, long endAlarmTimeMillis, String taskText, int snoty, int enoty) {
        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
        taskId+=1;

        Log.e("AlarmSet", "Start Alarm Time: " + startAlarmTimeMillis);
        Log.e("AlarmSet", "End Alarm Time: " + endAlarmTimeMillis);

        if(snoty==1) {

            // Create Intent for starting the task
            Intent startIntent = new Intent(con, AlarmActivity.class);
            startIntent.putExtra("taskId", taskId);
            startIntent.putExtra("task_text", taskText + " (Start)");
            startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            // Create PendingIntent for starting the task
            PendingIntent startPendingIntent = PendingIntent.getActivity(con, taskId, startIntent, PendingIntent.FLAG_IMMUTABLE);

            // Use setExact for precise alarm triggering
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startAlarmTimeMillis, startPendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, startAlarmTimeMillis, startPendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, startAlarmTimeMillis, startPendingIntent);
                }
            }
        }
        if(enoty==1) {

            // Check if there is a valid end alarm time
            if (endAlarmTimeMillis > 0) {
                // Create Intent for ending the task
                Intent endIntent = new Intent(con, AlarmActivity.class);
                endIntent.putExtra("taskId", (100) * taskId);
                endIntent.putExtra("task_text", taskText + " (End)");
                endIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // Create PendingIntent for ending the task
                PendingIntent endPendingIntent = PendingIntent.getActivity(con, (100) * taskId, endIntent, PendingIntent.FLAG_IMMUTABLE);

                // Set the end alarm
                if (alarmManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endAlarmTimeMillis, endPendingIntent);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, endAlarmTimeMillis, endPendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, endAlarmTimeMillis, endPendingIntent);
                    }
                }
            }
        }
    }

    public void cancelAlarm(int taskId) {
        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);

        // Интенты должны быть такими же, как и при установке Alarm
        Intent intent = new Intent(con, AlarmActivity.class);

        // Создаем PendingIntent для начала выполнения задачи
        PendingIntent startPendingIntent = PendingIntent.getActivity(con, taskId+1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Создаем PendingIntent для конца выполнения задачи
        PendingIntent endPendingIntent = PendingIntent.getActivity(con, (100)*(taskId+1), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Отменяем оба PendingIntent
        if (alarmManager != null) {
            alarmManager.cancel(startPendingIntent);
            alarmManager.cancel(endPendingIntent);
        }
    }




}
