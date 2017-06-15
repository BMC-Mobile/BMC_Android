package com.liuyufei.bmc_android.login;

import android.content.pm.ActivityInfo;
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
import android.widget.Toast;

import com.liuyufei.bmc_android.MainActivity;
import com.liuyufei.bmc_android.admin.AdminActivity;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.model.Staff;

/**
 * Created by Ying Chen 20170610
 */
public class LoginActivity extends AppCompatActivity {

    static String DEFAULT_PASSWORD = "123456";
    static String TAG = "LoginActivity";
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
                int taux_max = 200;
                int val = 200;
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
                btnRing.setTextColor(getResources().getColor(R.color.colorButtonFont));
            }
        });
    }

    String _mobile;
    String _password;

    private void userLogin() {

        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                try {
                    if (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME));
                        String mobileNumber = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME));

                        if (name.trim().length() > 0 && _mobile.trim().length() > 0) {
                            // create login session
                            if (_password.trim().equals(DEFAULT_PASSWORD)) {
                                session.createLoginSession(name, mobileNumber);
                                Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Log.i(TAG, "password doesn't match");
                                session.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect", false);
                            }
                        }
                    } else {
                        session.showAlertDialog(LoginActivity.this, "Login failed..", "Input mobile:" + _mobile + " doesn't exist", false);
                    }

                } finally {
                    cursor.close();
                }
            }
        };
        String selection = BMCContract.StaffEntry.COLUMN_MOBILE + "=?";
        String[] args = {_mobile};
        String orderBy = BMCContract.StaffEntry.COLUMN_MOBILE + " DESC";

        // query data when it is done, call on query complete
        queryHandler.startQuery(0, null, BMCContract.StaffEntry.CONTENT_URI, null, selection, args, orderBy);
    }
}