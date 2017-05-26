package com.liuyufei.bmc_android.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.databinding.ActivityEditStaffBinding;
import com.liuyufei.bmc_android.model.Staff;

public class EditStaffActivity extends AppCompatActivity {

    Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);

        Intent intent = getIntent();
        staff = (Staff)intent.getSerializableExtra("staff");
        ActivityEditStaffBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_edit_staff);
        binding.setStaff(staff);
    }
}
