package com.liuyufei.bmc_android;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.model.Staff;
import com.liuyufei.bmc_android.model.Visitor;
import com.liuyufei.bmc_android.utility.Constants;

import static com.liuyufei.bmc_android.R.id.checkbtn;
import static com.liuyufei.bmc_android.data.BMCContract.CHECKIN;

public class VisitorWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_welcome);


        findViewById(checkbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                TextView mobileTV = (TextView) findViewById(R.id.inputMobile);
                final String inputMobile = mobileTV.getText().toString();

                if(inputMobile==null||inputMobile.length()==0){
                    //alert input mobile is null
                    new AlertDialog.Builder(VisitorWelcomeActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Please input the Mobile")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                    return;
                }

                AsyncQueryHandler queryHandler =
                        new AsyncQueryHandler(getContentResolver()) {
                            @Override
                            protected void onQueryComplete(int token, Object cookie,
                                                           Cursor cursor) {
                                try {
                                    if ((cursor != null) && cursor.moveToFirst()) {
//                                        String displayName = cursor.getString(0);
                                        // go to appointment staffList page\
                                        //find all appointments relevant to the visitor.
                                        //Intent intentToAppointmentForm = new Intent(VisitorWelcomeActivity.this,AdminActivity.class);
                                        Constants.STR_ACIVITY_NAME = "VisitorWelcomeActivity";
                                        String visitorName = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_NAME));
                                        String visitorCompanyName = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_BUSINESS_NAME));
                                        String visitorMobile = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_MOBILE));
                                        Visitor visitor = new Visitor(-1,visitorName,visitorCompanyName,visitorMobile);
                                        Intent intentToAppointmentForm = new Intent(VisitorWelcomeActivity.this,VisitorCheckIn.class);
                                        intentToAppointmentForm.putExtra("visitor",visitor);
                                        intentToAppointmentForm.putExtra("check_status",CHECKIN);
                                        startActivity(intentToAppointmentForm);
                                    }else{
                                        //check if the visitor is new
                                        //if new go to the appointment creation page
                                        Intent intentToAppointmentForm = new Intent(VisitorWelcomeActivity.this,VisitorCheckIn.class);
                                        intentToAppointmentForm.putExtra("mobile",inputMobile);
                                        intentToAppointmentForm.putExtra("check_status",CHECKIN);
                                        startActivity(intentToAppointmentForm);
                                    }
                                } finally {
                                    cursor.close();
                                }
                            }
                        };

                String selection = BMCContract.VisitorEntry.COLUMN_MOBILE+"=?";
                String[] selectionArgs = {inputMobile};
                queryHandler.startQuery(0, null, BMCContract.VisitorEntry.CONTENT_URI,
                        new String[] {BMCContract.VisitorEntry.COLUMN_NAME, BMCContract.VisitorEntry.COLUMN_BUSINESS_NAME, BMCContract.VisitorEntry.COLUMN_MOBILE}, selection, selectionArgs,
                        null);

            }
        });

    }
}
