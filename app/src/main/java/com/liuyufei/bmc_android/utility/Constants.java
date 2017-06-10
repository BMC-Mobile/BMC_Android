package com.liuyufei.bmc_android.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by liuyufei on 9/06/17.
 */

public class Constants {

    public static String STR_ACIVITY_NAME = "AdminActivity";
    public static String WHOES_APPOINTMENT = "ALL";


    public static String getCurrentTimeAsString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
