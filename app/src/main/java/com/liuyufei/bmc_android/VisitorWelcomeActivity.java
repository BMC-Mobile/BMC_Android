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
import com.liuyufei.bmc_android.utility.Constants;

import static com.liuyufei.bmc_android.R.id.checkbtn;

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
                                        // go to appointment list page\
                                        //find all appointments relevant to the visitor.
                                        //Intent intentToAppointmentForm = new Intent(VisitorWelcomeActivity.this,AdminActivity.class);
                                        Constants.STR_ACIVITY_NAME = "VisitorWelcomeActivity";

                                    }else{
                                        //check if the visitor is new
                                        //if new go to the appointment creation page
                                        Intent intentToAppointmentForm = new Intent(VisitorWelcomeActivity.this,VisitorCheckIn.class);
                                        intentToAppointmentForm.putExtra("mobile",inputMobile);
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
                        new String[] {BMCContract.VisitorEntry.COLUMN_NAME}, selection, selectionArgs,
                        null);

            }
        });

    }
}
