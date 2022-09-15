package com.lib.liblibgo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_upload.UploadBookActivity;
import com.lib.liblibgo.dashboard.book_upload.fragments.IndividualBookFragment;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.dashboard.trackingbooks.TrackingDetailsActivity;
import com.lib.liblibgo.model.MyOwnBooksModel;
import com.lib.liblibgo.model.subadmin.CreateApartmentAdminModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleBackAdapter extends RecyclerView.Adapter<SaleBackAdapter.LibraryHolder> {
    List<MyOwnBooksModel.ResModelData.MyBookList> mList;
    //List<MyOwnBooksModel.ResModelData.MyBookList.BookIssueInfo> mInfoList = new ArrayList<>();
    Context mCtx;

    public SaleBackAdapter(List<MyOwnBooksModel.ResModelData.MyBookList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.book_list_row, parent, false);
        return new LibraryHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class LibraryHolder extends RecyclerView.ViewHolder {
        TextView tvBookName,tvBookAuthor,tvBookPrice,tvmetter,tvBookRentPrice,tvCommunity,tvPriceDiv;
        ImageView ivBook;
        LinearLayout llRent,llBuy,llLocation;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
           // tvBookName = itemView.findViewById(R.id.tv_book_name);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookPrice = itemView.findViewById(R.id.tvBookPrice);
            tvBookRentPrice = itemView.findViewById(R.id.tvBookRentPrice);
            tvmetter = itemView.findViewById(R.id.tvmetter);
            ivBook = itemView.findViewById(R.id.ivBook);
            llRent = itemView.findViewById(R.id.llRent);
            //llBuy = itemView.findViewById(R.id.llBuy);
            tvCommunity = itemView.findViewById(R.id.tvCommunity);
            tvPriceDiv = itemView.findViewById(R.id.tvPriceDiv);
            llLocation = itemView.findViewById(R.id.llLocation);

        }

        public void bind() {
            tvBookName.setText(mList.get(getAdapterPosition()).getBook_name());
            tvBookAuthor.setText("- "+mList.get(getAdapterPosition()).getAuthor_name());

            tvCommunity.setVisibility(View.INVISIBLE);
            llLocation.setVisibility(View.INVISIBLE);

            if (mList.get(getAdapterPosition()).getImage_url().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivBook);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getImage_url()).into(ivBook);
            }


            if (mList.get(getAdapterPosition()).getSelling_type().equals("For Rent")){
                if (mList.get(getAdapterPosition()).getRental_price().equals("0")){
                    tvBookRentPrice.setText("Free");
                }else {
                    tvBookRentPrice.setText("\u20B9"+mList.get(getAdapterPosition()).getRental_price()+"/day");
                }

                tvPriceDiv.setVisibility(View.GONE);
                tvBookPrice.setText("");
                //tvBookPrice.setText("N/A");

            }else if (mList.get(getAdapterPosition()).getSelling_type().equals("For Sale")){
                if (mList.get(getAdapterPosition()).getSale_price().equals("0")){
                    tvBookPrice.setText("Free");
                }else {
                    tvBookPrice.setText("\u20B9"+mList.get(getAdapterPosition()).getSale_price());
                }

                //tvBookRentPrice.setText("N/A");
                tvPriceDiv.setVisibility(View.GONE);
                tvBookRentPrice.setText("");
            }else {
                tvPriceDiv.setVisibility(View.VISIBLE);
                if (mList.get(getAdapterPosition()).getRental_price().equals("0")){
                    tvBookRentPrice.setText("Free");
                }else {
                    tvBookRentPrice.setText("\u20B9"+mList.get(getAdapterPosition()).getRental_price()+"/day");
                }

                if (mList.get(getAdapterPosition()).getSale_price().equals("0")){
                    tvBookPrice.setText("Free");
                }else {
                    tvBookPrice.setText("\u20B9"+mList.get(getAdapterPosition()).getSale_price());
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*SharedPreferences sharedpreferences = mCtx.getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("community_id",mList.get(getAdapterPosition()).getCommunity_id());
                    editor.commit();
                    Constants.isOwnLibrary = false;*/
                    Intent intent = new Intent(mCtx, UploadBookActivity.class);
                    intent.putExtra("name",mList.get(getAdapterPosition()).getBook_name());
                    intent.putExtra("author",mList.get(getAdapterPosition()).getAuthor_name());
                    intent.putExtra("isbn",mList.get(getAdapterPosition()).getIsbn_no());
                    intent.putExtra("publish_date",mList.get(getAdapterPosition()).getPublish_date());
                    intent.putExtra("description",mList.get(getAdapterPosition()).getDescription());
                    intent.putExtra("imgUrl",mList.get(getAdapterPosition()).getImage_url());

                    intent.putExtra("rental_price",mList.get(getAdapterPosition()).getRental_price());
                    intent.putExtra("rental_duration",mList.get(getAdapterPosition()).getRent_duration());
                    intent.putExtra("book_price",mList.get(getAdapterPosition()).getSale_price());
                    intent.putExtra("category_id",mList.get(getAdapterPosition()).getCategory_id());
                    intent.putExtra("category_name",mList.get(getAdapterPosition()).getCategory_name());
                    intent.putExtra("shelf_id",mList.get(getAdapterPosition()).getShelf_id());
                    intent.putExtra("shelf_name",mList.get(getAdapterPosition()).getShelf_no());

                    intent.putExtra("book_id",mList.get(getAdapterPosition()).getBook_id());
                    intent.putExtra("mrp",mList.get(getAdapterPosition()).getMrp());
                    intent.putExtra("quantity",mList.get(getAdapterPosition()).getQuantity());

                    intent.putExtra("sale_type",mList.get(getAdapterPosition()).getSelling_type());
                    intent.putExtra("book_condition",mList.get(getAdapterPosition()).getBook_condition_type());
                    intent.putExtra("security_money",mList.get(getAdapterPosition()).getSecurity_money());
                    intent.putExtra("giveaway_status",mList.get(getAdapterPosition()).getGiveaway());
                    mCtx.startActivity(intent);
                }
            });
        }
    }

}
