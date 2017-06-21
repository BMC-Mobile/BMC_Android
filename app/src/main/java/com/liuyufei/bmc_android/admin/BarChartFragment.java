package com.liuyufei.bmc_android.admin;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        getActivity().setTitle("Visitor Statistic");

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


        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        Calendar calendar = Calendar.getInstance();

        final List<String> xAxisValues = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR,-i);
            xAxisValues.add(dateFormat.format(calendar.getTime()));
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


        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisValues.get((int) value);
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