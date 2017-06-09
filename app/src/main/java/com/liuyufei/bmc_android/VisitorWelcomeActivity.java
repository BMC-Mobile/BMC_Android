package com.liuyufei.bmc_android;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;
import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.admin.AdminActivity;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.data.BMCQueryHandler;
import com.liuyufei.bmc_android.utility.Constants;

import static com.liuyufei.bmc_android.R.id.checkbtn;

public class VisitorWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_welcome);

        TextView mobileTV = (TextView) findViewById(R.id.inputMobile);
        final String inputMobile = mobileTV.getText().toString();

        if(inputMobile==null||inputMobile.length()==0){
            //alert input mobile is null
            return;
        }

        findViewById(checkbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AsyncQueryHandler queryHandler =
                        new AsyncQueryHandler(getContentResolver()) {
                            @Override
                            protected void onQueryComplete(int token, Object cookie,
                                                           Cursor cursor) {

                                Toast.makeText(v.getContext(),"query result from ...",Toast.LENGTH_LONG).show();

                                try {
                                    if ((cursor != null) && cursor.moveToFirst()) {
                                        String displayName = cursor.getString(0);
                                        // go to appointment list page\
                                        //find all appointments relevant to the visitor.
                                        //Intent intentToAppointmentForm = new Intent(VisitorWelcomeActivity.this,AdminActivity.class);
                                        Constants.STR_ACIVITY_NAME = "VisitorWelcomeActivity";

                                    }else{
                                        //check if the visitor is new
                                        //if new go to the appointment creation page

                                        Intent intentToAppointmentForm = new Intent(VisitorWelcomeActivity.this,VisitorCheckin.class);
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
