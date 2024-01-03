package com.example.todolist.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.todolist.Model.CategoryModel;
import com.example.todolist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static  final String DATABASE_NAME = "TODO_DATABASE";
    private static  final String TABLE_NAME = "TODO_TABLE";
    private static  final String COL_1 = "ID";
    private static  final String COL_2 = "TASK";
    private static  final String COL_3 = "STATUS";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "category_name";


    public DataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "("
                    + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CATEGORY_NAME + " TEXT)");
            // Вставка значений по умолчанию
            ContentValues values = new ContentValues();
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
                    + "DATE TEXT,"
                    + COLUMN_CATEGORY_ID + " INTEGER,"
                    + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_1 + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public void insertTask(ToDoModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , model.getTask());
        values.put(COL_3 , 0);
        values.put("DATE", model.getDate()); // <=== New line
        values.put(COLUMN_CATEGORY_ID, model.getCategoryId());
        db.insert(TABLE_NAME , null , values);
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

    public CategoryModel getLastInsertedCategory() {
        db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CATEGORIES + " ORDER BY " + COLUMN_CATEGORY_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        CategoryModel lastInsertedCategory = null;

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_CATEGORY_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);

            int categoryId = cursor.getInt(idIndex);
            String categoryName = cursor.getString(nameIndex);

            lastInsertedCategory = new CategoryModel();
            lastInsertedCategory.setId(categoryId);
            lastInsertedCategory.setName(categoryName);
        }

        cursor.close();
        return lastInsertedCategory;
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
    public List<ToDoModel> getAllTasks(String date){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();
        db.beginTransaction();
        try {
            String sortOrder = "CASE WHEN " + COL_3 + " = 1 THEN 0 ELSE 1 END, " + COL_3;
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE = ? ORDER BY " + sortOrder, new String[]{date});
            if (cursor !=null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        task.setDate(cursor.getString(cursor.getColumnIndex("DATE"))); // Добавили сюда строку
                        modelList.add(task);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

}
