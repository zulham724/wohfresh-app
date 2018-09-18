package com.woohfresh.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.woohfresh.R;
import com.woohfresh.adapter.MyAdapterSettings;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment {

    @BindView(R.id.lvSettings)
    ListView lvSettings;

    String Item[];
    String SubItem[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        Item = getActivity().getResources().getStringArray(R.array.arr_settings_title);
        SubItem = getActivity().getResources().getStringArray(R.array.arr_settings_sub);
        MyAdapterSettings customAdapter = new MyAdapterSettings(getActivity(), Item,SubItem);
        lvSettings.setAdapter(customAdapter);
    }
}