package com.liuyufei.bmc_android.admin;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFragment extends Fragment {


    public PieChartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.github.mikephil.charting.charts.PieChart pieChart = new com.github.mikephil.charting.charts.PieChart(this.getActivity());


        String selection = BMCContract.AppointmentEntry.COLUMN_DATETIME+" IS NOT NULL) GROUP BY (" + BMCContract.AppointmentEntry.COLUMN_DATETIME;
        String[] selectArgs = null;
        String[] projection = {
                "count("+ BMCContract.AppointmentEntry.TABLE_NAME+"._id)",
                "strftime('%d/%m', "+BMCContract.AppointmentEntry.COLUMN_DATETIME+") as appdate"

        };

        //show last 5 days
        Cursor cursor = getActivity().getContentResolver()
                .query(BMCContract.AppointmentEntry.CONTENT_URI, projection, selection, selectArgs, "appdate desc limit 7");

        List<PieEntry> entries = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                entries.add(new PieEntry(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
//                Log.i("GET Appointment", cursor.getString(0));
//                Log.i("GET Appointment", cursor.getString(1));
            }

            PieDataSet set = new PieDataSet(entries, "Appointment in Last Week");

            set.setColors(new int[]{
                    R.color.green,
                    R.color.yellow,
                    R.color.red,
                    R.color.blue,
                    R.color.pink,
                    R.color.orange,
                    R.color.purple
            }, pieChart.getContext());

            PieData data = new PieData(set);
            pieChart.setData(data);
            Description description = new Description();
            description.setText("Appointment Results Description");
            pieChart.setDescription(description);

//        pieChart.invalidate(); // refresh
        }

        return pieChart;
    }

}
//SELECT count(BMC_APPOINTMENT._id), strftime('%d-%m-%Y', app_datetime) as appdate FROM BMC_APPOINTMENT inner join BMC_STAFF on staff = BMC_STAFF._id inner join BMC_VISITOR on visitor = BMC_VISITOR._id WHERE (app_datetime IS NOT NULL) GROUP BY (app_datetime) ORDER BY appdate desc limit 7