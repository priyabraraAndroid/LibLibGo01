package com.lib.liblibgo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;

import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.listner.CommunityLibraryClickListener;
import com.lib.liblibgo.model.CommunityModel;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherCommunityAdapter extends RecyclerView.Adapter<OtherCommunityAdapter.LibraryHolder> {
    List<CommunityModel.CommunityResponse.OtherCommunityResponse> mList;
    Context mCtx;
    CommunityLibraryClickListener listener;

    public OtherCommunityAdapter(List<CommunityModel.CommunityResponse.OtherCommunityResponse> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.community_libs, parent, false);
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
        Button btnJoin;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            tvLibraryName = itemView.findViewById(R.id.tvName);
            ivLibLogo = itemView.findViewById(R.id.ivImage);
            btnJoin = itemView.findViewById(R.id.btnJoin);

        }

        public void bind() {
            tvLibraryName.setText(mList.get(getAdapterPosition()).getLibrary_name());

            if (mList.get(getAdapterPosition()).getLibrary_image().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivLibLogo);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getLibrary_image()).into(ivLibLogo);
            }

            if (mList.get(getAdapterPosition()).getCommunity_status() != null){
                if (mList.get(getAdapterPosition()).getCommunity_status().equals("1")){
                    btnJoin.setVisibility(View.GONE);
                }else {
                    btnJoin.setText("Join");
                    btnJoin.setEnabled(true);
                }
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
                        Call<SubmitDataResModel> resCall = holders.sendCommunityLibraryRequest(mList.get(getAdapterPosition()).getLibrary_id(),database.getUserId());
                        resCall.enqueue(new Callback<SubmitDataResModel>() {
                            @Override
                            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                                if (response.isSuccessful()){
                                    if (response.body().getResponse().getCode() == 1){
                                        progressBar.dismiss();
                                        btnJoin.setVisibility(View.GONE);
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

            itemView.setOnClickListener(new View.OnClickListener() {
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
                        Call<CommunityStatusModel> resCall = holders.checkCommunityRequestStatus(mList.get(getAdapterPosition()).getLibrary_id(),database.getUserId());
                        resCall.enqueue(new Callback<CommunityStatusModel>() {
                            @Override
                            public void onResponse(Call<CommunityStatusModel> call, Response<CommunityStatusModel> response) {
                                if (response.isSuccessful()){
                                    if (response.body().getResponse().getCode() == 1){
                                        progressBar.dismiss();
                                        if (response.body().getResponse().getStatus().equals("1")){
                                            SharedPreferences sharedpreferences = mCtx.getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("community_id",response.body().getResponse().getCommunity_id());
                                            editor.commit();
                                            Intent intent = new Intent(mCtx, CollectApartmentBooksActivity.class);
                                            intent.putExtra("lib_id",mList.get(getAdapterPosition()).getLibrary_id());
                                            intent.putExtra("community_id",response.body().getResponse().getCommunity_id());
                                            intent.putExtra("library_user_id",mList.get(getAdapterPosition()).getUser_id());
                                            intent.putExtra("library_name",mList.get(getAdapterPosition()).getLibrary_name());
                                            intent.putExtra("isLibrary","true");
                                            Constants.libraryType = mList.get(getAdapterPosition()).getLibrary_type();
                                            mCtx.startActivity(intent);
                                        }else if (response.body().getResponse().getStatus().equals("2")){
                                            Toast.makeText(mCtx, "Your request is rejected by the community.", Toast.LENGTH_SHORT).show();
                                        }else if (response.body().getResponse().getStatus().equals("0")){
                                            Toast.makeText(mCtx, "Your request is pending yet.", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(mCtx, "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                                    }else {
                                        progressBar.dismiss();
                                        Toast.makeText(mCtx, "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    progressBar.dismiss();
                                    Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CommunityStatusModel> call, Throwable t) {
                                progressBar.dismiss();
                                Toast.makeText(mCtx, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

}
