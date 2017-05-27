package com.liuyufei.bmc_android.admin;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.liuyufei.bmc_android.admin.dummy.DummyContent.DummyItem;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.model.Staff;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StaffFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private OnListFragmentInteractionListener mListener;

    ContentResolver contentResolver;
    private static final int URL_LOADER = 0;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StaffFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StaffFragment newInstance(int columnCount) {
        StaffFragment fragment = new StaffFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        setHasOptionsMenu(true);
    }



    Cursor cursor;
    StaffCursorAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_staff_list, container, false);
        contentResolver = view.getContext().getContentResolver();

        ListView lv = (ListView) view;

        adapter = new StaffCursorAdapter(view.getContext(), cursor, false);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //move the cursor to the selected row
                cursor = (Cursor) adapterView.getItemAtPosition(pos);
                //get the object data from the cursor
                int staffID = cursor.getInt(cursor.getColumnIndex(BMCContract.StaffEntry._ID));
                String staffName = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME));
                String staffPhoto = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_PHOTO));
                String staffDepartment = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_DEPARTMENT));
                String staffTitle = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_TITLE));
                String staffMobile = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_MOBILE));
                //create the object that will be passed to the todoActivity
                Staff staff = new Staff(staffID, staffName, staffPhoto, staffDepartment, staffTitle, staffMobile);
                Intent intent = new Intent(getActivity(), EditStaffActivity.class);
                //pass the ID to the todoActivity
                intent.putExtra("staff", staff);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> lc = new CursorLoader(
                getContext(),
                BMCContract.StaffEntry.CONTENT_URI, null, null, null, null);
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



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Search","onQueryTextSubmit....");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("Search","onQueryTextChange....");
                return false;
            }
        });
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
