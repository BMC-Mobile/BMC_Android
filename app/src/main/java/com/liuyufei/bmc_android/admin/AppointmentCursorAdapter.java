package com.liuyufei.bmc_android.admin;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.utility.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class AppointmentCursorAdapter extends CursorAdapter {
    public AppointmentCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.fragment_appointment, parent, false);
    }

    static String TAG = "AppCursorAdapter";
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        TextView description = (TextView) view.findViewById(R.id.appDesc);
//        TextView staffName = (TextView) view.findViewById(R.id.staff_name);
        TextView appointmentDate = (TextView) view.findViewById(R.id.appointment_date);
        TextView appointmentTime = (TextView) view.findViewById(R.id.appointment_time);
        TextView visitorName = (TextView) view.findViewById(R.id.visitor_name);
        TextView tvBusinessName = (TextView) view.findViewById(R.id.business_name);
        TextView tvRemainTime = (TextView) view.findViewById(R.id.remain_time);




        int textDesc = cursor.getColumnIndex(BMCContract.AppointmentEntry.COLUMN_DESCRIPTION);
        int textStaffName = cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME);
        int textVisitorName = cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_NAME);
        int textBusinessName = cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_BUSINESS_NAME);
        int textAppointmentDate = cursor.getColumnIndex(BMCContract.AppointmentEntry.COLUMN_DATETIME);

        String strStaffName = cursor.getString(textStaffName);
        String strDescription = cursor.getString(textDesc);
        String strVisitorName = cursor.getString(textVisitorName);
        String businessName = cursor.getString(textBusinessName);


        String strAppointmentDate = cursor.getString(textAppointmentDate);

        // re-write get needed data to bind view
        SimpleDateFormat dateFormatFromDb = new SimpleDateFormat(DateUtil.determineDateFormat(strAppointmentDate));
        Date dateFromDb = new Date();
        try {
            dateFromDb = dateFormatFromDb.parse(strAppointmentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat appTimeFormat = new SimpleDateFormat("h:mm a");
        SimpleDateFormat appDateFormat = new SimpleDateFormat("EEEE, MMM d");
        String appTimeStr = appTimeFormat.format(dateFromDb);
        String appDateStr = appDateFormat.format(dateFromDb);

        // bing view
        appointmentDate.setText(appDateStr);
        appointmentTime.setText(appTimeStr);

        String remainTimeStr = "";
        // remaining time
        try {
            Hashtable<String,Long> remainTime = DateUtil.getRemainingTime(dateFromDb);
            if(remainTime.get("day")>=0){
                remainTimeStr = remainTime.get("day")+" days";
            }else {
                if(remainTime.get("hour")>=0){
                    remainTimeStr = remainTime.get("hour")+" hours";
                }else{
                    remainTimeStr = remainTime.get("min")+" mins";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


//        staffName.setText(strStaffName);
//        description.setText(strDescription);
        visitorName.setText(strVisitorName);
        appointmentDate.setText(appDateStr);
        tvBusinessName.setText(businessName);
        tvRemainTime.setText(remainTimeStr);
    }





}
