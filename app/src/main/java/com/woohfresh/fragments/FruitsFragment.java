package com.woohfresh.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.adapters.ProductsAdapter;
import com.woohfresh.adapters.ProductsStateAdapter;
import com.woohfresh.adapters.ProductsSubcategoriesAdapter;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.products.GProducts;
import com.woohfresh.models.api.products.ProductTranslationsItem;
import com.woohfresh.models.api.products.state.GProductsState;
import com.woohfresh.models.api.products.state.ProductImagesItem;
import com.woohfresh.models.api.products.state.ProductSalesItem;
import com.woohfresh.models.api.search.state.GStateId;
import com.woohfresh.models.api.subcategories.GSubcategories;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FruitsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LocationListener {

    //    @BindView(R.id.card_recycler_view)
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.Adapter adapter;
    private List<ProductSalesItem> pSales;
    private List<ProductImagesItem> pImages;
    List<GProductsState> gProductsState;
    private List<ProductTranslationsItem> pTrans;
    String img, title, desc, weight, unit, badge;
    Context mContext;

//    private List<GProductsState> gProductsState;
//    private List<ProductSalesItem> pSales;
//    private List<ProductImagesItem> pImages;
//    private List<ProductTranslationsItem> pTrans;

    //getCurrentLoc
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude, longitude;
    App.LoadingPrimary pd;
    Boolean IS_LOC = false;

    @BindView(R.id.spFilter)
    Spinner spFilter;
    String sFilter,selected_item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        pd = new App.LoadingPrimary(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fruits, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ArrayAdapter adapterFilter = ArrayAdapter.createFromResource(getActivity(),
                R.array.arr_filter, R.layout.my_spinner_filter);
//        adapterFilter.setDropDownViewResource(R.layout.my_spinner_filter);
//        ArrayAdapter adapterFilter = ArrayAdapter.createFromResource(getActivity(), R.array.arr_filter,
//                android.R.layout.simple_spinner_dropdown_item);
//        adapterFilter.setDropDownViewResource(R.layout.my_spinner_filter);
        selected_item = "All Categories";
        spFilter.setAdapter(adapterFilter);
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                selected_item = adapter.getItemAtPosition(position).toString();
                initProduct();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        sFilter = spFilter.getSelectedItem().toString();

        recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
//        adapter = new ProductsStateAdapter(gProductsState, getActivity());
//        recyclerView.setAdapter(adapter);
//        pSales = new ArrayList<>();

//        adapter = new ProductsAdapter(pSales,getActivity());
//        gProductsState = new ArrayList<>();
//        pImages = new ArrayList<>();

//        initProduct();
//        if (CheckPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
//            // you have permission go ahead
//            initLocation();
//        } else {
//            // you do not have permission go request runtime permissions
//            RequestPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION);
//        }

//        getProductsState();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                initProduct();
            }
        });

        getLocation();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        App.TShort(mContext,"Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        if (location != null) {
            if (!IS_LOC) {
                IS_LOC = true;
                try {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    getStateId(addresses.get(0).getAdminArea());
                    App.TShort(mContext,"Lokasi anda : "+addresses.get(0).getAdminArea());
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if(sFilter.equals("Current Location")){
            App.TShort(mContext,"Please Enable GPS and Internet");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onRefresh() {
        // Fetching data from server
        initProduct();
    }

    private void initProduct() {
        if(selected_item.equals("Current Location")){
            String state_id = Prefs.getString(Constants.G_state_id, "");
            if (!state_id.equals("")) {
                getProductsState(state_id);
            } else {
                App.TShort(mContext,"No State Id");
                getLocation();
            }
        }else  if(selected_item.equals("Subcategories")){
            getSub();
        }else {
            getAllCategories();
        }
    }

    private void getSub() {
        pd.show();
        String auth = "Bearer " + Prefs.getString(Constants.OAUTH_ACCESS_TOKEN, "");
        AndroidNetworking.get(Apis.URL_SUBCATEGORIES)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", auth)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GSubcategories.class, new ParsedRequestListener<List<GSubcategories>>() {
                    @Override
                    public void onResponse(List<GSubcategories> rets) {
                        pd.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        for (int i = 0; i < rets.size(); i++) {
//                            List<GSubcategories> gSubcategories;
//                            gSubcategories = rets.get(i);
//                            pTrans = ret.getProductTranslations();
//                            pImages = rets.get(i).getProductImages();
                            adapter = new ProductsSubcategoriesAdapter(rets, getActivity());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
                        App.TShort(mContext,error.getErrorDetail());
                        Log.d("cErrorFruits", String.valueOf(error));
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void getAllCategories() {
        pd.show();
        String auth = "Bearer " + Prefs.getString(Constants.OAUTH_ACCESS_TOKEN, "");
        Log.d("cAuth", auth);
        AndroidNetworking.get(Apis.URL_PRODUCTS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", auth)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GProducts.class, new ParsedRequestListener<List<GProducts>>() {
                    @Override
                    public void onResponse(List<GProducts> rets) {
                        pd.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        for (int i = 0; i < rets.size(); i++) {
//                            pProductSales= rets.get(i).getProductSales();
//                            pTrans = ret.getProductTranslations();
//                            pImages = rets.get(i).getProductImages();
                            adapter = new ProductsAdapter(rets, getActivity());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
                        App.TShort(mContext,error.getErrorDetail());
                        Log.d("cErrorFruits", String.valueOf(error));
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void getProductsState(String stateId) {
        pd.show();
        String auth = "Bearer " + Prefs.getString(Constants.OAUTH_ACCESS_TOKEN, "");
        Log.d("cAuth", auth);
//        pd.show();
        AndroidNetworking.get(Apis.URL_PRODUCTS_STATE + stateId)
//        AndroidNetworking.get(Apis.URL_PRODUCTS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", auth)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GProductsState.class, new ParsedRequestListener<List<GProductsState>>() {
                    @Override
                    public void onResponse(List<GProductsState> rets) {
                        pd.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        for (int i = 0; i < rets.size(); i++) {
                            pSales = rets.get(i).getProductSales();
//                            pTrans = ret.getProductTranslations();
//                            pImages = rets.get(i).getProductImages();
                            adapter = new ProductsStateAdapter(rets, getActivity());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
                        App.TShort(mContext,error.getErrorDetail());
                        Log.d("cErrorFruits", String.valueOf(error));
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public void getStateId(String stateId) {
        pd.show();
        AndroidNetworking.get(Apis.URL_SEARCH_STATE + "{stateId}")
                .addPathParameter("stateId", stateId)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GStateId.class, new ParsedRequestListener<List<GStateId>>() {
                    @Override
                    public void onResponse(List<GStateId> rets) {
                        // do anything with response
                        for (GStateId ret : rets) {
                            Prefs.putString(Constants.G_state_id, String.valueOf(ret.getId()));
                            getProductsState(String.valueOf(ret.getId()));
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pd.dismiss();
                        App.TShort(mContext,anError.getErrorDetail());
                    }
                });
    }
}