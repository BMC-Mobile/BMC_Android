package com.liuyufei.bmc_android;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.data.BMCQueryHandler;
import com.liuyufei.bmc_android.model.Staff;

import java.util.ArrayList;
import java.util.List;

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
        String mobile = getIntent().getExtras().getString("mobile");

        name_txt = (EditText) findViewById(R.id.name_txt);
        company_txt = (EditText) findViewById(R.id.company_txt);
        contact_txt = (EditText) findViewById(R.id.contact_txt);
        contact_txt.setText(mobile);
        //hard code purpose
        purpose_spi = (Spinner) findViewById(R.id.purpose_spi);
        staff_spi = (Spinner) findViewById(R.id.staff_spi);
        //query db for staffs
        queryStaff();

        checkBTN = (Button) findViewById(R.id.checkin_btn);
        checkBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIn();
            }
        });
    }


    private void queryStaff(){
        AsyncQueryHandler queryHandler =
                new AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onQueryComplete(int token, Object cookie,
                                                   Cursor cursor) {
                        try {
                            List<Staff> list = new ArrayList<>();
                            while(cursor.moveToNext()){
                                Staff staff = new Staff(cursor.getInt(0),cursor.getString(1),null,null,null,null);
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
                new String[] {BMCContract.StaffEntry._ID,BMCContract.StaffEntry.COLUMN_NAME}, null, null, null);
    }


    public void checkIn(){
        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                BMCQueryHandler handler = new BMCQueryHandler(getContentResolver());
                ContentValues appointmentValues = new ContentValues();
                //get select staff id
                String visitor_id = uri.getLastPathSegment();
                appointmentValues.put(BMCContract.AppointmentEntry.COLUMN_STAFF,((Staff)staff_spi.getSelectedItem()).Id.get());
                appointmentValues.put(BMCContract.AppointmentEntry.COLUMN_DESCRIPTION,purpose_spi.getSelectedItem().toString());
                appointmentValues.put(BMCContract.AppointmentEntry.COLUMN_VISITOR,visitor_id);
                handler.startInsert(1, null, BMCContract.AppointmentEntry.CONTENT_URI,appointmentValues);

            }
        };

//      extract visitor to save
        ContentValues visitorValues = new ContentValues();
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_NAME,name_txt.getText().toString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_BUSINESS_NAME,company_txt.getText().toString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_MOBILE,contact_txt.getText().toString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_CREATION_TIME, getCurrentTimeAsString());
        visitorValues.put(BMCContract.VisitorEntry.COLUMN_LASTLOGIN_TIME, getCurrentTimeAsString());

        insertHandler.startInsert(1, null, BMCContract.VisitorEntry.CONTENT_URI,visitorValues);

        
    }
}
