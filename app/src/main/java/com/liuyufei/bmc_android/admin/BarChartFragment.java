package com.liuyufei.bmc_android.admin;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.liuyufei.bmc_android.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarChartFragment extends Fragment {


    public BarChartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        com.github.mikephil.charting.charts.BarChart chart = new com.github.mikephil.charting.charts.BarChart(this.getActivity());


        // the labels that should be drawn on the XAxis
        final ArrayList<String> labels = new ArrayList<String>();
        labels.add("Monday");
        labels.add("Tuesday");
        labels.add("Wednesday");
        labels.add("Thursday");
        labels.add("Friday");

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0,4f));
        entries.add(new BarEntry(1,8f));
        entries.add(new BarEntry(2,6f));
        entries.add(new BarEntry(3,12f));
        entries.add(new BarEntry(4,18f));


        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        BarDataSet dataset = new BarDataSet(entries, "Appointment in One Week");

        dataset.setColors(new int[]{
                R.color.green,
                R.color.yellow,
                R.color.red,
                R.color.blue,
                R.color.purple
        },chart.getContext());

        BarData data = new BarData(dataset);
        chart.setData(data);
//        Description description = new Description();
//        description.setText("# of times Alice called Bob");
//        chart.setDescription(description);

        return chart;
    }

}
