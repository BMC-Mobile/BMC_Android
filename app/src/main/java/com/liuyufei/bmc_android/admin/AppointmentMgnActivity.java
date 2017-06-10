package com.liuyufei.bmc_android.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.databinding.ActivityAppointmentMgnBinding;
import com.liuyufei.bmc_android.model.Appointment;
import com.liuyufei.bmc_android.model.Staff;
import com.liuyufei.bmc_android.model.Visitor;

public class AppointmentMgnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_mgn);

        Intent intent = getIntent();
        Staff staff = (Staff) intent.getSerializableExtra("staff");
        Visitor visitor = (Visitor) intent.getSerializableExtra("visitor");
        Appointment appointment = (Appointment) intent.getSerializableExtra("appointment");
        ActivityAppointmentMgnBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_mgn);

        binding.setStaff(staff);
        binding.setVisitor(visitor);
        binding.setAppointment(appointment);
    }
}
