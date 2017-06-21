package com.liuyufei.bmc_android.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.liuyufei.bmc_android.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.liuyufei.bmc_android.data.BMCContract.AppointmentEntry;
import static com.liuyufei.bmc_android.data.BMCContract.CHECKOUT;
import static com.liuyufei.bmc_android.data.BMCContract.StaffEntry;
import static com.liuyufei.bmc_android.data.BMCContract.VisitorEntry;

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
                    StaffEntry.COLUMN_CREATED_BY + " TEXT default BMC_ADMIN, " +
                    StaffEntry.COLUMN_CREATED_WHEN + " datetime default CURRENT_TIMESTAMP, " +
                    StaffEntry.COLUMN_UPDATED_BY + " TEXT, " +
                    StaffEntry.COLUMN_UPDATED_WHEN + " datetime, " +
                    StaffEntry.COLUMN_MOBILE + " TEXT" +
                    ")";

    private static final String TABLE_VISITOR_CREATE =
            "CREATE TABLE " + VisitorEntry.TABLE_NAME + " (" +
                    VisitorEntry._ID + " INTEGER PRIMARY KEY, " +
                    VisitorEntry.COLUMN_NAME + " TEXT, " +
                    VisitorEntry.COLUMN_CREATION_TIME + " datetime, " +
                    VisitorEntry.COLUMN_LASTLOGIN_TIME + " datetime, " +
                    VisitorEntry.COLUMN_LASTLOGOUT_TIME + " datetime, " +
                    VisitorEntry.COLUMN_BUSINESS_NAME + " TEXT, " +
                    VisitorEntry.COLUMN_MOBILE + " TEXT, " +
                    VisitorEntry.COLUMN_CREATED_BY + " TEXT default BMC_ADMIN, " +
                    VisitorEntry.COLUMN_CREATED_WHEN + " datetime default CURRENT_TIMESTAMP, " +
                    VisitorEntry.COLUMN_UPDATED_BY + " TEXT, " +
                    VisitorEntry.COLUMN_UPDATED_WHEN + " datetime, " +
                    VisitorEntry.COLUMN_CHECK_STATUS + " INTEGER default " +CHECKOUT+
                    " )";

    private static final String TABLE_APPOINTMENT_CREATE =
            "CREATE TABLE " + AppointmentEntry.TABLE_NAME + " (" +
                    AppointmentEntry._ID + " INTEGER PRIMARY KEY, " +
                    AppointmentEntry.COLUMN_DESCRIPTION + " TEXT, " +
                    AppointmentEntry.COLUMN_DATETIME + " datetime default CURRENT_TIMESTAMP, " +
                    AppointmentEntry.COLUMN_CREATED_BY + " TEXT default BMC_ADMIN, " +
                    AppointmentEntry.COLUMN_CREATED_WHEN + " datetime default CURRENT_TIMESTAMP, " +
                    AppointmentEntry.COLUMN_UPDATED_BY + " TEXT, " +
                    AppointmentEntry.COLUMN_UPDATED_WHEN + " datetime, " +
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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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


        values.put(StaffEntry.COLUMN_DEPARTMENT, "BMC");
        values.put(StaffEntry.COLUMN_NAME, "BMC_ADMIN");
        values.put(StaffEntry.COLUMN_MOBILE, "123456");
        values.put(StaffEntry.COLUMN_TITLE, "President");
        values.put(StaffEntry.COLUMN_PHOTO, R.drawable.staff);

        staffID.add(db.insert(StaffEntry.TABLE_NAME, null, values));
        values.clear();

        //create visitors
        for(int i=0;i<items;i++){
            if(i==1){
                values.put(VisitorEntry.COLUMN_BUSINESS_NAME, "Spark");
                values.put(VisitorEntry.COLUMN_NAME, "Smith"+i);
                values.put(VisitorEntry.COLUMN_MOBILE, "123456");
            }else if(i==2){
                values.put(VisitorEntry.COLUMN_BUSINESS_NAME, "BMC");
                values.put(VisitorEntry.COLUMN_NAME, "Steward"+i);
                values.put(VisitorEntry.COLUMN_MOBILE, "123456");
            }else if(i==3){
                values.put(VisitorEntry.COLUMN_BUSINESS_NAME, "BMC");
                values.put(VisitorEntry.COLUMN_NAME, "Hernandez"+i);
                values.put(VisitorEntry.COLUMN_MOBILE, "123456");
            }else{
                values.put(VisitorEntry.COLUMN_BUSINESS_NAME, "Wintec");
                values.put(VisitorEntry.COLUMN_NAME, "Visitor"+i);
                values.put(VisitorEntry.COLUMN_MOBILE, "123456");
            }

            cal.setTime(new Date());
            //cal.add(Calendar.HOUR_OF_DAY,-i-5);
            values.put(VisitorEntry.COLUMN_CREATION_TIME, dateFormat.format(cal.getTime()));
            values.put(VisitorEntry.COLUMN_LASTLOGIN_TIME, dateFormat.format(cal.getTime()));
            if(i%2==0) {
                //not logout
                values.put(VisitorEntry.COLUMN_CHECK_STATUS,1);
            }else{
                values.put(VisitorEntry.COLUMN_LASTLOGOUT_TIME, dateFormat.format(cal.getTime()));

            }
            visitorsID.add(db.insert(VisitorEntry.TABLE_NAME, null, values));
            values.clear();
        }

        //create appointment
        Collections.shuffle(visitorsID, new Random(123456));
        Collections.shuffle(staffID, new Random(654321));

        for(int i=0;i<items;i++){
            long visitor = visitorsID.get(i);
            long staff = staffID.get(i);
            values.put(AppointmentEntry.COLUMN_DESCRIPTION, "General Business");
            values.put(AppointmentEntry.COLUMN_STAFF, staff);
            values.put(AppointmentEntry.COLUMN_VISITOR, visitor);
            cal.setTime(new Date());
            if(i%3==0){
                cal.add(Calendar.HOUR_OF_DAY,-i-1);
            }else{
                cal.add(Calendar.HOUR_OF_DAY,-i);
            }
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
