package com.lib.liblibgo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.apartment_admin.ImageGallery;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.library.EditLibraryActivity;
import com.lib.liblibgo.dashboard.profile.ImageCroperActivity;
import com.lib.liblibgo.model.ImageModel;
;import java.util.ArrayList;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>{
    private Context mContext;
    private String mAppend;
    private ArrayList<ImageModel> imgURLs;


    public ImageGalleryAdapter(ArrayList<ImageModel> imgURLs, String mAppend, Context mContext) {
        this.imgURLs = imgURLs;
        this.mAppend = mAppend;
        this.mContext = mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_image;
        ImageView iv_checked;

        public MyViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String imgUrl = imgURLs.get(position).getImage();
        Glide.with(mContext).load(imgUrl).placeholder(R.drawable.no_img).into(holder.iv_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.openPageFrom.equals("fromUserProfile")){
                    Constants.SelectedProfileImage = imgURLs.get(position).getImage();
                    Log.d("myDrive",imgURLs.get(position).getImage());
                    /*Intent intent = new Intent(mContext, UpdateUserProfile.class);
                    mContext.startActivity(intent);*/
                    ((Activity)mContext).onBackPressed();
                }else if (Constants.openPageFrom.equals("fromLibraryProfile")){
                    Constants.SelectedLibraryProfileImage = imgURLs.get(position).getImage();
                    Log.d("myDrive",imgURLs.get(position).getImage());
                    /*Intent intent = new Intent(mContext, CreateLibraryActivity.class);
                    mContext.startActivity(intent);*/
                    //((Activity)mContext).onBackPressed();
                    Constants.openPageFrom = "fromLibraryProfile";
                    Intent intent = new Intent(mContext, ImageCroperActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }else if (Constants.openPageFrom.equals("fromEditLibrary")){
                    Constants.SelectedLibraryEditImage = imgURLs.get(position).getImage();
                    Log.d("myDrive",imgURLs.get(position).getImage());
                    /*Intent intent = new Intent(mContext, CreateLibraryActivity.class);
                    mContext.startActivity(intent);*/
                    //((Activity)mContext).onBackPressed();
                    Constants.openPageFrom = "fromEditLibrary";
                    Intent intent = new Intent(mContext, ImageCroperActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }else if (Constants.openPageFrom.equals("fromBookUpload")){
                    Constants.SelectedBookImage = imgURLs.get(position).getImage();
                    /*Log.d("myDrive",imgURLs.get(position).getImage());
                    if (Constants.SelectedLibraryProfileImage.equals("")){
                        Glide.with(mContext).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(UploadBookDetails.iv_image);
                    }else {
                        Glide.with(mContext).load(Constants.SelectedLibraryProfileImage).placeholder(R.drawable.no_img).into(UploadBookDetails.iv_image);
                    }*/
                    Constants.openPageFrom = "fromBookUpload";
                    //((Activity)mContext).onBackPressed();
                    /*Intent intent = new Intent(mContext, UploadBookDetails.class);
                    mContext.startActivity(intent);*/
                    Intent intent = new Intent(mContext, ImageCroperActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return imgURLs.size();
    }

}
