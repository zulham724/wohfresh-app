package com.woohfresh.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.activity.ProductsDetailActivity;
import com.woohfresh.data.local.Constants;
import com.woohfresh.models.api.products.state.GProductsState;
import com.woohfresh.models.api.products.state.ProductImagesItem;
import com.woohfresh.models.api.products.state.ProductSalesItem;
import com.woohfresh.models.api.products.state.ProductTranslationsItem;
import com.woohfresh.utils.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.woohfresh.data.sources.remote.api.Apis.URL_STORAGE;

public class ProductsStateAdapter extends RecyclerView.Adapter<ProductsStateAdapter.ViewHolder> {

    private List<GProductsState> gProducts;
    private List<ProductSalesItem> pSales;
    private List<ProductImagesItem> pImages;
    private List<ProductTranslationsItem> pTrans;
    private Context c;
//    String imageSlide = null;
//    public ProductsStateAdapter(List<GProductsState> gProducts,
//                            List<ProductSalesItem> pSales, List<ProductImagesItem> pImages, Context c) {
        public ProductsStateAdapter(List<GProductsState> gProducts, Context c) {
        this.gProducts = gProducts;
        this.pSales = pSales;
        this.pImages = pImages;
//        this.pTrans = pTrans;
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row_fruits, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView llNav;
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
        final GProductsState listProducts = gProducts.get(position);
        final ProductSalesItem listSales = listProducts.getProductSales().get(0);
        final ProductTranslationsItem listTrans = listProducts.getProductTranslations().get(Prefs.getInt(Constants.APP_LANG,0));
//        final ProductImagesItem listImages = listProducts.getProductImages().get(position);
//        final ProductImagesItem listImagesSlides = listProducts.getProductImages().get(1);
//        final ProductTranslationsItem listTrans = pTrans.get(Prefs.getInt(Constants.APP_LANG,0));

        String image = null;
        for(int i = 0;i<listProducts.getProductImages().size();i++)
        {
            if(listProducts.getProductImages().get(i).getImage() != null){
                image = listProducts.getProductImages().get(0).getImage();
//                imageSlide = listProducts.getProductImages().get(1).getImage();
            }
        }

//        if(listImages.getImage() != null){
//            image = listImages.getImage();
//            Log.d("fruitsadapter:img:",image);
//        }

//        String image = listImages.getImage();
        final String title = listTrans.getName();
        final String badge = listProducts.getBadge();
        final String price = String.valueOf(listSales.getPrice());
        final String weight = String.valueOf(listProducts.getWeight());
        final String unit = listProducts.getUnit();
        final String desc = listTrans.getDescription();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.my_bg_img_blank);
        requestOptions.error(R.drawable.err_no_image);
        Glide.with(c)
                .setDefaultRequestOptions(requestOptions)
                .load(URL_STORAGE + image).into(holder.iv);
        holder.tvTitle.setText(title);
        holder.tvPrice.setText("Rp " +App.toRupiah(price));
        holder.tvUnit.setText("/ "+ weight+" "+unit);
        holder.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.TShort("Added");
            }
        });
        holder.llNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProductsDetailActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.proddet_title, title);
                b.putString(Constants.proddet_desc, desc);
                b.putString(Constants.proddet_weight, weight+" "+unit);
                b.putString(Constants.proddet_badge, badge);
                b.putString(Constants.proddet_price, price);
                b.putInt(Constants.proddet_position, position);
//                b.putString(Constants.proddet_img, imageSlide);

                ArrayList<String> al = new ArrayList<String>();
                Collection l = Arrays.asList(listProducts.getProductImages());
                al.addAll(l);
//                Log.d("cekImg", al.toString());

//                String[] arr = listProducts.getProductImages().toArray(new String[] {});
//                b.putStringArray(Constants.proddet_img,arr);

//                ArrayList<ProductImagesItem> listImgs = new ArrayList<ProductImagesItem>();
//                listImgs.addAll(l);

                TinyDB tinydb = new TinyDB(v.getContext());
                for (int img = 0; img < listProducts.getProductImages().size(); img++) {
//            slidesMap.put(tinydb.getListString(Constants.proddet_img).get(i).);
                    tinydb.putObject(Constants.proddet_img, listProducts.getProductImages());
                }
//                tinydb.putObject(Constants.proddet_img, listProducts.getProductImages());

                i.putExtras(b);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        int a;
        if (gProducts != null && !gProducts.isEmpty()) {
            a = gProducts.size();
        } else {
            a = 0;
        }

        return a;
    }
}
