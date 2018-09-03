package com.woohfresh.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.data.local.Datas;
import com.woohfresh.models.api.GSecret;

import retrofit2.Call;
import retrofit2.Callback;

import static com.woohfresh.data.local.Datas.IS_LOGIN;

public class Splashbe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashbe);
        TextView tv = (TextView)findViewById(R.id.tvSplash);
        tv.setText(Html.fromHtml(getString(R.string.tv_style_app_title)));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if("1".equals(Prefs.getString(IS_LOGIN,""))) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    finish();
                }
            }
        }, 1000);
    }
}
