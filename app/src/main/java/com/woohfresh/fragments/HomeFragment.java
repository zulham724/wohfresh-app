package com.woohfresh.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.woohfresh.App;
import com.woohfresh.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ScrollView sv = (ScrollView)view.findViewById(R.id.dSV);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.TShort("coming soon ;) we are still working in an awesome project");
            }
        });
    }
}