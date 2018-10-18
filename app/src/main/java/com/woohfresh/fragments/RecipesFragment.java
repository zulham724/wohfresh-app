package com.woohfresh.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.activity.RecipesAddActivity;
import com.woohfresh.adapters.RecipesAdapter;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.recipes.GRecipes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.card_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    //    @OnClick(R.id.fabIngredient)
//    public void ocFab() {
//        startActivity(new Intent(getActivity(), RecipesAddActivity.class));
//    }
    @BindView(R.id.fabIngredient)
    FloatingActionButton fabAdd;
    @BindView(R.id.llFabRecipes)
    LinearLayout llFab;

    App.LoadingPrimary pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    private RecyclerView.Adapter adapter;
    private List<GRecipes> myRequestLists;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        pd = new App.LoadingPrimary(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecipesAdapter(myRequestLists, getActivity());
        recyclerView.setAdapter(adapter);
        myRequestLists = new ArrayList<>();
//        initData();

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
                initData();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RecipesAddActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.RECIPES_STATUS, "add");
                i.putExtras(b);
                startActivity(i);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        // Fetching data from server
        initData();
    }

    private void initData() {
        pd.show();
        AndroidNetworking.get(Apis.GET_RECIPES)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GRecipes.class, new ParsedRequestListener<List<GRecipes>>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(List<GRecipes> response) {
                        pd.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        myRequestLists = response;
                        adapter = new RecipesAdapter(myRequestLists, getActivity());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (adapter.getItemCount() == 0) {
                            App.TShort("Belum ada resep");
                            llFab.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        // handle error
                        App.TShort(error.getErrorDetail());
                    }
                });
    }
}