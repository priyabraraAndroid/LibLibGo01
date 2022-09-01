package com.lib.liblibgo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.dashboard.trackingbooks.TrackingDetailsActivity;
import com.lib.liblibgo.model.MyOwnBooksModel;
import com.lib.liblibgo.model.subadmin.CreateApartmentAdminModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCommunityBookAdapter extends RecyclerView.Adapter<MyCommunityBookAdapter.LibraryHolder> {
    List<MyOwnBooksModel.ResModelData.MyBookList> mList;
    List<MyOwnBooksModel.ResModelData.MyBookList.BookIssueInfo> mInfoList = new ArrayList<>();
    Context mCtx;

    public MyCommunityBookAdapter(List<MyOwnBooksModel.ResModelData.MyBookList> mList, Context mCtx) {
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
        ImageView vi_book,iv_edit;
        View view2;
        Switch cbAllowActive;

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
            iv_edit = itemView.findViewById(R.id.iv_edit);

        }

        public void bind() {
            tvBookName.setText(mList.get(getAdapterPosition()).getBook_name());
            tv_author.setText(mList.get(getAdapterPosition()).getAuthor_name());

            tvMoveToCommunity.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);

            if (mList.get(getAdapterPosition()).getSelling_type().equals("For Rent")){
                tvTrackBook.setVisibility(View.VISIBLE);
            }else {
                tvTrackBook.setVisibility(View.GONE);
            }

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
            }else if (!mList.get(getAdapterPosition()).getRecent_return_date().equals("")){
                tv_return_date.setText("Returned On "+convertTime(mList.get(getAdapterPosition()).getRecent_return_date()));
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
                        intent.putExtra("book_name",mList.get(getAdapterPosition()).getBook_name());
                        intent.putExtra("author",mList.get(getAdapterPosition()).getAuthor_name());
                        intent.putExtra("img",mList.get(getAdapterPosition()).getImage_url());
                        intent.putExtra("user_name",mList.get(getAdapterPosition()).getRecent_issued_user_name());
                        intent.putExtra("return_date",mList.get(getAdapterPosition()).getRecent_return_date());
                        intent.putExtra("expected_return_date",mList.get(getAdapterPosition()).getRecent_expected_return_date());
                        Constants.mInfoDetailList = mInfoList;
                        mCtx.startActivity(intent);
                    }else {
                        Toast.makeText(mCtx, "Record Not Available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (mList.get(getAdapterPosition()).getBook_status().equals("0")){
                cbAllowActive.setChecked(false);
                tv_status.setText("Inactive");
                tv_status.setTextColor(Color.parseColor("#D81406"));
            }else {
                cbAllowActive.setChecked(true);
                tv_status.setText("Active");
                tv_status.setTextColor(Color.parseColor("#07950D"));
            }

            cbAllowActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cbAllowActive.isChecked()){
                        bookActiveInactive(mList.get(getAdapterPosition()).getBook_id(),"1",tv_status);
                        tv_status.setText("Active");
                        tv_status.setTextColor(Color.parseColor("#07950D"));
                    }else {
                        bookActiveInactive(mList.get(getAdapterPosition()).getBook_id(),"0",tv_status);
                        //Toast.makeText(mCtx, ""+mList.get(getAdapterPosition()).getBook_name(), Toast.LENGTH_SHORT).show();
                        tv_status.setText("Inactive");
                        tv_status.setTextColor(Color.parseColor("#D81406"));
                        //Toast.makeText(mCtx, ""+mList.get(getAdapterPosition()).getBook_id(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedpreferences = mCtx.getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("community_id",mList.get(getAdapterPosition()).getCommunity_id());
                    editor.commit();
                    Constants.isOwnLibrary = false;
                    Intent intent = new Intent(mCtx, UploadBookDetails.class);
                    intent.putExtra("name",mList.get(getAdapterPosition()).getBook_name());
                    intent.putExtra("author",mList.get(getAdapterPosition()).getAuthor_name());
                    intent.putExtra("isbn",mList.get(getAdapterPosition()).getIsbn_no());
                    intent.putExtra("publish_date",mList.get(getAdapterPosition()).getPublish_date());
                    intent.putExtra("description",mList.get(getAdapterPosition()).getDescription());
                    intent.putExtra("imgUrl",mList.get(getAdapterPosition()).getImage_url());

                    intent.putExtra("rental_price",mList.get(getAdapterPosition()).getRental_price());
                    intent.putExtra("rental_duration",mList.get(getAdapterPosition()).getRent_duration());
                    intent.putExtra("book_price",mList.get(getAdapterPosition()).getSale_price());
                    intent.putExtra("category_id",mList.get(getAdapterPosition()).getCategory_id());
                    intent.putExtra("category_name",mList.get(getAdapterPosition()).getCategory_name());
                    intent.putExtra("shelf_id",mList.get(getAdapterPosition()).getShelf_id());
                    intent.putExtra("shelf_name",mList.get(getAdapterPosition()).getShelf_no());

                    intent.putExtra("book_id",mList.get(getAdapterPosition()).getBook_id());
                    intent.putExtra("mrp",mList.get(getAdapterPosition()).getMrp());
                    intent.putExtra("quantity",mList.get(getAdapterPosition()).getQuantity());

                    intent.putExtra("sale_type",mList.get(getAdapterPosition()).getSelling_type());
                    intent.putExtra("book_condition",mList.get(getAdapterPosition()).getBook_condition_type());
                    intent.putExtra("security_money",mList.get(getAdapterPosition()).getSecurity_money());
                    intent.putExtra("giveaway_status",mList.get(getAdapterPosition()).getGiveaway());
                    mCtx.startActivity(intent);
                }
            });
        }
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
