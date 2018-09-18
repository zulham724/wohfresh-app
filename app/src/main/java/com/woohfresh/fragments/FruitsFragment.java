package com.woohfresh.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.adapter.FruitsAdapter;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.products.GProducts;
import com.woohfresh.models.api.products.ProductSalesItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.woohfresh.data.local.Constants.APP_LANG;

public class FruitsFragment extends Fragment {

    @BindView(R.id.card_recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fruits, container, false);
    }

    private RecyclerView.Adapter adapter;
    private List<ProductSalesItem> pSales;
    private App.LoadingPrimary pd;

    String img, badge, title, weight, unit;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        pd = new App.LoadingPrimary(getActivity());
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new FruitsAdapter(pSales, getActivity(), img, badge, title, weight, unit);
        recyclerView.setAdapter(adapter);
        pSales = new ArrayList<>();
//        initProducts();
    }

    private void initProducts() {
        String auth = "Bearer " + Prefs.getString(Constants.OAUTH_ACCESS_TOKEN, "");
        Log.d("cAuth", auth);
        pd.show();
        AndroidNetworking.get(Apis.URL_PRODUCTS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", auth)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GProducts.class, new ParsedRequestListener<List<GProducts>>() {
                    @Override
                    public void onResponse(List<GProducts> response) {
                        pd.dismiss();

                        for (GProducts ret : response) {
                            pSales = ret.getProductSales();
                            List images = new ArrayList();
                            String image = "";
                            if(images.size() > 0){
                                image = ret.getProductImages().get(0).getImage();
                            }
                            adapter = new FruitsAdapter(pSales, getActivity(),
                                    image,
                                    ret.getBadge(), ret.getProductTranslations()
                                    .get(Prefs.getInt(APP_LANG,0)).getName(),
                                    String.valueOf(ret.getWeight()), ret.getUnit());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
                        App.TShort(error.getErrorDetail());
                        Log.d("cErrorFruits",String.valueOf(error));
                    }
                });
    }
}