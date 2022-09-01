package com.lib.liblibgo.dashboard.trackingbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.SearchAparmentAdapter;
import com.lib.liblibgo.adapter.FlatAdapter;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.FlatListData;
import com.lib.liblibgo.model.MyOwnBooksModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackingDetailsActivity extends AppCompatActivity {

    private String book_name = "";
    private String author = "";
    private String img = "";
    private String user_name = "";
    private String return_date = "";
    private String expected_return_date = "";
    private TextView tv_book_name;
    private TextView tv_author;
    private TextView tv_issued_user;
    private TextView tv_return_date;
    private ImageView vi_book;
    private RecyclerView rvTracking;
    private TrackingAdapter adapter;

    List<MyOwnBooksModel.ResModelData.MyBookList.BookIssueInfo> mInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_details);

        book_name = getIntent().getStringExtra("book_name");
        author = getIntent().getStringExtra("author");
        img = getIntent().getStringExtra("img");
        user_name = getIntent().getStringExtra("user_name");
        return_date = getIntent().getStringExtra("return_date");
        expected_return_date = getIntent().getStringExtra("expected_return_date");

        tv_book_name = (TextView)findViewById(R.id.tv_book_name);
        tv_author = (TextView)findViewById(R.id.tv_author);
        tv_issued_user = (TextView)findViewById(R.id.tv_issued_user);
        tv_return_date = (TextView)findViewById(R.id.tv_return_date);
        vi_book = (ImageView)findViewById(R.id.vi_book);

        tv_book_name.setText(book_name);
        tv_author.setText(author);


        if (user_name.equals("")){
            tv_issued_user.setVisibility(View.GONE);
            //tvTrackBook.setVisibility(View.GONE);
            //view2.setVisibility(View.GONE);
        }else {
            tv_issued_user.setVisibility(View.VISIBLE);
            tv_issued_user.setText("Issues by, "+user_name);
        }

        if (expected_return_date.equals("")){
            tv_return_date.setVisibility(View.GONE);
        }else if (!return_date.equals("")){
            tv_return_date.setText("Returned On "+convertTime(return_date));
        }else {
            tv_return_date.setText("Return On "+convertTime(expected_return_date));
        }

        if (img.equals("")){
            Glide.with(this).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
        }else {
            Glide.with(this).load(img).into(vi_book);
        }

        rvTracking = (RecyclerView)findViewById(R.id.rvTracking);

        mInfoList = Constants.mInfoDetailList;

        if (mInfoList.size() > 0){
            adapter = new TrackingAdapter(mInfoList, TrackingDetailsActivity.this);
            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            // verticalManager.setReverseLayout(true);
            // verticalManager.setStackFromEnd(true);
            rvTracking.setLayoutManager(verticalManager);
            rvTracking.setAdapter(adapter);
        }

    }

    public void onBack(View view) {
        onBackPressed();
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM,yyyy");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }

    public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.MyViewHolder> {
        List<MyOwnBooksModel.ResModelData.MyBookList.BookIssueInfo> mList = Collections.emptyList();
        Context context;
        private OnItemClickListener listener;

        public TrackingAdapter(List<MyOwnBooksModel.ResModelData.MyBookList.BookIssueInfo> mList, Context context) {
            this.mList = mList;
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_issued_user,tv_return_date;

            public MyViewHolder(View view) {
                super(view);
                tv_issued_user = (TextView) view.findViewById(R.id.tv_issued_user);
                tv_return_date = (TextView) view.findViewById(R.id.tv_return_date);
            }

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final int i = position;
            //holder.tv_noti.setText(mList.get(position).content);
            //holder.tvApartmentName.setSelected(true);
            holder.tv_issued_user.setText(mList.get(position).getUser_name());
            //holder.tv_issued_user.setText(mList.get(position).getUser_name());

            if (mList.get(position).getExpected_return_date().equals("")){
                holder.tv_return_date.setVisibility(View.GONE);
            }else if (!mList.get(position).getReturn_date().equals("")){
                holder.tv_return_date.setText("Returned On "+convertTime(mList.get(position).getReturn_date()));
            }else {
                holder.tv_return_date.setText("Return On "+convertTime(mList.get(position).getExpected_return_date()));
            }

        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

    }
}