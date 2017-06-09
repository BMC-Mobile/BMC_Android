package com.liuyufei.bmc_android.admin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.liuyufei.bmc_android.MainActivity;
import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.VisitorWelcomeActivity;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String STAFF = "Staff";
    public static final String APPOINTMENT = "Appointment";
    public static final String PIE = "Pie";
    public static final String BAR = "Bar";
    public static final String VISITOR = "Visitor";
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.staffMag) {
            goManagementFragment(STAFF);
//        } else if (id == R.id.tools) {

        } else if (id == R.id.visitorMag) {
            goManagementFragment(VISITOR);
        } else if (id == R.id.appointmentMag) {
            goManagementFragment(APPOINTMENT);
        } else if (id == R.id.logout) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.statistic_appointment) {
            goCharFragment(PIE);
        }else if (id == R.id.statistic_visitor) {
            goCharFragment(BAR);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goCharFragment(String chartType) {
        fab.hide();
        Fragment fragment;
        if(BAR.equals(chartType)){
            fragment = new BarChartFragment();
        }else{
            fragment = new PieChartFragment();
        }

        setTitle(chartType);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }


    private void goManagementFragment(String mangType) {
        Fragment fragment = null;
        if(STAFF.equals(mangType)){
            fragment = new StaffFragment();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminActivity.this,EditStaffActivity.class);
                    startActivity(intent);
                }
            });
            fab.show();
        }else if(APPOINTMENT.equals(mangType)){
            fragment = new AppointmentFragment();
        }else if(VISITOR.equals(mangType)){
            fragment = new VisitorFragment();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Check  In page called", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminActivity.this,VisitorWelcomeActivity.class);
                    startActivity(intent);
                }
            });
            fab.show();
        }

        Bundle args = new Bundle();
        args.putInt("position", 1);
        fragment.setArguments(args);
        setTitle(mangType);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

    }
}
