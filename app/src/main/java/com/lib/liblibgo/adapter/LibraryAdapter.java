package com.lib.liblibgo.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.dashboard.book_collect.CollectBookActivity;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.NearMeOwnLibraryModel;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryHolder> {
    List<NearMeOwnLibraryModel.ResModelData.NewrmeLibraryList> mList;
    Context mCtx;

    public LibraryAdapter(List<NearMeOwnLibraryModel.ResModelData.NewrmeLibraryList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.library_row, parent, false);
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
        TextView tvLibraryName,tvOwner,tvAdr;
        ImageView ivLibLogo;
        MaterialRatingBar lib_rating;
        CardView cardView;
        ImageView ivPhone;
        ImageView ivEdit;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            tvLibraryName = itemView.findViewById(R.id.tv_library_name);
            tvOwner = itemView.findViewById(R.id.tv_owner);
            tvAdr = itemView.findViewById(R.id.tv_adr);
            ivLibLogo = itemView.findViewById(R.id.vi_book);
            lib_rating = itemView.findViewById(R.id.lib_rating);
            cardView = itemView.findViewById(R.id.card);
            ivPhone = itemView.findViewById(R.id.ivPhone);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }

        public void bind() {
            tvLibraryName.setText(mList.get(getAdapterPosition()).getLibrary_name());
            tvOwner.setText("Owner, "+mList.get(getAdapterPosition()).getLibrary_owner());
            tvAdr.setText(mList.get(getAdapterPosition()).getLibrary_address());

            if (mList.get(getAdapterPosition()).getLibrary_image().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivLibLogo);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getLibrary_image()).into(ivLibLogo);
            }

            if (mList.get(getAdapterPosition()).getRatings().equals("0")){
                lib_rating.setRating(Float.parseFloat("1.0"));
            }else {
                lib_rating.setRating(Float.parseFloat(mList.get(getAdapterPosition()).getRatings()));
            }

            UserDatabase database = new UserDatabase(mCtx);

            if (mList.get(getAdapterPosition()).getUser_id().equals(database.getUserId())){
                ivPhone.setVisibility(View.GONE);
                ivEdit.setVisibility(View.VISIBLE);
            }else {
                ivEdit.setVisibility(View.GONE);
                if (mList.get(getAdapterPosition()).getAllow_contact().equals("1") || mList.get(getAdapterPosition()).getAllow_contact().equals("2")){
                    ivPhone.setVisibility(View.VISIBLE);
                }else if (mList.get(getAdapterPosition()).getAllow_contact().equals("0")){
                    ivPhone.setVisibility(View.GONE);
                }else {
                    ivPhone.setVisibility(View.GONE);
                }
            }

            if (mList.get(getAdapterPosition()).getAllow_contact().equals("1") || mList.get(getAdapterPosition()).getAllow_contact().equals("2")){
                ivPhone.setVisibility(View.VISIBLE);
            }else if (mList.get(getAdapterPosition()).getAllow_contact().equals("0")){
                ivPhone.setVisibility(View.GONE);
            }else {
                ivPhone.setVisibility(View.GONE);
            }

            ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callLibraryOwner(mList.get(getAdapterPosition()).getContact_no());
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, CollectBookActivity.class);
                    intent.putExtra("lib_id",mList.get(getAdapterPosition()).getLibrary_id());
                    intent.putExtra("library_name",mList.get(getAdapterPosition()).getLibrary_name());
                    //Toast.makeText(mCtx, ""+mList.get(getAdapterPosition()).getLibrary_name(), Toast.LENGTH_SHORT).show();
                    intent.putExtra("isLibrary","true");
                    mCtx.startActivity(intent);
                }
            });

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, SubAdminDashboard.class);
                    Constants.isOwnLibrary = true;
                    Constants.libraryType = "";
                    mCtx.startActivity(intent);
                }
            });

        }
    }

    public void setFilter(List<NearMeOwnLibraryModel.ResModelData.NewrmeLibraryList> arrayList) {
        mList = new ArrayList<>();
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }

    private void callLibraryOwner(String contact_no) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+contact_no));
        if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mCtx,
                    new String[]{Manifest.permission.CALL_PHONE},
                    10);
            return;
        }else {
            try{
                mCtx.startActivity(callIntent);
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(mCtx,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
