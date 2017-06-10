package com.liuyufei.bmc_android.admin;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;


public class AppointmentCursorAdapter extends CursorAdapter {
    public AppointmentCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.fragment_appointment, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView description = (TextView) view.findViewById(R.id.appDesc);
        TextView staffName = (TextView) view.findViewById(R.id.staff_name);
        TextView visitorName = (TextView) view.findViewById(R.id.visitor_name);
        TextView appointmentDate = (TextView) view.findViewById(R.id.appointment_date);

        int textDesc = cursor.getColumnIndex(BMCContract.AppointmentEntry.COLUMN_DESCRIPTION);
        int textStaffName = cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME);
        int textVisitorName = cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_NAME);
        int textAppointmentDate = cursor.getColumnIndex(BMCContract.AppointmentEntry.COLUMN_DATETIME);

        String strStaffName = cursor.getString(textStaffName);
        String strDescription = cursor.getString(textDesc);
        String strVisitorName = cursor.getString(textVisitorName);
        String strAppointmentDate = cursor.getString(textAppointmentDate);

        staffName.setText(strStaffName);
        description.setText(strDescription);
        visitorName.setText(strVisitorName);
        appointmentDate.setText(strAppointmentDate);

    }
}
