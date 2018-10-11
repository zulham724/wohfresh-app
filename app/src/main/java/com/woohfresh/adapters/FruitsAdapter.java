package com.woohfresh.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.activity.ProductsDetailActivity;
import com.woohfresh.data.local.Constants;
import com.woohfresh.models.api.products.ProductSalesItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.woohfresh.data.sources.remote.api.Apis.URL_STORAGE;

public class FruitsAdapter extends RecyclerView.Adapter<FruitsAdapter.ViewHolder> {

    private List<ProductSalesItem> pSales;
    private Context context;
    String img, title, desc, weight, unit, badge;

    public FruitsAdapter(List<ProductSalesItem> pSales, Context context,
                         String imgs, String titles, String descs, String weights, String units,
                         String badges) {
        this.pSales = pSales;
        this.context = context;
        this.img = imgs;
        this.title = titles;
        this.desc = descs;
        this.weight = weights;
        this.unit = units;
        this.badge = badges;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row_fruits, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llnavProdDet)
        LinearLayout llNav;
        @BindView(R.id.ivFruits)
        ImageView iv;
        @BindView(R.id.rtbFruits)
        RatingBar rtb;
        @BindView(R.id.tvFruitsTitle)
        TextView tvTitle;
        @BindView(R.id.tvFruitsPrice)
        TextView tvPrice;
        @BindView(R.id.tvFruitsUnit)
        TextView tvUnit;
        @BindView(R.id.btnFruitsCart)
        Button btnCart;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d("fruitsadapter:img: ",URL_STORAGE + img);
        final ProductSalesItem requestList = pSales.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.my_bg_img_blank);
        requestOptions.error(R.drawable.err_no_image);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(URL_STORAGE + img).into(holder.iv);
        holder.tvTitle.setText(title);
        holder.tvPrice.setText("Rp " +App.toRupiah(String.valueOf(requestList.getPrice())));
        holder.tvUnit.setText("/ "+weight+" "+unit);
        holder.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.TShort("Added");
            }
        });
        holder.llNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductSalesItem listPSales = pSales.get(position);
                Intent i = new Intent(v.getContext(), ProductsDetailActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.proddet_title, title);
                b.putString(Constants.proddet_desc, desc);
                b.putString(Constants.proddet_weight, weight+" "+unit);
                b.putString(Constants.proddet_badge, badge);
                b.putInt(Constants.proddet_price, listPSales.getPrice());
                i.putExtras(b);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        int a;
        if (pSales != null && !pSales.isEmpty()) {
            a = pSales.size();
        } else {
            a = 0;
        }

        return a;
    }
}
