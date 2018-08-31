package com.woohfresh.activity;

import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.data.local.Datas;
import com.woohfresh.data.sources.remote.api.ApiService;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.fragments.SignIn;
import com.woohfresh.fragments.SignUp;
import com.woohfresh.models.api.GSecret;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class AuthActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(Html.fromHtml(getString(R.string.tv_style_app_title)));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,25f);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        initSecret();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignIn(), "Log In");
        adapter.addFragment(new SignUp(), "Sign Up");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }else {
                    finish();
                }
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void initSecret() {
        App.mApiService.gSecret().enqueue(new Callback<GSecret>() {
            @Override
            public void onResponse(Call<GSecret> call, retrofit2.Response<GSecret> response) {
                if (response.isSuccessful()) {
                    Prefs.putString(Datas.APP_CLIENT_ID, String.valueOf(response.body().getId()));
                    Prefs.putString(Datas.APP_CLIENT_SECRET, response.body().getSecret());
                } else {
                    App.TShort(getString(R.string.err_server));
                }

            }

            @Override
            public void onFailure(Call<GSecret> call, Throwable t) {
                App.TShort(t.getMessage());
            }
        });
    }

}
