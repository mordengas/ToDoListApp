package com.dominik_machnik.ToDoApp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dominik_machnik.ToDoApp.model.Task;

import java.util.ArrayList;



public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todolist_db";
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME= "time";
    private static final String KEY_STATUS = "status";


    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_STATUS + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        // Create tables again
        onCreate(db);
    }


    public void insertTasks(String name, String description, String date, String time){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("status", "incomplete");


        long newRow = db.insert(TABLE_TASKS, null, contentValues);
        db.close();


    }

    public ArrayList<Task> getAllTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Task> tasksList = new ArrayList<>();

        String query = "SELECT * FROM "+ TABLE_TASKS + " ORDER BY "+KEY_ID+" DESC";
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){

            Task task = new Task(
                    cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_TIME)),
                    cursor.getString(cursor.getColumnIndex(KEY_STATUS))
            );

            tasksList.add(task);
        }


        return tasksList;
    }



    public void deleteTask(String nota_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID+" = ?", new String[]{nota_id});
        db.close();
    }

    public int deleteAllTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete (TABLE_TASKS, String.valueOf (1), null);
    }


    public int updateTask(String task_id, String name, String description, String date, String time, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("status", status);

        return db.update(TABLE_TASKS, contentValues, KEY_ID+" = ?",new String[]{task_id});
    }

    public int updateStatus(String status, String task_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        return db.update(TABLE_TASKS, contentValues, KEY_ID+" = ?",new String[]{task_id});
    }

}
