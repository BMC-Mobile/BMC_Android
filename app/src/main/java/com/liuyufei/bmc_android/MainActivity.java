package com.liuyufei.bmc_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.liuyufei.bmc_android.admin.AdminActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.pwd_value);
                String inputPwd = editText.getText().toString();
                if("admin".equals(inputPwd)){
                    Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                    startActivity(intent);
                }else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Incorrect password, input again")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                }
            }
        });
    }
}
