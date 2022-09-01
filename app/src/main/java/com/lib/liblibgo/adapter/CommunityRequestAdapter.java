package com.lib.liblibgo.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.dashboard.book_collect.CollectBookActivity;
import com.lib.liblibgo.model.CommunityRequestModel;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityRequestAdapter extends RecyclerView.Adapter<CommunityRequestAdapter.LibraryHolder> {
    List<CommunityRequestModel.ResDataBooks.ReqList> mList;
    Context mCtx;
    TextView textView;
    String count;

    public CommunityRequestAdapter(List<CommunityRequestModel.ResDataBooks.ReqList> mList, Context mCtx,TextView textView,String count) {
        this.mList = mList;
        this.mCtx = mCtx;
        this.textView = textView;
        this.count = count;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.community_req_row, parent, false);
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
        TextView tvName;
        TextView tvphone;
        TextView tvAccept;
        TextView tvReject;
        TextView tvOwnerShip;
        TextView tvDate;
        ImageView iv_msg,iv_whatsapp;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvphone = itemView.findViewById(R.id.tvphone);
            tvAccept = itemView.findViewById(R.id.tvAccept);
            tvReject = itemView.findViewById(R.id.tvReject);
            tvOwnerShip = itemView.findViewById(R.id.tvOwnerShip);
            iv_msg = itemView.findViewById(R.id.iv_msg);
            iv_whatsapp = itemView.findViewById(R.id.iv_whatsapp);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void bind() {
            tvName.setText(mList.get(getAdapterPosition()).getRequest_user_name());
            tvphone.setText("+91 "+mList.get(getAdapterPosition()).getRequest_user_mobile());

            if (!mList.get(getAdapterPosition()).getRequested_date().equals("")){
                tvDate.setText("Request On "+convertTime(mList.get(getAdapterPosition()).getRequested_date()));
            }

            if (mList.get(getAdapterPosition()).getCommunity_status().equals("0")){
                tvAccept.setVisibility(View.VISIBLE);
                tvReject.setVisibility(View.VISIBLE);
                tvOwnerShip.setVisibility(View.INVISIBLE);

                tvAccept.setText("Accept");
                tvAccept.setEnabled(true);
                tvAccept.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg));

                tvReject.setText("Decline");
                tvReject.setEnabled(true);
                tvReject.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_red));

            }else if (mList.get(getAdapterPosition()).getCommunity_status().equals("1")){
                tvAccept.setVisibility(View.VISIBLE);
                tvReject.setVisibility(View.VISIBLE);
                tvOwnerShip.setVisibility(View.VISIBLE);

                tvAccept.setText("Accepted");
                tvAccept.setEnabled(false);
                tvAccept.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_grey));

                tvReject.setText("Decline");
                tvReject.setEnabled(true);
                tvReject.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_red));

            }else if (mList.get(getAdapterPosition()).getCommunity_status().equals("2")){
                tvReject.setVisibility(View.VISIBLE);
                tvAccept.setVisibility(View.VISIBLE);
                tvOwnerShip.setVisibility(View.INVISIBLE);

                tvReject.setText("Declined");
                tvReject.setEnabled(false);
                tvReject.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_grey));

                tvAccept.setText("Accept");
                tvAccept.setEnabled(true);
                tvAccept.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg));
            }

            tvAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressBar = new ProgressDialog(mCtx);
                    progressBar.setMessage("Please wait...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    Holders holders = AllApiClass.getInstance().getApi();
                    Call<SubmitDataResModel> resCall = holders.requestAcceptReject(mList.get(getAdapterPosition()).community_id,"1");
                    resCall.enqueue(new Callback<SubmitDataResModel>() {
                        @Override
                        public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                            if (response.isSuccessful()){
                                if (response.body().getResponse().getCode() == 1){
                                    progressBar.dismiss();
                                    tvAccept.setVisibility(View.VISIBLE);
                                    tvReject.setVisibility(View.VISIBLE);
                                    tvOwnerShip.setVisibility(View.VISIBLE);

                                    textView.setText("Number of pending requests (" +String.valueOf(Integer.parseInt(count)-1) +")");

                                    tvAccept.setText("Accepted");
                                    tvAccept.setEnabled(false);
                                    tvAccept.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_grey));

                                    tvReject.setText("Decline");
                                    tvReject.setEnabled(true);
                                    tvReject.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_red));

                                    HomeActivity.flag = 1;
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
            });


            tvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressBar = new ProgressDialog(mCtx);
                    progressBar.setMessage("Please wait...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    Holders holders = AllApiClass.getInstance().getApi();
                    Call<SubmitDataResModel> resCall = holders.requestAcceptReject(mList.get(getAdapterPosition()).community_id,"2");
                    resCall.enqueue(new Callback<SubmitDataResModel>() {
                        @Override
                        public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                            if (response.isSuccessful()){
                                if (response.body().getResponse().getCode() == 1){
                                    progressBar.dismiss();
                                    tvReject.setVisibility(View.VISIBLE);
                                    tvAccept.setVisibility(View.VISIBLE);
                                    tvOwnerShip.setVisibility(View.INVISIBLE);

                                    //textView.setText(String.valueOf(Integer.parseInt(count)+1));

                                    tvReject.setText("Declined");
                                    tvReject.setEnabled(false);
                                    tvReject.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_grey));

                                    tvAccept.setText("Accept");
                                    tvAccept.setEnabled(true);
                                    tvAccept.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg));

                                    HomeActivity.flag = 1;
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
            });

            tvOwnerShip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressBar = new ProgressDialog(mCtx);
                    progressBar.setMessage("Please wait...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    Holders holders = AllApiClass.getInstance().getApi();
                    Call<SubmitDataResModel> resCall = holders.makeOwnerByLibrary(mList.get(getAdapterPosition()).getRequest_user_id(),mList.get(getAdapterPosition()).getLibrary_id());
                    resCall.enqueue(new Callback<SubmitDataResModel>() {
                        @Override
                        public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                            if (response.isSuccessful()){
                                if (response.body().getResponse().getCode() == 1){
                                    progressBar.dismiss();
                                    Intent intent = new Intent(mCtx,HomeActivity.class);
                                    mCtx.startActivity(intent);
                                    ((Activity)mCtx).finishAffinity();
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
            });

            iv_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://api.whatsapp.com/send?phone=91" + mList.get(getAdapterPosition()).getRequest_user_mobile();
                    try {
                        PackageManager pm = view.getContext().getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        view.getContext().startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                }
            });

            iv_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dexter.withContext(mCtx).withPermissions(Manifest.permission.SEND_SMS)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                    String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(mCtx);
                                    Uri _uri = Uri.parse("tel:" +  mList.get(getAdapterPosition()).getRequest_user_mobile());
                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, _uri);
                                    sendIntent.putExtra("address", mList.get(getAdapterPosition()).getRequest_user_mobile());
                                    sendIntent.putExtra("sms_body", "");
                                    sendIntent.setPackage(defaultSmsPackageName);
                                    sendIntent.setType("vnd.android-dir/mms-sms");
                                    mCtx.startActivity(sendIntent);
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }
            });
        }
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }
}
