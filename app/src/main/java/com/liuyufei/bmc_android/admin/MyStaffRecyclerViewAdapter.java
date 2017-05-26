package com.liuyufei.bmc_android.admin;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuyufei.bmc_android.EditStaff;
import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.admin.StaffFragment.OnListFragmentInteractionListener;
import com.liuyufei.bmc_android.admin.dummy.DummyContent.DummyItem;
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

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyStaffRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Picasso.with(holder.mImgView.getContext())
                .load(R.drawable.trump)
                .error(staff)
                .resize(100,100).centerCrop() //for performance,downsize the pic
                .into(holder.mImgView );

        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

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
        Intent intent = new Intent(v.getContext(), EditStaff.class);
        Staff staff = new Staff(1,"Trump","pathToPhoto","USA","President","1234567");

        //pass the ID to the todoActivity
        intent.putExtra("staff", staff);
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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
