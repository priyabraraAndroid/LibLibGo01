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
import com.lib.liblibgo.dashboard.book_collect.CollectBookActivity;
import com.lib.liblibgo.model.NearMeLibraryModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class NearByLibraryAdapter extends RecyclerView.Adapter<NearByLibraryAdapter.LibraryHolder> {
    List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> mList;
    Context mCtx;

    public NearByLibraryAdapter(List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.nearby_library_row, parent, false);
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
        TextView tvLibraryName;
        CircleImageView ivLibLogo;
        MaterialRatingBar lib_rating;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            tvLibraryName = itemView.findViewById(R.id.tv_library_name);
            ivLibLogo = itemView.findViewById(R.id.vi_lib_logo);
            lib_rating = itemView.findViewById(R.id.lib_rating);
        }

        public void bind() {
            tvLibraryName.setText(mList.get(getAdapterPosition()).getLibrary_name());

            if (mList.get(getAdapterPosition()).getLibrary_image().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivLibLogo);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getLibrary_image()).into(ivLibLogo);
            }

            if (mList.get(getAdapterPosition()).getRatings().equals("0.0")){
                lib_rating.setRating(Float.parseFloat("1.0"));
            }else {
                lib_rating.setRating(Float.parseFloat(mList.get(getAdapterPosition()).getRatings()));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, CollectBookActivity.class);
                    intent.putExtra("lib_id",mList.get(getAdapterPosition()).getLibrary_id());
                    intent.putExtra("isLibrary","true");
                    mCtx.startActivity(intent);
                }
            });
        }
    }
}
