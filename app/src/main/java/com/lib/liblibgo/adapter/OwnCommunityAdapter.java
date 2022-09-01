package com.lib.liblibgo.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.ManageCommunityUsersActivity;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.listner.CommunityLibraryClickListener;
import com.lib.liblibgo.model.CommunityModel;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnCommunityAdapter extends RecyclerView.Adapter<OwnCommunityAdapter.LibraryHolder> {
    List<CommunityModel.CommunityResponse.OwnCommunityResponse> mList;
    Context mCtx;

    public OwnCommunityAdapter(List<CommunityModel.CommunityResponse.OwnCommunityResponse> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.own_community_libs, parent, false);
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
        ImageView ivLibLogo;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            tvLibraryName = itemView.findViewById(R.id.tvName);
            ivLibLogo = itemView.findViewById(R.id.ivImg);

        }

        public void bind() {
            tvLibraryName.setText(mList.get(getAdapterPosition()).getLibrary_name());

            if (mList.get(getAdapterPosition()).getLibrary_image().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivLibLogo);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getLibrary_image()).into(ivLibLogo);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, CollectApartmentBooksActivity.class);
                    intent.putExtra("lib_id",mList.get(getAdapterPosition()).getLibrary_id());
                    intent.putExtra("community_id","0");
                    intent.putExtra("library_user_id",mList.get(getAdapterPosition()).getUser_id());
                    intent.putExtra("library_name",mList.get(getAdapterPosition()).getLibrary_name());
                    intent.putExtra("isLibrary","true");
                    Constants.libraryType = mList.get(getAdapterPosition()).getLibrary_type();
                    mCtx.startActivity(intent);
                }
            });

        }
    }


}
