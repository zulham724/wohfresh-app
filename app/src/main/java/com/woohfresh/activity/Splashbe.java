package com.woohfresh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.TypedValue;
import android.widget.TextView;

import com.woohfresh.R;

public class Splashbe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashbe);
        TextView tv = (TextView) findViewById(R.id.tvSplash);
        tv.setText(Html.fromHtml(getString(R.string.tv_style_app_title)));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                finish();
            }
        }, 1000);
    }
}
