package com.liuyufei.bmc_android.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuyufei.bmc_android.R;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.liuyufei.bmc_android.MainActivity;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.model.Staff;

/**
 *  Created by Ying Chen 20170610
 * */
public class LoginActivity extends AppCompatActivity {

    static String DEFAULT_PASSWORD = "1234";
    static String TAG = "LoginActivity";

    // ImageView
    ImageView image;

    // Email, password edittext
    EditText txtUsername, txtPassword;
    // login button
    Button btnLogin;
    Button btnRing;
    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Session Manager
        session = new SessionManager(getApplicationContext());

        image = (ImageView) findViewById(R.id.imgLogo);

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);

        // Show session
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        btnLogin = (Button) findViewById(R.id.login_button);
        btnRing = (Button) findViewById(R.id.ring_button);

        // Set click Listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                // Check if username, password is filled
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    _mobile = username;
                    _password = password;
                    userLogin();
                } else {
                    Log.i(TAG, "password is empty");
                    session.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password", false);
                }
            }
        });

        // Set click Ring
        btnRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"User Click Ring");
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    requestCameraPermission();
                    return ;
                }

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0277777006"));
                startActivity(intent);
            }
        });
    }

    private static final int REQUEST_CALL_PHONE = 0;

    public void requestCameraPermission(){
        Log.i(TAG,"requestCameraPermission -- start --");
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL_PHONE);
        }else{
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL_PHONE);
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

    String _mobile;
    String _password;

    private void userLogin(){

        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()){
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                Staff staff = new Staff(0,null,null,null,null,null);
                try{
                    if(cursor.moveToNext()){
                        Integer id = cursor.getInt(cursor.getColumnIndex(BMCContract.StaffEntry._ID));
                        String name = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME));
                        String mobileNumber = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME));
                        String department=cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_DEPARTMENT));
                        String photo=cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_PHOTO));
                        String title=cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_TITLE));

                        if (name.trim().length()>0&&_mobile.trim().length()>0){
                            // create login session
                            if(_password.trim().equals(DEFAULT_PASSWORD)){
                                session.createLoginSession(name,mobileNumber);
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }else {
                                Log.i(TAG,"password doesn't match");
                                session.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect", false);
                            }
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        };
        String selection = BMCContract.StaffEntry.COLUMN_MOBILE + "=?";
        String[] args = {_mobile};
        String orderBy = BMCContract.StaffEntry.COLUMN_MOBILE+" DESC";

        // query data when it is done, call on query complete
        queryHandler.startQuery(0, null, BMCContract.StaffEntry.CONTENT_URI, null, selection, args, orderBy);
    }
}