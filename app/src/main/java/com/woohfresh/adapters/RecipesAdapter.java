package com.woohfresh.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.activity.RecipesAddActivity;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.recipes.GRecipes;
import com.woohfresh.models.api.user.GUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.woohfresh.data.sources.remote.api.Apis.URL_STORAGE;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<GRecipes> requestLists;
    private Context context;

    public RecipesAdapter(List<GRecipes> requestLists, Context context) {

        this.requestLists = requestLists;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row_ingredient, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvIngredientProfileName)
        TextView tvProfile;
        @BindView(R.id.tvIngredientTitle)
        TextView tvTitle;
        @BindView(R.id.tvIngredientDesc)
        TextView tvDesc;
        @BindView(R.id.tvIngredientComment)
        TextView tvComment;
        @BindView(R.id.rtbRowRecipes)
        RatingBar rbRecipes;
        @BindView(R.id.ivRecipeImage)
        ImageView ivRecipes;
        @BindView(R.id.ivIngredient)
        ImageView ivIngredient;
        @BindView(R.id.cvMyRequest)
        CardView mMyRequest;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GRecipes requestList = requestLists.get(position);
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.my_bg_img_blank);
        requestOptions.error(R.drawable.err_no_image);
        AndroidNetworking.get(Apis.GET_USER_BY_ID+requestList.getUserId())
//        AndroidNetworking.get(Apis.GET_USER_BY_ID)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(GUser.class, new ParsedRequestListener<GUser>() {
                    @Override
                    public void onResponse(GUser response) {
                        holder.tvProfile.setText(response.getName());
                        if(null != response.getAvatar()){
                            Glide.with(context)
                                    .load(URL_STORAGE + response.getAvatar())
                                    .into(holder.ivIngredient);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        App.TShort(error.getErrorDetail());
                    }
                });
        holder.tvTitle.setText(requestList.getName());
        holder.tvDesc.setText(requestList.getDescription());
        holder.rbRecipes.setRating(requestList.getDifficultyLevel());
        if(requestList.getRecipeImages().size() > 0) {
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(URL_STORAGE + requestList.getRecipeImages().get(0).getImage()).into(holder.ivRecipes);
        }
        holder.tvComment.setText("Comment "+requestList.getRecipeComments().size());
        holder.mMyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRecipes requestList1 = requestLists.get(position);
                String user_id = Prefs.getString(Constants.G_USER_ID, "");
                Intent i = new Intent(v.getContext(), RecipesAddActivity.class);
                Bundle b = new Bundle();
                if(user_id.equals(String.valueOf(requestList1.getUserId()))){
                    b.putString(Constants.RECIPES_STATUS, "edit");
                }else{
                    b.putString(Constants.RECIPES_STATUS, "show");
                }
                b.putString(Constants.RECIPES_ID, String.valueOf(requestList1.getId()));
                b.putString(Constants.RECIPES_USER_ID, String.valueOf(requestList1.getUserId()));
                b.putString(Constants.RECIPES_NAME, requestList1.getName());
                b.putString(Constants.RECIPES_DESCRIPTION, requestList1.getDescription());
                b.putString(Constants.RECIPES_TUTORIAL, requestList1.getTutorial());
                b.putString(Constants.RECIPES_DIFFICULTY_LEVEL, String.valueOf(requestList1.getDifficultyLevel()));
                b.putString(Constants.RECIPES_PREPARATION_TIME, String.valueOf(requestList1.getPreparationTime()));
                b.putString(Constants.RECIPES_PORTION_PER_SERVE, String.valueOf(requestList1.getPortionPerServe()));
                i.putExtras(b);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        int a;
        if (requestLists != null && !requestLists.isEmpty()) {
            a = requestLists.size();
        } else {
            a = 0;
        }

        return a;
    }
}
