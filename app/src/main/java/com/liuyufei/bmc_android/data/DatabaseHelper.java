package com.liuyufei.bmc_android.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.liuyufei.bmc_android.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.liuyufei.bmc_android.data.BMCContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BMC.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STAFF_CREATE =
            "CREATE TABLE " + StaffEntry.TABLE_NAME + " (" +
                    StaffEntry._ID + " INTEGER PRIMARY KEY, " +
                    StaffEntry.COLUMN_NAME + " TEXT, " +
                    StaffEntry.COLUMN_TITLE + " TEXT, " +
                    StaffEntry.COLUMN_DEPARTMENT + " TEXT, " +
                    StaffEntry.COLUMN_PHOTO + " TEXT, " +
                    StaffEntry.COLUMN_MOBILE + " TEXT" +
                    ")";

    private static final String TABLE_VISITOR_CREATE =
            "CREATE TABLE " + VisitorEntry.TABLE_NAME + " (" +
                    VisitorEntry._ID + " INTEGER PRIMARY KEY, " +
                    VisitorEntry.COLUMN_NAME + " TEXT, " +
                    VisitorEntry.COLUMN_CREATION_TIME + " TEXT default CURRENT_TIMESTAMP, " +
                    VisitorEntry.COLUMN_LASTLOGIN_TIME + " TEXT default CURRENT_TIMESTAMP, " +
                    VisitorEntry.COLUMN_BUSINESS_NAME + " TEXT, " +
                    VisitorEntry.COLUMN_MOBILE + " TEXT " +
                    ")";

    private static final String TABLE_APPOINTMENT_CREATE =
            "CREATE TABLE " + AppointmentEntry.TABLE_NAME + " (" +
                    AppointmentEntry._ID + " INTEGER PRIMARY KEY, " +
                    AppointmentEntry.COLUMN_DESCRIPTION + " TEXT, " +
                    AppointmentEntry.COLUMN_DATETIME + " TEXT default CURRENT_TIMESTAMP, " +
                    AppointmentEntry.COLUMN_STAFF + " INTEGER NOT NULL, " +
                    AppointmentEntry.COLUMN_VISITOR + " INTEGER NOT NULL, " +
                    " FOREIGN KEY(" + AppointmentEntry.COLUMN_VISITOR + ") REFERENCES " + VisitorEntry.TABLE_NAME + "(" + VisitorEntry._ID + "), " +
                    " FOREIGN KEY(" + AppointmentEntry.COLUMN_STAFF + ") REFERENCES " + StaffEntry.TABLE_NAME + "(" + StaffEntry._ID + ") " +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_STAFF_CREATE);
        db.execSQL(TABLE_VISITOR_CREATE);
        db.execSQL(TABLE_APPOINTMENT_CREATE);
        initData(db);
    }


    private void initData(SQLiteDatabase db) {
        Log.i("DatabaseHelper", "init data...");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss");
        Calendar cal = Calendar.getInstance();
        ContentValues values = new ContentValues();

        List<Long> visitorsID = new ArrayList<>();
        List<Long> staffID = new ArrayList<>();

        int items = 10;

        //create staff
        for(int i=0;i<items;i++){
            values.put(StaffEntry.COLUMN_DEPARTMENT, "USA");
            values.put(StaffEntry.COLUMN_NAME, "Trump"+i);
            values.put(StaffEntry.COLUMN_MOBILE, "123456");
            values.put(StaffEntry.COLUMN_TITLE, "President");
            values.put(StaffEntry.COLUMN_PHOTO, R.drawable.staff);

            staffID.add(db.insert(StaffEntry.TABLE_NAME, null, values));
            values.clear();
        }

        //create visitors
        for(int i=0;i<items;i++){
            values.put(VisitorEntry.COLUMN_BUSINESS_NAME, "China");
            values.put(VisitorEntry.COLUMN_NAME, "XI JINP"+i);
            values.put(VisitorEntry.COLUMN_MOBILE, "123456");
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR,-i-5);
            values.put(VisitorEntry.COLUMN_CREATION_TIME, dateFormat.format(cal.getTime()));
            values.put(VisitorEntry.COLUMN_LASTLOGIN_TIME, dateFormat.format(cal.getTime()));

            visitorsID.add(db.insert(VisitorEntry.TABLE_NAME, null, values));
            values.clear();
        }

        //create appointment
        Collections.shuffle(visitorsID, new Random(123456));
        Collections.shuffle(staffID, new Random(654321));

        for(int i=0;i<items;i++){
            long visitor = visitorsID.get(i);
            long staff = staffID.get(i);
            values.put(AppointmentEntry.COLUMN_DESCRIPTION, "Come to play LOL "+i);
            values.put(AppointmentEntry.COLUMN_STAFF, staff);
            values.put(AppointmentEntry.COLUMN_VISITOR, visitor);
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR,-i);
            values.put(AppointmentEntry.COLUMN_DATETIME, dateFormat.format(cal.getTime()));
            visitorsID.add(db.insert(AppointmentEntry.TABLE_NAME, null, values));
            values.clear();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StaffEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VisitorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AppointmentEntry.TABLE_NAME);
        onCreate(db);
    }
}
