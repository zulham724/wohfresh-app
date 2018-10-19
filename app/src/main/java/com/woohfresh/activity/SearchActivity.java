package com.woohfresh.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.adapters.ProductsStateAdapter;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.products.state.GProductsState;
import com.woohfresh.models.api.products.state.ProductSalesItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.llSearch)
    LinearLayout ll;
    @BindView(R.id.card_recycler_view)
            RecyclerView recyclerView;
    App.LoadingPrimary pd;
    SearchView searchView;
    Context c;
    private RecyclerView.Adapter adapter;
    private List<ProductSalesItem> pSales;
    List<GProductsState> gProductsState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        pd = new App.LoadingPrimary(this);
        c = this;
        initLL(false);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ProductsStateAdapter(gProductsState,this);
        recyclerView.setAdapter(adapter);
        pSales = new ArrayList<>();
//        gSearch("a");
    }

    private void initLL(Boolean b){
        if(b){
            ll.setVisibility(View.VISIBLE);
        }else {
            ll.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.navFind);

        // Get SearchView object.
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchView.setIconified(false);

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(getResources().getColor(R.color.colorPrimarySearch));
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(R.color.colorPrimaryLight);

        // Create a new ArrayAdapter and add data to search auto complete object.
        String dataArr[] = {"Apple" , "Banana" , "Semangka", "Mangga"};
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
//                Toast.makeText(getApplicationContext(), "you clicked " + queryString, Toast.LENGTH_LONG).show();
                gSearch(queryString);
                initLL(false);
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                AlertDialog alertDialog = new AlertDialog.Builder(SearchActivity.this).create();
//                alertDialog.setMessage("Search keyword is " + query);
//                alertDialog.show();
                gSearch(query);
                initLL(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    //Text is cleared, do your thing
                    initLL(true);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.navCart:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            initLL(true);
        } else {
            super.onBackPressed();
        }
    }

    private void gSearch(String key) {
        String auth = "Bearer " + Prefs.getString(Constants.OAUTH_ACCESS_TOKEN, "");
        Log.d("cAuth", auth);
        pd.show();
        AndroidNetworking.get(Apis.URL_SEARCH+key)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", auth)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GProductsState.class, new ParsedRequestListener<List<GProductsState>>() {
                    @Override
                    public void onResponse(List<GProductsState> rets) {
                        pd.dismiss();
                        for (int i = 0; i < rets.size(); i++) {
                            pSales = rets.get(i).getProductSales();
                            adapter = new ProductsStateAdapter(rets, SearchActivity.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
                        App.TShort(c,error.getErrorDetail());
                        Log.d("cErrorFruits", String.valueOf(error));
                    }
                });
    }
}
