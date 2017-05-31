package com.liuyufei.bmc_android.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.io.Serializable;

public class Appointment implements Serializable {

    public static final ObservableInt id = new ObservableInt();
    public final ObservableField<String> desc = new ObservableField<String>();
    public final ObservableField<String> meetingTime = new ObservableField<String>();


    public Appointment(int id, String desc, String meetingTime){
        this.id.set(id);
        this.desc.set(desc);
        this.meetingTime.set(meetingTime);
    }
}
