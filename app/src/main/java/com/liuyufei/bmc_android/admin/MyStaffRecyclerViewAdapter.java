package com.liuyufei.bmc_android.admin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.admin.StaffFragment.OnListFragmentInteractionListener;
import com.liuyufei.bmc_android.admin.dummy.DummyContent.DummyItem;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.model.Staff;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.liuyufei.bmc_android.R.drawable.staff;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyStaffRecyclerViewAdapter extends RecyclerView.Adapter<MyStaffRecyclerViewAdapter.ViewHolder> {

//    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;


    private Context mContext;

    private Cursor mCursor;

    private boolean mDataValid;

    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;

    public MyStaffRecyclerViewAdapter(Context context, Cursor cursor, OnListFragmentInteractionListener listener) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String name = mCursor.getString(mCursor.getColumnIndexOrThrow(BMCContract.StaffEntry.COLUMN_NAME));
//        String priority = mCursor.getString(mCursor.getColumnIndexOrThrow("priority"));.
//        holder.mItem = mValues.get(position);

        Picasso.with(holder.mImgView.getContext())
                .load(R.drawable.trump)
                .error(staff)
                .resize(100,100).centerCrop() //for performance,downsize the pic
                .into(holder.mImgView );

        holder.mIdView.setText("id");
        holder.mContentView.setText(name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(this.getClass().getName(),"Item clicked in the list");
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
                toEditStaff(v);
            }
        });
    }


    private void toEditStaff(View v){

//        Todo todo = new Todo(todoId, todoText, todoCreated, todoExpireDate, boolDone,
//                todoCategory);
        Intent intent = new Intent(v.getContext(), EditStaffActivity.class);
        Staff staff = new Staff(1,"Trump","pathToPhoto","USA","President","1234567");

        //pass the ID to the todoActivity
        intent.putExtra("staff", staff);
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }



    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageView mImgView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mImgView = (ImageView) view.findViewById(R.id.staffImg);
            mContentView = (TextView) view.findViewById(R.id.staffName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
