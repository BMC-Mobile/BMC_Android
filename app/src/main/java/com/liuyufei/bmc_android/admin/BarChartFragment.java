package com.liuyufei.bmc_android.admin;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarChartFragment extends Fragment {


    public static final int days = 7;

    public BarChartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<Entry> newVisitors = new ArrayList<>();
        List<Entry> oldVisitors = new ArrayList<>();

        // Inflate the layout for this fragment
        com.github.mikephil.charting.charts.LineChart mLineChart = new com.github.mikephil.charting.charts.LineChart(this.getActivity());

        String selectionNewVisitors = BMCContract.VisitorEntry.COLUMN_LASTLOGIN_TIME + " == " + BMCContract.VisitorEntry.COLUMN_CREATION_TIME + " GROUP BY lasttime";
        String selectionOldVisitors = BMCContract.VisitorEntry.COLUMN_LASTLOGIN_TIME + " != " + BMCContract.VisitorEntry.COLUMN_CREATION_TIME + " GROUP BY lasttime";
        String[] selectArgs = null;
        String[] projection = {
                "count(" + BMCContract.VisitorEntry.TABLE_NAME + "._id)",
                "strftime('%d-%m-%Y', " + BMCContract.VisitorEntry.COLUMN_LASTLOGIN_TIME + ") as lasttime"

        };

        //show new visitors last 7 days
        Cursor cursor_new = getActivity().getContentResolver()
                .query(BMCContract.VisitorEntry.CONTENT_URI, projection, selectionNewVisitors, selectArgs, "lasttime desc limit "+days);

        //show old visitors 7 days
        Cursor cursor_old = getActivity().getContentResolver()
                .query(BMCContract.VisitorEntry.CONTENT_URI, projection, selectionOldVisitors, selectArgs, "lasttime desc limit "+days);


        for (int i = 0; i < days; i++) {
            if (cursor_new.moveToNext()) {
                newVisitors.add(new Entry(i, Integer.parseInt(cursor_new.getString(0))));
            } else {
                newVisitors.add(new Entry(i, 0));
            }

            if (cursor_old.moveToNext()) {
                oldVisitors.add(new Entry(i, Integer.parseInt(cursor_old.getString(0))));
            } else {
                oldVisitors.add(new Entry(i, 0));
            }
        }

        LineDataSet newVisitorSet = new LineDataSet(newVisitors, "New Visitors");
        newVisitorSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineDataSet oldVisitorSet = new LineDataSet(oldVisitors, "Old Visitors");
        oldVisitorSet.setAxisDependency(YAxis.AxisDependency.LEFT);


        // the labels that should be drawn on the XAxis
        final String[] quarters = new String[]{"Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Q7"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        YAxis left = mLineChart.getAxisLeft();
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false);
        left.setGranularity(1f); // interval 1
        mLineChart.getAxisRight().setEnabled(false); // no right axis

        newVisitorSet.setColors(new int[]{
                R.color.red,
        }, mLineChart.getContext());

        oldVisitorSet.setColors(new int[]{
                R.color.blue,
        }, mLineChart.getContext());


        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(newVisitorSet);
        dataSets.add(oldVisitorSet);

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate(); // refresh

        return mLineChart;
    }

}
//    new visitors in last week
//    select count(*),strftime("%Y-%m-%d",last_login_time) as lastTime from  bmc_visitor where creation_time == last_login_time group by lastTime limit 7
//    old visitors in last week
//    select count(*),strftime("%Y-%m-%d",last_login_time) as lastTime from  bmc_visitor where creation_time != last_login_time group by lastTime limit 7