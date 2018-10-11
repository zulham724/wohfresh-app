package com.woohfresh.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.products.state.GProductsState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvProdDetTitle)
    TextView tvTitle;
    @BindView(R.id.tvProdDetDesc)
    TextView tvDesc;
    @BindView(R.id.tvProdDetWeight)
    TextView tvWeight;
    @BindView(R.id.rtbFruits)
    RatingBar rtb;
    @BindView(R.id.tvProdDetBadge)
    TextView tvBadge;
    @BindView(R.id.tvProdDetPrice)
    TextView tvPrice;
    @BindView(R.id.tvProddetKey)
    TextView tvKey;
    @BindView(R.id.tvProddetAbout)
    TextView tvAbout;
    @BindView(R.id.tvProddetHandling)
    TextView tvHandling;
//    @BindView(R.id.ivProdDet)
//    ImageView iv;
    @BindView(R.id.slider)
    SliderLayout mSlider;

    ArrayList<HashMap<String, String>> contactList;
    App.LoadingPrimary pd;
    RequestOptions requestOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        pd = new App.LoadingPrimary(this);
        Intent b = getIntent();
        tvTitle.setText(b.getStringExtra(Constants.proddet_title));
        tvDesc.setText(b.getStringExtra(Constants.proddet_desc));
        tvWeight.setText(b.getStringExtra(Constants.proddet_weight));
        tvBadge.setText(b.getStringExtra(Constants.proddet_badge));
        tvPrice.setText(b.getStringExtra(Constants.proddet_price));
        tvKey.setText(b.getStringExtra(Constants.proddet_desc));

        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.my_bg_img_blank);
        requestOptions.error(R.drawable.err_no_image);

        String state_id = Prefs.getString(Constants.loc_state_id, "");
        if(!state_id.equals("")) {
            getProductsState(state_id);
        }else {
            App.TShort(getString(R.string.s_proddet_err_no_stateid));
        }
    }

    private void getProductsState(String stateId) {
        String auth = "Bearer " + Prefs.getString(Constants.OAUTH_ACCESS_TOKEN, "");
        Log.d("cAuth", auth);
//        pd.show();
        AndroidNetworking.get(Apis.URL_PRODUCTS_STATE+stateId)
//        AndroidNetworking.get(Apis.URL_PRODUCTS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", auth)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GProductsState.class, new ParsedRequestListener<List<GProductsState>>() {
                    @Override
                    public void onResponse(List<GProductsState> rets) {

                        final GProductsState listProducts = rets.get(getIntent().getIntExtra(Constants.proddet_position,0));

                        String image = null;
                        String desc = "";
                        HashMap<String, String> slidesMap = new HashMap<>();
                        for(int i = 0;i<listProducts.getProductImages().size();i++)
                        {
//                            if(listProducts.getProductImages().get(i).getImage() != null){
//                                slidesMap.put(desc,image);
//                            }
                            image = listProducts.getProductImages().get(i).getImage();
                            desc = String.valueOf(listProducts.getProductImages().get(i).getDescription());
                            slidesMap.put(image, image);
                        }

                        for (String caption : slidesMap.keySet()) {
                            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                            textSliderView
                                    .image(Apis.URL_STORAGE + slidesMap.get(caption))
//                                    .description(caption)
                                    .setRequestOption(requestOptions)
                                    .setProgressBarVisible(true)
                                    .setBackgroundColor(Color.WHITE)
                                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(BaseSliderView baseSliderView) {
//                                            startActivity(new Intent(getApplicationContext(), SliderActivity.class));
                                        }
                                    });
                            mSlider.addSlider(textSliderView);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        pd.dismiss();
                        // handle error
                        App.TShort(error.getErrorDetail());
                        Log.d("cErrorFruits", String.valueOf(error));
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
        }
        return false;
    }
}
