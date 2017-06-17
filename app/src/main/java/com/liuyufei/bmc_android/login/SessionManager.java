package com.liuyufei.bmc_android.login;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.content.SharedPreferences;
import android.util.Log;
import com.liuyufei.bmc_android.R;
import java.util.HashMap;
/**
 * Created by Administrator on 06/15/17.
 */

public class SessionManager {
    private static String TAG="SessionManager";
    /* Declaration  shared preference */
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public static final String KEY_NAME="name";
    public static final String KEY_MOBILE="mobile";

    public SessionManager(Context context) {
        this._context=context;
        pref=_context.getSharedPreferences(String.valueOf(R.string.PREF_NAME),Context.MODE_PRIVATE);
        editor=pref.edit();
    }

    public void createLoginSession(String name, String mobile){
        /* store preference */
        editor.putBoolean(String.valueOf(R.string.IsLoggedIn),true);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_MOBILE,mobile);
        editor.commit();
    }

    public HashMap<String, String> getUserSession(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME,null));
        user.put(KEY_MOBILE,pref.getString(KEY_MOBILE,null));
        return user;
    }

    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(String.valueOf(R.string.IsLoggedIn), false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        //
        Log.i(TAG,"Logout");
    }

    /**
     * Function to display simple Alert Dialog
     * @param context -application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - true false
     */
    public void showAlertDialog(
            Context context,
            String title,
            String message,
            Boolean status
    ){
        // Declare a dialog
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if(status!=null){
            alertDialog.setIcon((status)? R.drawable.success:R.drawable.fail);
        }
        alertDialog.show();
    }
}
