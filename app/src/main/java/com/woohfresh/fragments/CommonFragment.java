package com.woohfresh.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.woohfresh.R;

public class CommonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView tvDev = (TextView) view.findViewById(R.id.tvDev);
        tvDev.setText(Html.fromHtml(getString(R.string.dev)));
        YoYo.with(Techniques.Bounce)
                .duration(3000)
                .repeat(-1)
                .playOn(tvDev);
    }
}