package com.lib.liblibgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.book_collect.CollectBookActivity;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.model.CategoryListData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    List<CategoryListData> mList;
    Context mCtx;

    public CategoryAdapter(List<CategoryListData> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.category_row, parent, false);
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
        TextView tvCatName;
        CircleImageView catIv;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            catIv = itemView.findViewById(R.id.cat_iv);
        }

        public void bind() {
            tvCatName.setText(mList.get(getAdapterPosition()).getCategory_name());

            if (mList.get(getAdapterPosition()).getCategory_image().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(catIv);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getCategory_image()).placeholder(R.drawable.no_img).into(catIv);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constants.selectedCategoriesCommunity = mList.get(getAdapterPosition()).getCategory_id();
                    Intent intent = new Intent(mCtx, BookCatActivity.class);
                    mCtx.startActivity(intent);
                }
            });
        }
    }
    
}
