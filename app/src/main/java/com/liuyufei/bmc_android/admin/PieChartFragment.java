package com.liuyufei.bmc_android.admin;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.liuyufei.bmc_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFragment extends Fragment {


    public PieChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.github.mikephil.charting.charts.PieChart pieChart = new com.github.mikephil.charting.charts.PieChart(this.getActivity());
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
        entries.add(new PieEntry(10.8f, "Purple"));

        PieDataSet set = new PieDataSet(entries, "Appointment in a Week");

        set.setColors(new int[]{
                R.color.green,
                R.color.yellow,
                R.color.red,
                R.color.blue,
                R.color.purple
        },pieChart.getContext());

        PieData data = new PieData(set);
        pieChart.setData(data);
        Description description = new Description();
        description.setText("Appointment Results Description");
        pieChart.setDescription(description);
//        pieChart.invalidate(); // refresh
        return pieChart;
    }

}
