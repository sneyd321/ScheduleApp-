package com.example.ryan.scheduleapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ryan on 12/6/2016.
 */
public class ScheduleDb {


    private SQLiteDatabase database;

    private SQLiteOpenHelper openHelper;

    private static final String DB_NAME = "schedule.db";

    public static final int DB_VERSION = 1;

    public static final String SCHEDULE_TABLE = "schedule";



    private static final String ID = "_id";

    public static final String DAY = "day";
    public static final String  CLASSNAME = "classname";
    public static final String  CLASSROOM = "classroom";
    public static final String  TIME = "time";



    public static final String CREATE_TABLE =
            "CREATE TABLE " + SCHEDULE_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DAY + " TEXT, " + CLASSNAME + " TEXT, " +
            CLASSROOM + " TEXT, " + TIME + " TEXT)";

    public ScheduleDb(Context context){
        openHelper = new DBHelper(context);


    }

    public Schedule saveSchedule(Schedule schedule){
        database = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DAY, schedule.getDay());
        values.put(CLASSNAME, schedule.getClassName());
        values.put(CLASSROOM, schedule.getClassRoom());
        values.put(TIME, schedule.getTime());

        long id = database.insert(SCHEDULE_TABLE, null, values);

        if (id > 0){
            schedule.setId(id);
        }

        database.close();

        return schedule;


    }

    public ArrayList<Schedule> selectSchedules(){

        ArrayList<Schedule> schedules = new ArrayList<>();

        database = openHelper.getReadableDatabase();

        Cursor cursor = database.query(SCHEDULE_TABLE, null, null, null, null, null, null);

        while (cursor.moveToNext()){

            long id = cursor.getLong(cursor.getColumnIndex(ID));
            String day = cursor.getString(cursor.getColumnIndex(DAY));
            String className = cursor.getString(cursor.getColumnIndex(CLASSNAME));
            String classRoom = cursor.getString(cursor.getColumnIndex(CLASSROOM));
            String time = cursor.getString(cursor.getColumnIndex(TIME));

            schedules.add(new Schedule(id, day, className, classRoom, time));

        }

        cursor.close();

        database.close();

        return schedules;


    }

    public void deletePlayer(long id){
        database = openHelper.getWritableDatabase();

        String where = ID + " = ? ";

        String[] whereArgs = new String[] {String.format("%d", id)};

        database.delete(SCHEDULE_TABLE, where, whereArgs);

        database.close();

    }

    public  void update(Schedule schedule){

        database = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(DAY, schedule.getDay());
        values.put(CLASSNAME, schedule.getClassName());
        values.put(CLASSROOM, schedule.getClassRoom());
        values.put(TIME, schedule.getTime());

        String where = ID + " = ? ";

        String[] whereArgs = new String[] {String.format("%d", schedule.getId())};

        database.update(SCHEDULE_TABLE, values, where, whereArgs);

        database.close();

    }





    private static final class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE);

            onCreate(db);
        }
    }

}
