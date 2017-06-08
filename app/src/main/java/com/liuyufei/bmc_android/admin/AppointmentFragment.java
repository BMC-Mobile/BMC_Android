package com.liuyufei.bmc_android.admin;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.model.Appointment;
import com.liuyufei.bmc_android.model.Staff;
import com.liuyufei.bmc_android.model.Visitor;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    ContentResolver contentResolver;
    private static final int URL_LOADER = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    Cursor cursor;
    AppointmentCursorAdapter adapter;
    ListView lv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        contentResolver = view.getContext().getContentResolver();
        lv = (ListView) view;
        adapter = new AppointmentCursorAdapter(getContext(), cursor, false);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //move the cursor to the selected row
                cursor = (Cursor) adapterView.getItemAtPosition(pos);
                //get the staff data from the cursor
                int staffID = cursor.getInt(cursor.getColumnIndex(BMCContract.StaffEntry._ID));
                String staffMobile = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_MOBILE));
                String staffName = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME));
                String staffPhoto = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_PHOTO));
                String staffDepartment = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_DEPARTMENT));
                String staffTitle = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_TITLE));
                //create the object that will be passed to the todoActivity
                Staff staff = new Staff(staffID, staffName, staffPhoto, staffDepartment, staffTitle, staffMobile);

                //get the visitor data from the cursor
                int visitorID = cursor.getInt(cursor.getColumnIndex(BMCContract.VisitorEntry._ID));
                String visitorMobile = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_MOBILE));
                String visitorName = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_NAME));
                String vBusinessName = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_BUSINESS_NAME));
                Visitor visitor = new Visitor(visitorID,visitorName,vBusinessName,visitorMobile);

                //get the appointment data from the cursor
                int appointmentID = cursor.getInt(cursor.getColumnIndex(BMCContract.AppointmentEntry._ID));
                String appointmentTime = cursor.getString(cursor.getColumnIndex(BMCContract.AppointmentEntry.COLUMN_DATETIME));
                String appointmentDesc = cursor.getString(cursor.getColumnIndex(BMCContract.AppointmentEntry.COLUMN_DESCRIPTION));
                Appointment appointment = new Appointment(appointmentID,appointmentDesc,appointmentTime);

                Intent intent = new Intent(getActivity(), AppointmentMgnActivity.class);
                //pass the ID to the todoActivity
                intent.putExtra("staff", staff);
                intent.putExtra("visitor", visitor);
                intent.putExtra("appointment", appointment);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //change selection
        Log.i("onCreateLoader", "onCreateLoader selection changed");
        Uri resourceUri = BMCContract.AppointmentEntry.CONTENT_URI;

        //filter visitors who forget to checkout
        Loader<Cursor> lc = new CursorLoader(getActivity(), resourceUri, null, null, null, null);
        return lc;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

/*
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }
*/

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
