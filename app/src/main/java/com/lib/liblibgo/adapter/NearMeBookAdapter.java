package com.lib.liblibgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.dashboard.book_details.BookDetails;
import com.lib.liblibgo.dashboard.book_details.CommunityBookDetailsActivity;
import com.lib.liblibgo.model.NearMeBookModel;

import java.text.DecimalFormat;
import java.util.List;

public class NearMeBookAdapter extends RecyclerView.Adapter<NearMeBookAdapter.CategoryHolder> {
    List<NearMeBookModel.ResModelData.NewrmeBookList> mList;
    Context mCtx;

    public NearMeBookAdapter(List<NearMeBookModel.ResModelData.NewrmeBookList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.near_me_book_row, parent, false);
        return new CategoryHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView tvBookName,tvBookAuthor,tvBookPrice,tvmetter,tvBookRentPrice,tvCommunity;
        ImageView ivBook;
        LinearLayout llRent,llBuy;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookPrice = itemView.findViewById(R.id.tvBookPrice);
            tvBookRentPrice = itemView.findViewById(R.id.tvBookRentPrice);
            tvmetter = itemView.findViewById(R.id.tvmetter);
            ivBook = itemView.findViewById(R.id.ivBook);
            llRent = itemView.findViewById(R.id.llRent);
            llBuy = itemView.findViewById(R.id.llBuy);
            tvCommunity = itemView.findViewById(R.id.tvCommunity);
        }

        public void bind() {
            tvBookName.setText(mList.get(getAdapterPosition()).getBook_name());
            tvBookAuthor.setText("By "+mList.get(getAdapterPosition()).getAuthor_name());

            if (mList.get(getAdapterPosition()).getSelling_type().equals("For Rent")){
                if (mList.get(getAdapterPosition()).getRental_price().equals("0")){
                    tvBookRentPrice.setText("Free");
                }else {
                    tvBookRentPrice.setText("\u20B9 "+mList.get(getAdapterPosition()).getRental_price());
                }

                tvBookPrice.setText("N/A");
                /*llRent.setVisibility(View.VISIBLE);
                llBuy.setVisibility(View.INVISIBLE);*/
            }else if (mList.get(getAdapterPosition()).getSelling_type().equals("For Sale")){
                if (mList.get(getAdapterPosition()).getSale_price().equals("0")){
                    tvBookPrice.setText("Free");
                }else {
                    tvBookPrice.setText("\u20B9 "+mList.get(getAdapterPosition()).getSale_price());
                }

                tvBookRentPrice.setText("N/A");
                /*llRent.setVisibility(View.INVISIBLE);
                llBuy.setVisibility(View.VISIBLE);*/
            }else {
                if (mList.get(getAdapterPosition()).getRental_price().equals("0")){
                    tvBookRentPrice.setText("Free");
                }else {
                    tvBookRentPrice.setText("\u20B9 "+mList.get(getAdapterPosition()).getRental_price());
                }

                if (mList.get(getAdapterPosition()).getSale_price().equals("0")){
                    tvBookPrice.setText("Free");
                }else {
                    tvBookPrice.setText("\u20B9 "+mList.get(getAdapterPosition()).getSale_price());
                }
            }

            if (mList.get(getAdapterPosition()).getImage_url().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivBook);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getImage_url()).into(ivBook);
            }

            tvmetter.setText("1.9km");
            tvmetter.setVisibility(View.VISIBLE);

            if (mList.get(getAdapterPosition()).getLibrary_name() != null){
                if (mList.get(getAdapterPosition()).getLibrary_name().equals("")){
                    tvCommunity.setText("Open");
                }else {
                    tvCommunity.setText(mList.get(getAdapterPosition()).getLibrary_name());
                }
            }



            /*if (mList.get(getAdapterPosition()).getDistance().equals("0") || mList.get(getAdapterPosition()).getDistance().equals("")){
                tvmetter.setText("1.9km");
                tvmetter.setVisibility(View.VISIBLE);
            }else {
                Double distanceInMiles = Double.parseDouble(mList.get(getAdapterPosition()).getDistance());
                Double distanceInKm = 1.60934 * distanceInMiles;
                Double distanceInMeter = distanceInKm / 0.001;
                if (distanceInMeter < 1000){
                    if (distanceInMeter <= 99){
                        tvmetter.setText(new DecimalFormat("#.#").format(distanceInMeter) + " m");
                    }else {
                        tvmetter.setText("99 m");
                    }
                }else {
                    distanceInKm = distanceInMeter * 0.001;
                    if (distanceInKm <= 99){
                        tvmetter.setText(new DecimalFormat("#.#").format(distanceInKm) + " km");
                    }else {
                        tvmetter.setText("99 km");
                    }
                }
                tvmetter.setVisibility(View.VISIBLE);
            }*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, BookDetails.class);
                    intent.putExtra("book_id",mList.get(getAdapterPosition()).getBook_id());
                    intent.putExtra("book_img",mList.get(getAdapterPosition()).getImage_url());
                    mCtx.startActivity(intent);
                }
            });
        }
    }
}
