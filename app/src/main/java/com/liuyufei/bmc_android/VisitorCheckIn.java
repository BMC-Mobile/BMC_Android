package com.liuyufei.bmc_android;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.liuyufei.bmc_android.admin.AdminActivity;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.data.BMCQueryHandler;
import com.liuyufei.bmc_android.model.Staff;
import com.liuyufei.bmc_android.utility.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.liuyufei.bmc_android.data.BMCContract.CHECKIN;
import static com.liuyufei.bmc_android.data.BMCContract.CHECKOUT;
import static com.liuyufei.bmc_android.utility.Constants.getCurrentTimeAsString;


public class VisitorCheckIn extends AppCompatActivity {

    EditText name_txt;
    EditText company_txt;
    EditText contact_txt;
    Spinner purpose_spi;
    Spinner staff_spi;
    Button checkBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_checkin);
        name_txt = (EditText) findViewById(R.id.name_txt);
        company_txt = (EditText) findViewById(R.id.company_txt);
        contact_txt = (EditText) findViewById(R.id.contact_txt);
        //hard code purpose
        purpose_spi = (Spinner) findViewById(R.id.purpose_spi);
        staff_spi = (Spinner) findViewById(R.id.staff_spi);
        checkBTN = (Button) findViewById(R.id.checkin_btn);

        String check_status = getIntent().getExtras().getString("check_status");
        if ("checkout".equals(check_status)) {
            final Integer visitorID = getIntent().getExtras().getInt("visitorID");
            final String visitorName = getIntent().getExtras().getString("visitorName");
            //disable all editText
            checkBTN.setText("Check Out");
            checkBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkOut(visitorID, visitorName);
                }
            });
        } else {
            String mobile = getIntent().getExtras().getString("mobile");
            contact_txt.setText(mobile);
            //query db for staffs
            queryStaff();
            checkBTN.setText("Check In");
            checkBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkIn();
                }
            });
        }


    }


    private void queryStaff() {
        AsyncQueryHandler queryHandler =
                new AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onQueryComplete(int token, Object cookie,
                                                   Cursor cursor) {
                        try {
                            List<Staff> list = new ArrayList<>();
                            while (cursor.moveToNext()) {
                                Staff staff = new Staff(cursor.getInt(0), cursor.getString(1), null, null, null, null);
                                list.add(staff);
                            }

                            ArrayAdapter<Staff> dataAdapter = new ArrayAdapter<>(VisitorCheckIn.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            staff_spi.setAdapter(dataAdapter);

                        } finally {
                            cursor.close();
                        }
                    }
                };

        queryHandler.startQuery(0, null, BMCContract.StaffEntry.CONTENT_URI,
                new String[]{BMCContract.StaffEntry._ID, BMCContract.StaffEntry.COLUMN_NAME}, null, null, null);
    }


    public void checkIn() {
        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                BMCQueryHandler handler = new BMCQueryHandler(getContentResolver());
                ContentValues appointmentValues = new ContentValues();
                //get select staff id
                String visitor_id = uri.getLastPathSegment();
                appointmentValues.put(BMCContract.AppointmentEntry.COLUMN_STAFF, ((Staff) staff_spi.getSelectedItem()).Id.get());
                appointmentValues.put(BMCContract.AppointmentEntry.COLUMN_DESCRIPTION, purpose_spi.getSelectedItem().toString());
                appointmentValues.put(BMCContract.AppointmentEntry.COLUMN_VISITOR, visitor_id);
                handler.startInsert(1, null, BMCContract.AppointmentEntry.CONTENT_URI, appointmentValues);
                //TODO go to the appointment list
            }
        };

//      extract visitor to save
        ContentValues visitorValues = new ContentValues();
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_NAME, name_txt.getText().toString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_BUSINESS_NAME, company_txt.getText().toString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_MOBILE, contact_txt.getText().toString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_CREATION_TIME, getCurrentTimeAsString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_LASTLOGIN_TIME, getCurrentTimeAsString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_CHECK_STATUS, CHECKIN);

        insertHandler.startInsert(1, null, BMCContract.VisitorEntry.CONTENT_URI, visitorValues);
        Constants.STR_ACIVITY_NAME = "VisitorCheckInActivity";
        Intent goAdmin = new Intent(VisitorCheckIn.this, AdminActivity.class);
        startActivity(goAdmin);

    }


    public void checkOut(final Integer visitorID, String visitorName) {
        //confirm checkout a visitor by admin?
        new AlertDialog.Builder(this)
                .setTitle("Confirm Checkout?")
                .setMessage("Confirm help " + visitorName + " checkout?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //update checkout status
                        String selection = BMCContract.VisitorEntry._ID + "=?";
                        String[] arguments = new String[1];
                        arguments[0] = String.valueOf(visitorID);
                        BMCQueryHandler queryHandler = new BMCQueryHandler(getContentResolver());
                        ContentValues values = new ContentValues();
                        values.put(BMCContract.VisitorEntry.COLUMN_CHECK_STATUS, CHECKOUT);
                        queryHandler.startUpdate(1, null, BMCContract.VisitorEntry.CONTENT_URI, values, selection, arguments);
                        Constants.STR_ACIVITY_NAME = "VisitorCheckInActivity";
                        Intent goAdmin = new Intent(VisitorCheckIn.this, AdminActivity.class);
                        startActivity(goAdmin);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();


    }
}
