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
import com.lib.liblibgo.model.NotifyListModel;
import com.lib.liblibgo.model.WishListModel;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListHolder> {
    List<NotifyListModel.ResDataBooks.WishListData> mList;
    Context mCtx;

    public WishListAdapter(List<NotifyListModel.ResDataBooks.WishListData> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public WishListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.wishlist_row, parent, false);
        return new WishListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class WishListHolder extends RecyclerView.ViewHolder {
        TextView tv_book,tv_author,tv_price;
        ImageView vi_book;

        public WishListHolder(@NonNull View itemView) {
            super(itemView);
            tv_book = itemView.findViewById(R.id.tv_book);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_price = itemView.findViewById(R.id.tv_price);
            vi_book = itemView.findViewById(R.id.vi_book);
        }

        public void bind() {
            tv_book.setText(mList.get(getAdapterPosition()).getBook_name());
            tv_author.setText("By "+mList.get(getAdapterPosition()).getAuthorName());
            //tv_price.setText("\u20B9 "+mList.get(getAdapterPosition()).getSale_price());
            //tv_price.setVisibility(View.GONE);
            if (mList.get(getAdapterPosition()).getImage_url().equals("")) {
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            } else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getImage_url()).into(vi_book);
            }

            if (mList.get(getAdapterPosition()).getQuantity().equals("0")){
                tv_price.setText("Book not available.");
            }else {
                tv_price.setText("Book is available.");
            }

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mList.get(getAdapterPosition()).getBook_id().equals("0")){
                        Intent intent = new Intent(mCtx, BookDetails.class);
                        intent.putExtra("book_id",mList.get(getAdapterPosition()).getBook_id());
                        intent.putExtra("book_img",mList.get(getAdapterPosition()).getImage_url());
                        mCtx.startActivity(intent);
                    }else {
                        Intent intent = new Intent(mCtx, BookDetails.class);
                        intent.putExtra("book_id",mList.get(getAdapterPosition()).getBook_id());
                        intent.putExtra("book_img",mList.get(getAdapterPosition()).getImage_url());
                        mCtx.startActivity(intent);
                    }
                }
            });*/
        }
    }
}
