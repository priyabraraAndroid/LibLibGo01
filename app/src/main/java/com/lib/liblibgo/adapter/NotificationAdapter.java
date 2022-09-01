package com.lib.liblibgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.apartment_admin.ManageCommunityUsersActivity;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.library.CommunityLibraryRulesActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.dashboard.profile.OrderDetailsActivityNew;
import com.lib.liblibgo.model.NotificationModel;
import com.lib.liblibgo.views.MyTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    List<NotificationModel.ResData.NotificationList> mList;
    Context mCtx;

    public NotificationAdapter(List<NotificationModel.ResData.NotificationList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.row_notification, parent, false);
        return new NotificationHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        MyTextView tvSubject, tvDate, tvContent;
        FrameLayout flayout;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvContent = itemView.findViewById(R.id.tvContent);
            flayout = itemView.findViewById(R.id.flayout);
        }

        public void bind() {
            tvSubject.setText(mList.get(getAdapterPosition()).getTitle());
            //tvDate.setText(convertTime(mList.get(getAdapterPosition()).getDated()));
            tvDate.setText(convertTime(mList.get(getAdapterPosition()).getDated()));
            tvContent.setText(mList.get(getAdapterPosition()).getMessage());

            if (mList.get(getAdapterPosition()).getRead_status().equals("1")){
                flayout.setBackgroundColor(mCtx.getResources().getColor(R.color.white));
            }else {
                flayout.setBackgroundColor(mCtx.getResources().getColor(R.color.grey_light_secondary));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mList.get(getAdapterPosition()).getOrder_id().equals("0")){
                        Intent intent = new Intent(mCtx, OrderDetailsActivityNew.class);
                        intent.putExtra("order_id",mList.get(getAdapterPosition()).getOrder_id());
                        mCtx.startActivity(intent);
                    }else if (mList.get(getAdapterPosition()).getNotification_status().equals("1")){
                        Intent intent = new Intent(mCtx, ManageCommunityUsersActivity.class);
                        mCtx.startActivity(intent);
                    }else if (!mList.get(getAdapterPosition()).getCommunity_id().equals("0")){
                        SharedPreferences sharedpreferences = mCtx.getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("community_id",mList.get(getAdapterPosition()).getCommunity_id());
                        editor.commit();
                        Intent intent = new Intent(mCtx, CollectApartmentBooksActivity.class);
                        intent.putExtra("lib_id",mList.get(getAdapterPosition()).getLibrary_id());
                        intent.putExtra("community_id",mList.get(getAdapterPosition()).getCommunity_id());
                        intent.putExtra("library_user_id","");
                        intent.putExtra("library_name",mList.get(getAdapterPosition()).getLibrary_name());
                        //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                        intent.putExtra("isLibrary","true");
                        Constants.libraryType = "virtual";
                        mCtx.startActivity(intent);
                    }
                }
            });
        }
    }

    public void setFilter(List<NotificationModel.ResData.NotificationList> arrayList) {
        mList = new ArrayList<>();
        mList.addAll(arrayList);
        notifyDataSetChanged();
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
