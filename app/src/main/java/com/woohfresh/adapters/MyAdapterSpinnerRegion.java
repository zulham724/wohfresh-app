package com.woohfresh.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.woohfresh.R;

public class MyAdapterSpinnerRegion extends ArrayAdapter<String> {

    String[] spinnerName, spinnerId;
    Context mContext;

    public MyAdapterSpinnerRegion(@NonNull Context context, String[] ids, String[] names) {
        super(context, R.layout.my_spinner_region);
        this.spinnerName = names;
        this.spinnerId = ids;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return spinnerName.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.my_spinner_region, parent, false);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.tvRegionName);
            mViewHolder.mId = (TextView) convertView.findViewById(R.id.tvRegionId);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mName.setText(spinnerName[position]);
        mViewHolder.mId.setText(spinnerId[position]);

        return convertView;
    }

    private static class ViewHolder {
        TextView mName;
        TextView mId;
    }
}