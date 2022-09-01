package com.lib.liblibgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.dashboard.book_details.BookDetails;
import com.lib.liblibgo.model.BookListModel;
import com.lib.liblibgo.model.NearMeBookModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NearMeBookSearchAdapter extends RecyclerView.Adapter<NearMeBookSearchAdapter.CategoryHolder> {
    List<NearMeBookModel.ResModelData.NewrmeBookList> mList;
    Context mCtx;

    public NearMeBookSearchAdapter(List<NearMeBookModel.ResModelData.NewrmeBookList> mList, Context mCtx) {
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
        TextView tvBookName,tvBookAuthor,tvBookPrice,tvmetter;
        ImageView ivBook,iv_pin;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookPrice = itemView.findViewById(R.id.tvBookPrice);
            tvmetter = itemView.findViewById(R.id.tvmetter);
            ivBook = itemView.findViewById(R.id.ivBook);
            iv_pin = itemView.findViewById(R.id.iv_pin);
        }

        public void bind() {
            tvBookName.setText(mList.get(getAdapterPosition()).getBook_name());
            tvBookAuthor.setText("By "+mList.get(getAdapterPosition()).getAuthor_name());
            tvBookPrice.setText("\u20B9 "+mList.get(getAdapterPosition()).getSale_price());

            if (mList.get(getAdapterPosition()).getImage_url().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivBook);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getImage_url()).into(ivBook);
            }

            if (mList.get(getAdapterPosition()).getDistance().equals("0") || mList.get(getAdapterPosition()).getDistance().equals("")){
                tvmetter.setText("");
                tvmetter.setVisibility(View.GONE);
                iv_pin.setVisibility(View.GONE);
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
                iv_pin.setVisibility(View.VISIBLE);
            }

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

    public void setFilter(List<NearMeBookModel.ResModelData.NewrmeBookList> arrayList) {
        mList = new ArrayList<>();
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }
}
