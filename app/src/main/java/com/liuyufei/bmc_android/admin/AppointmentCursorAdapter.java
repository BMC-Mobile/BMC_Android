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
        TextView visitorName = (TextView) view.findViewById(R.id.appDesc);
        int textColumn = cursor.getColumnIndex(BMCContract.AppointmentEntry.COLUMN_DESCRIPTION);
        String text = cursor.getString(textColumn);
        visitorName.setText(text);

    }
}
