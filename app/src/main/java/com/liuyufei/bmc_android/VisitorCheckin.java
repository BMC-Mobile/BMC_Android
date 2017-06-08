package com.liuyufei.bmc_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.liuyufei.bmc_android.data.DatabaseHelper;
import com.liuyufei.bmc_android.model.Appointment;
import com.liuyufei.bmc_android.model.Visitor;


public class VisitorCheckin extends AppCompatActivity {

    EditText name_txt;
    EditText company_txt;
    EditText contact_txt;
    Spinner purpose_spi;
    Spinner staff_spi;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_checkin);

        name_txt = (EditText) findViewById(R.id.name_txt);
        company_txt = (EditText) findViewById(R.id.company_txt);
        contact_txt = (EditText) findViewById(R.id.contact_txt);
        purpose_spi = (Spinner) findViewById(R.id.purpose_spi);
        staff_spi = (Spinner) findViewById(R.id.staff_spi);
//        dbHelper = new DatabaseHelper(this, null, getContext(), 1);
    }

    public void checkIn(){
//        Visitor visitor = new Visitor(name_txt.getText().toString());
//        Visitor visitor2 = new Visitor(company_txt.getText().toString());
//        Visitor visitor3 = new Visitor(contact_txt.getText().toString());
//        Appointment appointment = new Appointment(purpose_txt.getText().toString());
//        dbHelper.onCreate();
        
    }
}
