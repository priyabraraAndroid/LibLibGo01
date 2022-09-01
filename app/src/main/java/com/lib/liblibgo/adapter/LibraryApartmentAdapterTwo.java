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
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.listner.CommunityLibraryClickListener;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryApartmentAdapterTwo extends RecyclerView.Adapter<LibraryApartmentAdapterTwo.LibraryHolder> {
    List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> mList;
    Context mCtx;
    CommunityLibraryClickListener listener;

    public LibraryApartmentAdapterTwo(List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.library_apartment_row_two, parent, false);
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
        TextView tvLibraryName,tvOwner,tvAdr,tvCount,tv_num_users;
        ImageView ivLibLogo;
        MaterialRatingBar lib_rating;
        CardView cardView;
        ImageView ivPhone;
        ImageView ivEdit;
        Button btnJoin;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            tvLibraryName = itemView.findViewById(R.id.tv_library_name);
            tvOwner = itemView.findViewById(R.id.tv_owner);
            tvAdr = itemView.findViewById(R.id.tv_adr);
            ivLibLogo = itemView.findViewById(R.id.vi_book);
            lib_rating = itemView.findViewById(R.id.lib_rating);
            cardView = itemView.findViewById(R.id.card);
            ivPhone = itemView.findViewById(R.id.ivPhone);
            btnJoin = itemView.findViewById(R.id.btnJoin);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvCount = itemView.findViewById(R.id.tvCount);
            tv_num_users = itemView.findViewById(R.id.tv_num_users);
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

            ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callLibraryOwner(mList.get(getAdapterPosition()).getContact_no());
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            if (mList.get(getAdapterPosition()).getTotal_accepted_requests().equals("0")){
                tv_num_users.setVisibility(View.GONE);
            }else {
                tv_num_users.setVisibility(View.VISIBLE);
                tv_num_users.setText(mList.get(getAdapterPosition()).getTotal_accepted_requests() + " People Joined.");
            }


            btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDatabase database = new UserDatabase(mCtx);
                    if (database.getUserId().equals("")){
                        Intent intent = new Intent(mCtx, LoginWithPhoneNumber.class);
                        mCtx.startActivity(intent);
                    }else {
                        final ProgressDialog progressBar = new ProgressDialog(mCtx);
                        progressBar.setMessage("Please wait...");
                        progressBar.setCancelable(false);
                        progressBar.show();
                        Holders holders = AllApiClass.getInstance().getApi();
                        Call<SubmitDataResModel> resCall = holders.leaveCommunity(database.getUserId(),mList.get(getAdapterPosition()).getCommunity_id());
                        resCall.enqueue(new Callback<SubmitDataResModel>() {
                            @Override
                            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                                if (response.isSuccessful()){
                                    if (response.body().getResponse().getCode() == 1){
                                        //alertDialog.dismiss();
                                        progressBar.dismiss();
                                        mList.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                        notifyItemRangeChanged(getAdapterPosition(), mList.size());
                                        Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        progressBar.dismiss();
                                        Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    progressBar.dismiss();
                                    Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                                progressBar.dismiss();
                                Toast.makeText(mCtx, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }
    }

    public void setOnItemClickListener(CommunityLibraryClickListener listener){
        this.listener = listener;
    }

    public void setFilter(List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> arrayList) {
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
