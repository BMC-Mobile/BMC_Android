package com.liuyufei.bmc_android;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.liuyufei.bmc_android.model.Visitor;
import com.liuyufei.bmc_android.utility.Constants;

import java.util.ArrayList;
import java.util.Arrays;
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
    List<Staff> staffList = new ArrayList<>();
    List<String> purposeList = Arrays.asList("General Business","Drop In","Scheduled Appointment","Other");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_visitor_checkin);
        name_txt = (EditText) findViewById(R.id.name_txt);
        company_txt = (EditText) findViewById(R.id.company_txt);
        contact_txt = (EditText) findViewById(R.id.contact_txt);
        //hard code purpose
        purpose_spi = (Spinner) findViewById(R.id.purpose_spi);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(VisitorCheckIn.this,
                android.R.layout.simple_spinner_item, purposeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purpose_spi.setAdapter(dataAdapter);

        staff_spi = (Spinner) findViewById(R.id.staff_spi);
        checkBTN = (Button) findViewById(R.id.checkin_btn);

        Integer check_status = getIntent().getExtras().getInt("check_status");
        queryStaff();
        if (CHECKOUT.equals(check_status)) {
            final Integer visitorID = getIntent().getExtras().getInt("visitorID");
            final String visitorName = getIntent().getExtras().getString("visitorName");
            queryAppointmentByVisitorID(visitorID);
            checkBTN.setText("Check Out");
            checkBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkOut(visitorID, visitorName);
                }
            });
        } else {
            Visitor visitor = (Visitor) getIntent().getExtras().get("visitor");
            if(visitor!=null){
                //for existing visitor
                name_txt.setText(visitor.name.get());
                company_txt.setText(visitor.bussinessname.get());
                contact_txt.setText(visitor.mobile.get());
            }else{
                //for the new coming visitor
                String mobile = getIntent().getExtras().getString("mobile");
                contact_txt.setText(mobile);
            }
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

        if(staffList.size()!=0) return;

        AsyncQueryHandler queryHandler =
                new AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onQueryComplete(int token, Object cookie,
                                                   Cursor cursor) {
                        try {
                            while (cursor.moveToNext()) {
                                Staff staff = new Staff(cursor.getInt(0), cursor.getString(1), null, null, null, null);
                                staffList.add(staff);
                            }

                            ArrayAdapter<Staff> dataAdapter = new ArrayAdapter<>(VisitorCheckIn.this,
                                    android.R.layout.simple_spinner_item, staffList);
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


    private void queryAppointmentByVisitorID(Integer visitorID) {
        //query from appointment by visitorID
        AsyncQueryHandler queryHandler =
                new AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onQueryComplete(int token, Object cookie,
                                                   Cursor cursor) {
                        try {
                            //only selection the first one
                            if (cursor.moveToNext()) {

                                String purpose = cursor.getString(cursor.getColumnIndex(BMCContract.AppointmentEntry.COLUMN_DESCRIPTION));
                                String staffName = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME));
                                String visitorName = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_NAME));
                                String visitorCompanyName = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_BUSINESS_NAME));
                                String visitorMobile = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_MOBILE));
                                name_txt.setText(visitorName);
                                name_txt.setEnabled(false);
                                company_txt.setText(visitorCompanyName);
                                company_txt.setEnabled(false);
                                contact_txt.setText(visitorMobile);
                                contact_txt.setEnabled(false);
                                // set Staff spinner position
                                for(int indexOfStaff = 0;indexOfStaff<staffList.size();indexOfStaff++){
                                    if(staffList.get(indexOfStaff).name.get().equals(staffName)){
                                        staff_spi.setSelection(indexOfStaff);
                                        //??
                                        break;
                                    }
                                }

                                // set purpose spinner position
                                for(int indexOfPurpose = 0;indexOfPurpose<purposeList.size();indexOfPurpose++){
                                    if(purposeList.get(indexOfPurpose).equals(purpose)){
                                        purpose_spi.setSelection(indexOfPurpose);
                                        //??
                                        //111
                                        break;
                                    }
                                }
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                };

        String selection = BMCContract.AppointmentEntry.COLUMN_VISITOR + "=?";
        String[] args = {visitorID.toString()};
        String orderBy = BMCContract.AppointmentEntry.COLUMN_CREATED_WHEN+" DESC";
        queryHandler.startQuery(0, null, BMCContract.AppointmentEntry.CONTENT_URI, null, selection, args, orderBy);
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
                //TODO go to the appointment staffList
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
