package com.liuyufei.bmc_android.admin;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;
import com.squareup.picasso.Picasso;

import java.io.File;


public class VisitorCursorAdapter extends CursorAdapter {
    public VisitorCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.fragment_visitor, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView visitorName = (TextView) view.findViewById(R.id.visitorName);
        int textColumn = cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_NAME);
        String text = cursor.getString(textColumn);
        visitorName.setText(text);

    }
}
