package com.liuyufei.bmc_android.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.io.Serializable;

public class Visitor implements Serializable {

    public static final ObservableInt id = new ObservableInt();
    public final ObservableField<String> name = new ObservableField<String>();
    public final ObservableField<String> bussinessname = new ObservableField<String>();
    public final ObservableField<String> mobile = new ObservableField<String>();
    public final ObservableField<String> creationtime = new ObservableField<String>();
    public final ObservableField<String> lastlogintime = new ObservableField<String>();




    public Visitor(int id,String name,String bussinessname,String mobile,String creationtime,String lastlogintime){
        this.id.set(id);
        this.name.set(name);
        this.bussinessname.set(bussinessname);
        this.mobile.set(mobile);
        this.creationtime.set(creationtime);
        this.lastlogintime.set(lastlogintime);
    }
}
