package com.woohfresh.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woohfresh.R;
import com.woohfresh.models.api.subcategories.GSubcategories;
import com.woohfresh.models.api.subcategories.SubCategoryTranslationsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.woohfresh.data.sources.remote.api.Apis.URL_STORAGE;

public class ProductsSubcategoriesAdapter extends RecyclerView.Adapter<ProductsSubcategoriesAdapter.ViewHolder> {

    private List<GSubcategories> gSub;
    private List<SubCategoryTranslationsItem> pTrans;
    private Context context;

    public ProductsSubcategoriesAdapter(List<GSubcategories> pSales, Context context) {
        this.gSub = pSales;
//        this.pImages = pImages;
//        this.pTrans = pTrans;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row_fruits, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.llnavProdDet)
//        LinearLayout llNav;
        @BindView(R.id.ivFruits)
        ImageView iv;
        //        @BindView(R.id.rtbFruits)
//        RatingBar rtb;
        @BindView(R.id.tvFruitsTitle)
        TextView tvTitle;
        @BindView(R.id.tvFruitsPrice)
        TextView tvPrice;
//        @BindView(R.id.tvFruitsUnit)
//        TextView tvUnit;
//        @BindView(R.id.btnFruitsCart)
//        Button btnCart;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
//            tvTitle = (TextView)v.findViewById(R.id.tvFruitsTitle);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final GSubcategories requestList = gSub.get(position);
//        final ProductTranslationsItem listTrans = pTrans.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.my_bg_img_blank);
        requestOptions.error(R.drawable.err_no_image);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(URL_STORAGE + requestList.getImage()).into(holder.iv);
        holder.tvTitle.setText(String.valueOf(requestList.getName()));
//        holder.tvPrice.setText("Rp " +App.toRupiah(String.valueOf(requestList.getPrice())));
//        holder.tvUnit.setText("/ "+weight+" "+unit);
//        holder.btnCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                App.TShort("Added");
//            }
//        });
//        holder.llNav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ProductSalesItem listPSales = pSales.get(position);
//                Intent i = new Intent(v.getContext(), ProductsDetailActivity.class);
//                Bundle b = new Bundle();
//                b.putString(Constants.proddet_title, title);
//                b.putString(Constants.proddet_desc, desc);
//                b.putString(Constants.proddet_weight, weight+" "+unit);
//                b.putString(Constants.proddet_badge, badge);
//                b.putInt(Constants.proddet_price, listPSales.getPrice());
//                i.putExtras(b);
//                v.getContext().startActivity(i);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        int a;
        if (gSub != null && !gSub.isEmpty()) {
            a = gSub.size();
        } else {
            a = 0;
        }

        return a;
    }
}
