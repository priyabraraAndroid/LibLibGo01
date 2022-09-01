package com.lib.liblibgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.model.BookDetailsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>  {
    List<BookDetailsModel.ResData.ResDataRes.ReviewListData> mList = Collections.emptyList();
    Context context;

    public ReviewAdapter(List<BookDetailsModel.ResData.ResDataRes.ReviewListData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_create_date;
        TextView tv_comment;
        TextView tv_rate;
        CircleImageView iv_profile;


        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.tv_name2);
            tv_create_date = (TextView)view.findViewById(R.id.tv_create_date);
            tv_comment = (TextView)view.findViewById(R.id.tv_comment);
            tv_rate = (TextView)view.findViewById(R.id.tv_rate);
            iv_profile = (CircleImageView)view.findViewById(R.id.iv_profile);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int i = position;

        /*if (mList.get(position).profile_img == null){
            holder.iv_profile.setImageResource(R.drawable.blank_user);
        }else {
            Glide.with(context).load(mList.get(position).profile_img).into(holder.iv_profile);
        }*/

        if (mList.get(position).getUser_name() == null){
            holder.tv_name.setText("No Name");
        }else {
            holder.tv_name.setText(mList.get(position).getUser_name());
        }

        holder.tv_comment.setText(mList.get(position).getReview());
        if (mList.get(position).getRatings().equals("nan")){
            holder.tv_rate.setText("0.0");
        }else {
            holder.tv_rate.setText(mList.get(position).getRatings());
        }
        holder.tv_create_date.setText(convertTime(mList.get(position).getDate_time()));
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM,yyyy h:mm a");
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




