package com.woohfresh.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woohfresh.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment {

    @BindView(R.id.tvSignupTermPrivacy)
    TextView mTermPrivacy;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        mTermPrivacy.setText(Html.fromHtml(getString(R.string.signup_term)));
        mTermPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
    }

}
