package com.liuyufei.bmc_android.admin;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.VisitorCheckIn;
import com.liuyufei.bmc_android.VisitorWelcomeActivity;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.data.BMCQueryHandler;

import static com.liuyufei.bmc_android.data.BMCContract.CHECKIN;
import static com.liuyufei.bmc_android.data.BMCContract.CHECKOUT;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    ContentResolver contentResolver;
    private static final int URL_LOADER = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }


    Cursor cursor;
    VisitorCursorAdapter adapter;
    ListView lv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visitor_list, container, false);
        contentResolver = view.getContext().getContentResolver();
        lv = (ListView) view;
        adapter = new VisitorCursorAdapter(getContext(), cursor, false);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //move the cursor to the selected row
                cursor = (Cursor) adapterView.getItemAtPosition(pos);
                //get the object data from the cursor
                final int visitorID = cursor.getInt(cursor.getColumnIndex(BMCContract.VisitorEntry._ID));
                String visitorName = cursor.getString(cursor.getColumnIndex(BMCContract.VisitorEntry.COLUMN_NAME));


                Intent intentToAppointmentForm = new Intent(getActivity(),VisitorCheckIn.class);
                intentToAppointmentForm.putExtra("check_status",CHECKOUT);
                intentToAppointmentForm.putExtra("visitorID",visitorID);
                intentToAppointmentForm.putExtra("visitorName",visitorName);

                startActivity(intentToAppointmentForm);

            }
        });

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //change selection
        Log.i("onCreateLoader", "onCreateLoader selection changed");
        Uri resourceUri = BMCContract.VisitorEntry.CONTENT_URI;
        String[] selectionArgs = {CHECKIN.toString()};
        String selection = BMCContract.VisitorEntry.COLUMN_CHECK_STATUS+"=?";
        String orderBy = BMCContract.VisitorEntry.COLUMN_LASTLOGIN_TIME+" DESC";

        if (args != null) {
            selectionArgs = args.getStringArray("selectionArgs");
            selection = args.getString("selection");
            resourceUri = BMCContract.VisitorEntry.CONTENT_URI;
        }

        //filter visitors who forget to checkout
        Loader<Cursor> lc = new CursorLoader(getActivity(), resourceUri, null, selection, selectionArgs, orderBy);
        return lc;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    Handler handler = new Handler();
    boolean canRun = true;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String input = newText;
                if (newText.length() > 1) {
                    String selection = BMCContract.VisitorEntry.COLUMN_NAME + " like ?";
                    String[] selectionArgs = {"%" + input + "%"};
                    Bundle bundle = new Bundle();
                    bundle.putString("selection", selection);
                    bundle.putStringArray("selectionArgs", selectionArgs);
                    getLoaderManager().restartLoader(URL_LOADER, bundle, VisitorFragment.this);
                } else {
                    //select all records
                    getLoaderManager().restartLoader(URL_LOADER, null, VisitorFragment.this);
                }
                return false;
            }
        });
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }


}
