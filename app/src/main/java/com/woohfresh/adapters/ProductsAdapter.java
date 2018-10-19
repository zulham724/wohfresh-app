package com.woohfresh.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.activity.ProductsDetailActivity;
import com.woohfresh.data.local.Constants;
import com.woohfresh.models.api.products.GProducts;
import com.woohfresh.models.api.products.ProductTranslationsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.woohfresh.data.sources.remote.api.Apis.URL_STORAGE;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<GProducts> pSales;
    //    private List<ProductImagesItem> pImages;
    private List<ProductTranslationsItem> pTrans;
    private Context c;
    String title, desc, weight, unit, badge, price;

    public ProductsAdapter(List<GProducts> pSales, Context context) {
        this.pSales = pSales;
//        this.pImages = pImages;
//        this.pTrans = pTrans;
        this.c = context;
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

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

//        final GProducts requestList = pSales.get(position);
//        final ProductTranslationsItem listTrans = requestList.getProductTranslations().get(Prefs.getInt(Constants.APP_LANG,0));
        final GProducts listProducts = pSales.get(position);
//        final ProductSalesItem requestList = listProducts.getProductSales().get(0);
        if (null != listProducts.getProductTranslations()) {
            ProductTranslationsItem listTrans;
            if (listProducts.getProductTranslations().size() == 1) {
                listTrans = listProducts.getProductTranslations().get(0);
                title = listTrans.getName();
                holder.tvTitle.setText(String.valueOf(title));
            } else {
                listTrans = listProducts.getProductTranslations().get(Prefs.getInt(Constants.APP_LANG, 0));
                title = listTrans.getName();
                holder.tvTitle.setText(String.valueOf(title));
            }
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.my_bg_img_blank);
        requestOptions.error(R.drawable.err_no_image);
        if (null != listProducts.getProductImages() && listProducts.getProductImages().size() > 0) {
            Glide.with(c)
                    .setDefaultRequestOptions(requestOptions)
                    .load(URL_STORAGE + listProducts.getProductImages().get(0).getImage()).into(holder.iv);
        }
        if (null != listProducts.getProductSales() && listProducts.getProductSales().size() > 0) {
            price = String.valueOf(listProducts.getProductSales().get(position).getPrice());
            holder.tvPrice.setText("Rp " + App.toRupiah(price));
        }
        weight = String.valueOf(listProducts.getWeight());
        unit = listProducts.getUnit();
        holder.tvUnit.setText("/ " + weight + " " + unit);
        holder.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.TShort(c, "Added");
            }
        });
        holder.llNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProductSalesItem listPSales = pSales.get(position);
                Intent i = new Intent(v.getContext(), ProductsDetailActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.proddet_title, title);
                b.putString(Constants.proddet_desc, desc);
                b.putString(Constants.proddet_weight, weight+" "+unit);
                b.putString(Constants.proddet_badge, badge);
                b.putString(Constants.proddet_price, price);
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
