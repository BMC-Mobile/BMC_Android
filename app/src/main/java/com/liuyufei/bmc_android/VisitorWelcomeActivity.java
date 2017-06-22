package com.liuyufei.bmc_android;

import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import com.liuyufei.bmc_android.admin.AdminActivity;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.login.LoginActivity;
import com.liuyufei.bmc_android.model.Staff;
import com.liuyufei.bmc_android.model.Visitor;
import com.liuyufei.bmc_android.utility.Constants;

import static com.liuyufei.bmc_android.R.id.checkbtn;
import static com.liuyufei.bmc_android.data.BMCContract.CHECKIN;

public class VisitorWelcomeActivity extends AppCompatActivity implements
            GestureDetector.OnDoubleTapListener,
            GestureDetector.OnGestureListener{
    static String TAG = "VisitorWelcomeActivity";
    Button btnRing;

    private GestureDetectorCompat myDector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_visitor_welcome);
        btnRing = (Button) findViewById(R.id.ring_button_visit);
        //ring reception
        // Set click Ring
        btnRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"User Click Ring");
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                    return ;
                }

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0277777006"));
                startActivity(intent);
            }
        });

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
        myDector = new GestureDetectorCompat(this,this);
        myDector.setOnDoubleTapListener(this);
    }

    private static final int REQUEST_CALL_PHONE = 0;

    public void requestCameraPermission(){
        Log.i(TAG,"requestCameraPermission -- start --");
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
            ActivityCompat.requestPermissions(VisitorWelcomeActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL_PHONE);
        }else{
            ActivityCompat.requestPermissions(VisitorWelcomeActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL_PHONE);
        }
    }

    /**
     * Handle the permission check result of contacts
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==RESULT_OK){
            Log.i(TAG,"print onRequestPermission return yes");
            Log.i(TAG,"print permission granted:" + PackageManager.PERMISSION_GRANTED);

            //check if only permission has been granted
            if(grantResults.length==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.i(TAG,"Contacts Permission Granted");

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.myDector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // Visitor Welcome
    public void backToAdminActivity(){
        Intent i = new Intent(getApplicationContext(), AdminActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        backToAdminActivity();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
