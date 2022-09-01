package com.lib.liblibgo.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.book_collect.CollectBookActivity;
import com.lib.liblibgo.dashboard.trackingbooks.TrackingDetailsActivity;
import com.lib.liblibgo.dialogs.FlatDialog;
import com.lib.liblibgo.dialogs.MyCommunitiesDialog;
import com.lib.liblibgo.listner.CommunityLibraryClickListener;
import com.lib.liblibgo.listner.CommunityLibraryClickListenerTwo;
import com.lib.liblibgo.listner.MoveToCommunityClickListener;
import com.lib.liblibgo.model.MyOwnBooksModel;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.SearchResModel;
import com.lib.liblibgo.model.subadmin.CreateApartmentAdminModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOwnBookAdapter extends RecyclerView.Adapter<MyOwnBookAdapter.LibraryHolder> {
    List<MyOwnBooksModel.ResModelData.MyBookList> mList;
    List<MyOwnBooksModel.ResModelData.MyBookList.BookIssueInfo> mInfoList = new ArrayList<>();
    Context mCtx;
    CommunityLibraryClickListenerTwo listener;

    public MyOwnBookAdapter(List<MyOwnBooksModel.ResModelData.MyBookList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.my_own_book_list_row, parent, false);
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
        TextView tvBookName,tv_author,tv_issued_user,tv_return_date,tvTrackBook,tvMoveToCommunity,tv_status;
        ImageView vi_book;
        View view2;
        CheckBox cbAllowActive;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tv_book_name);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_issued_user = itemView.findViewById(R.id.tv_issued_user);
            tv_return_date = itemView.findViewById(R.id.tv_return_date);
            tvTrackBook = itemView.findViewById(R.id.tvTrackBook);
            tvMoveToCommunity = itemView.findViewById(R.id.tvMoveToCommunity);
            vi_book = itemView.findViewById(R.id.vi_book);
            view2 = itemView.findViewById(R.id.view2);
            cbAllowActive = itemView.findViewById(R.id.cbAllowActive);
            tv_status = itemView.findViewById(R.id.tv_status);

        }

        public void bind() {
            tvBookName.setText(mList.get(getAdapterPosition()).getBook_name());
            tv_author.setText(mList.get(getAdapterPosition()).getAuthor_name());

            if (mList.get(getAdapterPosition()).getRecent_issued_user_name().equals("")){
                tv_issued_user.setVisibility(View.GONE);
                //tvTrackBook.setVisibility(View.GONE);
                //view2.setVisibility(View.GONE);
            }else {
                tvTrackBook.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                tv_issued_user.setVisibility(View.VISIBLE);
                tv_issued_user.setText("Issues by, "+mList.get(getAdapterPosition()).getRecent_issued_user_name());
            }

            if (mList.get(getAdapterPosition()).getRecent_expected_return_date().equals("")){
                tv_return_date.setVisibility(View.GONE);
            }else {
                tv_return_date.setText("Return On "+convertTime(mList.get(getAdapterPosition()).getRecent_expected_return_date()));
            }

            if (mList.get(getAdapterPosition()).getImage_url().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getImage_url()).into(vi_book);
            }

            tvTrackBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInfoList = mList.get(getAdapterPosition()).getBook_issue_info();
                    if (mInfoList.size() > 0){
                        Intent intent = new Intent(mCtx, TrackingDetailsActivity.class);
                        Constants.mInfoDetailList = mInfoList;
                        mCtx.startActivity(intent);
                    }else {
                        Toast.makeText(mCtx, "Record Not Available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tvMoveToCommunity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            if (mList.get(getAdapterPosition()).getBook_status().equals("0")){
                cbAllowActive.setChecked(true);
                tv_status.setText("Inactive");
                tv_status.setTextColor(Color.parseColor("#D81406"));
            }else {
                cbAllowActive.setChecked(false);
                tv_status.setText("Active");
                tv_status.setTextColor(Color.parseColor("#07950D"));
            }

            cbAllowActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cbAllowActive.isChecked()){
                        tv_status.setText("Inactive");
                        tv_status.setTextColor(Color.parseColor("#D81406"));
                        bookActiveInactive(mList.get(getAdapterPosition()).getBook_id(),"0",tv_status);
                        //Toast.makeText(mCtx, ""+mList.get(getAdapterPosition()).getBook_name(), Toast.LENGTH_SHORT).show();
                    }else {
                        //Toast.makeText(mCtx, ""+mList.get(getAdapterPosition()).getBook_id(), Toast.LENGTH_SHORT).show();
                        bookActiveInactive(mList.get(getAdapterPosition()).getBook_id(),"1",tv_status);
                        tv_status.setText("Active");
                        tv_status.setTextColor(Color.parseColor("#07950D"));
                    }
                }
            });

        }
    }

    public void setOnItemClickListener(CommunityLibraryClickListenerTwo listener){
        this.listener = listener;
    }

    private void bookActiveInactive(String book_id, String status,TextView textView) {
        final ProgressDialog loginDialog = new ProgressDialog(mCtx);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();

        Holders holders = AllApiClass.getInstance().getApi();
        Call<CreateApartmentAdminModel> modelCall = holders.chnageBookStatus(book_id,status);
        modelCall.enqueue(new Callback<CreateApartmentAdminModel>() {
            @Override
            public void onResponse(Call<CreateApartmentAdminModel> call, Response<CreateApartmentAdminModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        loginDialog.dismiss();
                        Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateApartmentAdminModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(mCtx, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
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

    public void setFilter(List<MyOwnBooksModel.ResModelData.MyBookList> arrayList) {
        mList = new ArrayList<>();
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }
}
